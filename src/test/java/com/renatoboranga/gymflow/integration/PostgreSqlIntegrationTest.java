package com.renatoboranga.gymflow.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.renatoboranga.gymflow.model.Cliente;
import com.renatoboranga.gymflow.model.Plano;
import com.renatoboranga.gymflow.model.Professor;
import com.renatoboranga.gymflow.model.Treino;
import com.renatoboranga.gymflow.repository.ClienteRepository;
import com.renatoboranga.gymflow.repository.PlanoRepository;
import com.renatoboranga.gymflow.repository.ProfessorRepository;
import com.renatoboranga.gymflow.repository.TreinoRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@SpringBootTest
@Transactional
class PostgreSqlIntegrationTest {

    @Container
    static final PostgreSQLContainer POSTGRES =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:17-alpine"))
                    .withDatabaseName("gymflow_test")
                    .withUsername("gymflow_test")
                    .withPassword("test-only-password");

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
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

    @Test
    void flywayCriaSchemaVersionado() {
        Integer domainTableCount = jdbcTemplate.queryForObject("""
                SELECT COUNT(*)
                FROM information_schema.tables
                WHERE table_schema = 'public'
                  AND table_name IN ('clientes', 'professores', 'planos', 'treinos')
                """, Integer.class);
        Integer migrationCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM flyway_schema_history WHERE success = TRUE",
                Integer.class);

        assertThat(domainTableCount).isEqualTo(4);
        assertThat(migrationCount).isEqualTo(4);
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
}
