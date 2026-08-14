package com.renatoboranga.gymflow.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.renatoboranga.gymflow.dto.request.PlanoCreateRequest;
import com.renatoboranga.gymflow.dto.request.PlanoUpdateRequest;
import com.renatoboranga.gymflow.exception.ResourceNotFoundException;
import com.renatoboranga.gymflow.model.Cliente;
import com.renatoboranga.gymflow.model.Plano;
import com.renatoboranga.gymflow.repository.ClienteRepository;
import com.renatoboranga.gymflow.repository.PlanoRepository;
import com.renatoboranga.gymflow.service.impl.PlanoServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class PlanoServiceImplTest {

    @Mock
    private PlanoRepository repository;

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private PlanoServiceImpl service;

    @Test
    void criarExigeClienteExistente() {
        when(clienteRepository.findById(9L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.criar(new PlanoCreateRequest("Força", 4, 9L)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Cliente não encontrado com o id: 9");
    }

    @Test
    void criarPlanoComClienteValido() {
        Cliente cliente = cliente(2L);
        when(clienteRepository.findById(2L)).thenReturn(Optional.of(cliente));
        when(repository.save(any(Plano.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = service.criar(new PlanoCreateRequest("Força", 4, 2L));

        assertThat(response.nome()).isEqualTo("Força");
        assertThat(response.clienteId()).isEqualTo(2L);
    }

    @Test
    void atualizarExigePlanoExistente() {
        when(repository.findById(4L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.atualizar(
                4L, new PlanoUpdateRequest("Força", 4, 2L)))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void listarFiltraPorCliente() {
        PageRequest pageable = PageRequest.of(0, 20);
        Cliente cliente = cliente(2L);
        when(repository.findByClienteId(2L, pageable)).thenReturn(
                new PageImpl<>(List.of(new Plano("Força", 4, cliente)), pageable, 1));

        var response = service.listar(pageable, 2L);

        assertThat(response.content()).hasSize(1);
        verify(repository).findByClienteId(2L, pageable);
    }

    private Cliente cliente(Long id) {
        Cliente cliente = new Cliente("Ana", "ana@example.com");
        ReflectionTestUtils.setField(cliente, "id", id);
        return cliente;
    }
}
