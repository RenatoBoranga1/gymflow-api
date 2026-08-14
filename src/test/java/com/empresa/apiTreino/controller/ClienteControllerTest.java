package com.empresa.apiTreino.controller;

import com.empresa.apiTreino.exception.GlobalExceptionHandler;
import com.empresa.apiTreino.exception.ResourceNotFoundException;
import com.empresa.apiTreino.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClienteController.class)
@Import(GlobalExceptionHandler.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClienteService clienteService;

    @Test
    void recursoInexistenteRetornaNotFound() throws Exception {
        when(clienteService.getClienteById(99L))
                .thenThrow(new ResourceNotFoundException(
                        "Cliente não encontrado com o id: 99"));

        mockMvc.perform(get("/clientes/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message")
                        .value("Cliente não encontrado com o id: 99"))
                .andExpect(jsonPath("$.path").value("/clientes/99"));
    }
}
