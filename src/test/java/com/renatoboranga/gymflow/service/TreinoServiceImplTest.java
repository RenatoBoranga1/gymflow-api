package com.renatoboranga.gymflow.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.renatoboranga.gymflow.dto.request.TreinoCreateRequest;
import com.renatoboranga.gymflow.exception.ResourceNotFoundException;
import com.renatoboranga.gymflow.model.Cliente;
import com.renatoboranga.gymflow.model.Plano;
import com.renatoboranga.gymflow.model.Professor;
import com.renatoboranga.gymflow.model.Treino;
import com.renatoboranga.gymflow.repository.PlanoRepository;
import com.renatoboranga.gymflow.repository.ProfessorRepository;
import com.renatoboranga.gymflow.repository.TreinoRepository;
import com.renatoboranga.gymflow.service.impl.TreinoServiceImpl;
import java.time.LocalDate;
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
class TreinoServiceImplTest {

    @Mock
    private TreinoRepository repository;

    @Mock
    private PlanoRepository planoRepository;

    @Mock
    private ProfessorRepository professorRepository;

    @InjectMocks
    private TreinoServiceImpl service;

    @Test
    void criarExigePlanoExistente() {
        when(planoRepository.findById(8L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.criar(request(8L, 3L)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Plano não encontrado com o id: 8");
    }

    @Test
    void criarExigeProfessorExistente() {
        when(planoRepository.findById(2L)).thenReturn(Optional.of(plano(2L)));
        when(professorRepository.findById(3L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.criar(request(2L, 3L)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Professor não encontrado com o id: 3");
    }

    @Test
    void criarPreservaDataComoLocalDate() {
        Plano plano = plano(2L);
        Professor professor = professor(3L);
        when(planoRepository.findById(2L)).thenReturn(Optional.of(plano));
        when(professorRepository.findById(3L)).thenReturn(Optional.of(professor));
        when(repository.save(any(Treino.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = service.criar(request(2L, 3L));

        assertThat(response.data()).isEqualTo(LocalDate.of(2026, 8, 20));
        assertThat(response.planoId()).isEqualTo(2L);
        assertThat(response.professorId()).isEqualTo(3L);
    }

    @Test
    void buscarTreinoInexistenteLancaNotFound() {
        when(repository.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPorId(10L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void listarFiltraPorPlano() {
        PageRequest pageable = PageRequest.of(0, 20);
        Plano plano = plano(2L);
        Treino treino = new Treino("Agachamento", LocalDate.of(2026, 8, 20), plano, professor(3L));
        when(repository.findByPlanoId(2L, pageable)).thenReturn(
                new PageImpl<>(List.of(treino), pageable, 1));

        var response = service.listar(pageable, 2L);

        assertThat(response.content()).hasSize(1);
        verify(repository).findByPlanoId(2L, pageable);
    }

    private TreinoCreateRequest request(Long planoId, Long professorId) {
        return new TreinoCreateRequest(
                "Agachamento", LocalDate.of(2026, 8, 20), planoId, professorId);
    }

    private Plano plano(Long id) {
        Cliente cliente = new Cliente("Ana", "ana@example.com");
        ReflectionTestUtils.setField(cliente, "id", 1L);
        Plano plano = new Plano("Força", 4, cliente);
        ReflectionTestUtils.setField(plano, "id", id);
        return plano;
    }

    private Professor professor(Long id) {
        Professor professor = new Professor("Maria");
        ReflectionTestUtils.setField(professor, "id", id);
        return professor;
    }
}
