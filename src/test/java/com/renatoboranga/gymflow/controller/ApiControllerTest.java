package com.renatoboranga.gymflow.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.renatoboranga.gymflow.dto.request.ClienteCreateRequest;
import com.renatoboranga.gymflow.dto.request.PlanoCreateRequest;
import com.renatoboranga.gymflow.dto.request.ProfessorCreateRequest;
import com.renatoboranga.gymflow.dto.request.TreinoCreateRequest;
import com.renatoboranga.gymflow.dto.response.ClienteResponse;
import com.renatoboranga.gymflow.dto.response.PageResponse;
import com.renatoboranga.gymflow.dto.response.PlanoResponse;
import com.renatoboranga.gymflow.dto.response.ProfessorResponse;
import com.renatoboranga.gymflow.dto.response.TreinoResponse;
import com.renatoboranga.gymflow.exception.ConflictException;
import com.renatoboranga.gymflow.exception.GlobalExceptionHandler;
import com.renatoboranga.gymflow.exception.ResourceNotFoundException;
import com.renatoboranga.gymflow.service.ClienteService;
import com.renatoboranga.gymflow.service.PlanoService;
import com.renatoboranga.gymflow.service.ProfessorService;
import com.renatoboranga.gymflow.service.TreinoService;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {
        ClienteController.class,
        ProfessorController.class,
        PlanoController.class,
        TreinoController.class
})
@Import(GlobalExceptionHandler.class)
class ApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClienteService clienteService;

    @MockitoBean
    private ProfessorService professorService;

    @MockitoBean
    private PlanoService planoService;

    @MockitoBean
    private TreinoService treinoService;

    @Test
    void clienteInexistenteRetorna404Estruturado() throws Exception {
        when(clienteService.buscarPorId(99L)).thenThrow(
                new ResourceNotFoundException("Cliente não encontrado com o id: 99"));

        mockMvc.perform(get("/api/v1/clientes/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Cliente não encontrado com o id: 99"))
                .andExpect(jsonPath("$.path").value("/api/v1/clientes/99"));
    }

    @Test
    void criarClienteRetorna201ELocation() throws Exception {
        when(clienteService.criar(any(ClienteCreateRequest.class)))
                .thenReturn(new ClienteResponse(1L, "Ana", "ana@example.com"));

        mockMvc.perform(post("/api/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"Ana","email":"ana@example.com"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/v1/clientes/1"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void clienteInvalidoRetorna400ComViolacoes() throws Exception {
        mockMvc.perform(post("/api/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"","email":"invalido"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Dados de entrada inválidos"))
                .andExpect(jsonPath("$.violations.nome").exists())
                .andExpect(jsonPath("$.violations.email").exists());
    }

    @Test
    void emailDuplicadoRetorna409() throws Exception {
        when(clienteService.criar(any(ClienteCreateRequest.class)))
                .thenThrow(new ConflictException("Já existe um cliente com este e-mail"));

        mockMvc.perform(post("/api/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"Ana","email":"ana@example.com"}
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void excluirClienteRetorna204() throws Exception {
        doNothing().when(clienteService).excluir(1L);

        mockMvc.perform(delete("/api/v1/clientes/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(clienteService).excluir(1L);
    }

    @Test
    void listarClientesRetornaMetadadosDePaginacao() throws Exception {
        PageResponse<ClienteResponse> page = new PageResponse<>(
                List.of(new ClienteResponse(1L, "Ana", "ana@example.com")),
                0, 20, 1, 1, true, true);
        when(clienteService.listar(any(Pageable.class), eq("Ana"), isNull())).thenReturn(page);

        mockMvc.perform(get("/api/v1/clientes").param("nome", "Ana"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nome").value("Ana"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void criarProfessorRetorna201() throws Exception {
        when(professorService.criar(any(ProfessorCreateRequest.class)))
                .thenReturn(new ProfessorResponse(2L, "Maria"));

        mockMvc.perform(post("/api/v1/professores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Maria\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2));
    }

    @Test
    void criarPlanoRetorna201() throws Exception {
        when(planoService.criar(any(PlanoCreateRequest.class)))
                .thenReturn(new PlanoResponse(3L, "Força", 4, 1L, "Ana"));

        mockMvc.perform(post("/api/v1/planos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"Força","numeroTreinos":4,"clienteId":1}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clienteId").value(1));
    }

    @Test
    void criarTreinoAceitaDataIso() throws Exception {
        LocalDate data = LocalDate.of(2026, 8, 20);
        when(treinoService.criar(any(TreinoCreateRequest.class))).thenReturn(
                new TreinoResponse(4L, "Agachamento", data, 3L, "Força", 2L, "Maria"));

        mockMvc.perform(post("/api/v1/treinos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "descricao":"Agachamento",
                                  "data":"2026-08-20",
                                  "planoId":3,
                                  "professorId":2
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").value("2026-08-20"));
    }

    @Test
    void dataDeTreinoMalformadaRetorna400() throws Exception {
        mockMvc.perform(post("/api/v1/treinos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "descricao":"Agachamento",
                                  "data":"20/08/2026",
                                  "planoId":3,
                                  "professorId":2
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Requisição inválida"));
    }
}
