package com.renatoboranga.gymflow.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.renatoboranga.gymflow.dto.request.ProfessorCreateRequest;
import com.renatoboranga.gymflow.dto.request.ProfessorUpdateRequest;
import com.renatoboranga.gymflow.exception.ResourceNotFoundException;
import com.renatoboranga.gymflow.model.Professor;
import com.renatoboranga.gymflow.repository.ProfessorRepository;
import com.renatoboranga.gymflow.service.impl.ProfessorServiceImpl;
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
class ProfessorServiceImplTest {

    @Mock
    private ProfessorRepository repository;

    @InjectMocks
    private ProfessorServiceImpl service;

    @Test
    void buscarProfessorInexistenteLancaNotFound() {
        when(repository.findById(7L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPorId(7L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Professor não encontrado com o id: 7");
    }

    @Test
    void criarRemoveEspacosDoNome() {
        when(repository.save(any(Professor.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = service.criar(new ProfessorCreateRequest(" Maria "));

        assertThat(response.nome()).isEqualTo("Maria");
    }

    @Test
    void atualizarMudaNomeDoProfessor() {
        Professor professor = new Professor("Maria");
        when(repository.findById(1L)).thenReturn(Optional.of(professor));

        var response = service.atualizar(1L, new ProfessorUpdateRequest("Joana"));

        assertThat(response.nome()).isEqualTo("Joana");
    }

    @Test
    void listarUsaFiltroDeNome() {
        PageRequest pageable = PageRequest.of(0, 20);
        when(repository.findByNomeContainingIgnoreCase("Maria", pageable))
                .thenReturn(new PageImpl<>(List.of(new Professor("Maria")), pageable, 1));

        var response = service.listar(pageable, " Maria ");

        assertThat(response.content()).hasSize(1);
        verify(repository).findByNomeContainingIgnoreCase("Maria", pageable);
    }
}
