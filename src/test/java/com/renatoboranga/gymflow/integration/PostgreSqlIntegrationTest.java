package com.renatoboranga.gymflow.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.renatoboranga.gymflow.model.Cliente;
import com.renatoboranga.gymflow.model.Plano;
import com.renatoboranga.gymflow.model.Professor;
import com.renatoboranga.gymflow.model.Treino;
import com.renatoboranga.gymflow.model.Role;
import com.renatoboranga.gymflow.model.UserAccount;
import com.renatoboranga.gymflow.repository.ClienteRepository;
import com.renatoboranga.gymflow.repository.PlanoRepository;
import com.renatoboranga.gymflow.repository.ProfessorRepository;
import com.renatoboranga.gymflow.repository.TreinoRepository;
import com.renatoboranga.gymflow.repository.UserAccountRepository;
import com.jayway.jsonpath.JsonPath;
import java.time.LocalDate;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PostgreSqlIntegrationTest {

    @Container
    static final PostgreSQLContainer POSTGRES =
            new PostgreSQLContainer(DockerImageName.parse("postgres:17-alpine"))
                    .withDatabaseName("gymflow_test")
                    .withUsername("gymflow_test")
                    .withPassword("test-only-password");

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("gymflow.security.jwt.secret", PostgreSqlIntegrationTest::randomSecret);
    }

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private PlanoRepository planoRepository;

    @Autowired
    private TreinoRepository treinoRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void flywayCriaSchemaVersionado() {
        Integer domainTableCount = jdbcTemplate.queryForObject("""
                SELECT COUNT(*)
                FROM information_schema.tables
                WHERE table_schema = 'public'
                  AND table_name IN ('clientes', 'professores', 'planos', 'treinos', 'user_accounts')
                """, Integer.class);
        Integer migrationCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM flyway_schema_history WHERE success = TRUE",
                Integer.class);

        assertThat(domainTableCount).isEqualTo(5);
        assertThat(migrationCount).isEqualTo(5);
    }

    @Test
    void mappingsPersistemRelacionamentosNoPostgreSql() {
        Cliente cliente = clienteRepository.save(new Cliente("Ana", "ana@example.com"));
        Professor professor = professorRepository.save(new Professor("Maria"));
        Plano plano = planoRepository.save(new Plano("Força", 4, cliente));
        LocalDate data = LocalDate.of(2026, 8, 20);
        Treino treino = treinoRepository.saveAndFlush(
                new Treino("Agachamento", data, plano, professor));

        Treino persisted = treinoRepository.findById(treino.getId()).orElseThrow();

        assertThat(persisted.getData()).isEqualTo(data);
        assertThat(persisted.getPlano().getCliente().getEmail()).isEqualTo("ana@example.com");
        assertThat(persisted.getProfessor().getNome()).isEqualTo("Maria");
    }

    @Test
    void rotaProtegidaSemTokenRetorna401Estruturado() throws Exception {
        mockMvc.perform(get("/api/v1/clientes"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.path").value("/api/v1/clientes"));
    }

    @Test
    void usuarioPodeLerMasNaoPodeAlterarDominio() throws Exception {
        String email = "user-" + UUID.randomUUID() + "@example.test";
        String password = randomPassword();
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}"))
                .andExpect(status().isCreated());

        String token = login(email, password);
        mockMvc.perform(get("/api/v1/clientes").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/v1/clientes")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Ana\",\"email\":\"ana@example.test\"}"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403));
    }

    @Test
    void administradorPodeAlterarDominio() throws Exception {
        String email = "admin-" + UUID.randomUUID() + "@example.test";
        String password = randomPassword();
        userAccountRepository.saveAndFlush(new UserAccount(
                email, passwordEncoder.encode(password), Role.ADMIN));

        String token = login(email, password);
        mockMvc.perform(post("/api/v1/clientes")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Ana\",\"email\":\"ana@example.test\"}"))
                .andExpect(status().isCreated());
    }

    private String login(String email, String password) throws Exception {
        String response = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return JsonPath.read(response, "$.accessToken");
    }

    private static String randomSecret() {
        byte[] secret = new byte[32];
        new SecureRandom().nextBytes(secret);
        return Base64.getEncoder().encodeToString(secret);
    }

    private static String randomPassword() {
        return "Aa1!" + UUID.randomUUID();
    }
}
