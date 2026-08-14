package com.renatoboranga.gymflow.service.impl;

import com.renatoboranga.gymflow.dto.request.TreinoCreateRequest;
import com.renatoboranga.gymflow.dto.request.TreinoUpdateRequest;
import com.renatoboranga.gymflow.dto.response.PageResponse;
import com.renatoboranga.gymflow.dto.response.TreinoResponse;
import com.renatoboranga.gymflow.exception.ResourceNotFoundException;
import com.renatoboranga.gymflow.mapper.TreinoMapper;
import com.renatoboranga.gymflow.model.Plano;
import com.renatoboranga.gymflow.model.Professor;
import com.renatoboranga.gymflow.model.Treino;
import com.renatoboranga.gymflow.repository.PlanoRepository;
import com.renatoboranga.gymflow.repository.ProfessorRepository;
import com.renatoboranga.gymflow.repository.TreinoRepository;
import com.renatoboranga.gymflow.service.TreinoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TreinoServiceImpl implements TreinoService {

    private final TreinoRepository repository;
    private final PlanoRepository planoRepository;
    private final ProfessorRepository professorRepository;

    public TreinoServiceImpl(
            TreinoRepository repository,
            PlanoRepository planoRepository,
            ProfessorRepository professorRepository) {
        this.repository = repository;
        this.planoRepository = planoRepository;
        this.professorRepository = professorRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<TreinoResponse> listar(Pageable pageable, Long planoId) {
        Page<Treino> page = planoId == null
                ? repository.findAll(pageable)
                : repository.findByPlanoId(planoId, pageable);
        return PageResponse.from(page.map(TreinoMapper::toResponse));
    }

    @Override
    @Transactional(readOnly = true)
    public TreinoResponse buscarPorId(Long id) {
        return TreinoMapper.toResponse(findById(id));
    }

    @Override
    @Transactional
    public TreinoResponse criar(TreinoCreateRequest request) {
        Plano plano = findPlano(request.planoId());
        Professor professor = findProfessor(request.professorId());
        Treino treino = repository.save(
                new Treino(request.descricao(), request.data(), plano, professor));
        return TreinoMapper.toResponse(treino);
    }

    @Override
    @Transactional
    public TreinoResponse atualizar(Long id, TreinoUpdateRequest request) {
        Treino treino = findById(id);
        Plano plano = findPlano(request.planoId());
        Professor professor = findProfessor(request.professorId());
        treino.atualizar(request.descricao(), request.data(), plano, professor);
        return TreinoMapper.toResponse(treino);
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        repository.delete(findById(id));
    }

    private Treino findById(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Treino não encontrado com o id: " + id));
    }

    private Plano findPlano(Long id) {
        return planoRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Plano não encontrado com o id: " + id));
    }

    private Professor findProfessor(Long id) {
        return professorRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Professor não encontrado com o id: " + id));
    }
}
