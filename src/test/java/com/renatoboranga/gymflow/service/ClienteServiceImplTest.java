package com.renatoboranga.gymflow.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.renatoboranga.gymflow.dto.request.ClienteCreateRequest;
import com.renatoboranga.gymflow.dto.request.ClienteUpdateRequest;
import com.renatoboranga.gymflow.exception.ConflictException;
import com.renatoboranga.gymflow.exception.ResourceNotFoundException;
import com.renatoboranga.gymflow.model.Cliente;
import com.renatoboranga.gymflow.repository.ClienteRepository;
import com.renatoboranga.gymflow.service.impl.ClienteServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository repository;

    @InjectMocks
    private ClienteServiceImpl service;

    @Test
    void buscarClienteInexistenteLancaNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPorId(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Cliente não encontrado com o id: 99");
    }

    @Test
    void criarNormalizaEmail() {
        when(repository.existsByEmailIgnoreCase("ana@example.com")).thenReturn(false);
        when(repository.save(any(Cliente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = service.criar(new ClienteCreateRequest(" Ana ", " ANA@EXAMPLE.COM "));

        assertThat(response.nome()).isEqualTo("Ana");
        assertThat(response.email()).isEqualTo("ana@example.com");
    }

    @Test
    void criarEmailDuplicadoRetornaConflito() {
        when(repository.existsByEmailIgnoreCase("ana@example.com")).thenReturn(true);

        assertThatThrownBy(() -> service.criar(
                new ClienteCreateRequest("Ana", "ana@example.com")))
                .isInstanceOf(ConflictException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void atualizarValidaEmailDeOutroCliente() {
        Cliente cliente = new Cliente("Ana", "ana@example.com");
        when(repository.findById(1L)).thenReturn(Optional.of(cliente));
        when(repository.existsByEmailIgnoreCaseAndIdNot("nova@example.com", 1L)).thenReturn(true);

        assertThatThrownBy(() -> service.atualizar(
                1L, new ClienteUpdateRequest("Ana", "nova@example.com")))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    void listarCombinaFiltrosDeNomeEEmail() {
        PageRequest pageable = PageRequest.of(0, 20);
        Cliente cliente = new Cliente("Ana", "ana@example.com");
        when(repository.findByNomeContainingIgnoreCaseAndEmailContainingIgnoreCase(
                "Ana", "example", pageable))
                .thenReturn(new PageImpl<>(List.of(cliente), pageable, 1));

        var page = service.listar(pageable, " Ana ", " example ");

        assertThat(page.content()).hasSize(1);
        assertThat(page.totalElements()).isEqualTo(1);
    }
}
