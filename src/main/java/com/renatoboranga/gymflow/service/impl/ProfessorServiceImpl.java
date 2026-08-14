package com.renatoboranga.gymflow.service.impl;

import com.renatoboranga.gymflow.dto.request.ProfessorCreateRequest;
import com.renatoboranga.gymflow.dto.request.ProfessorUpdateRequest;
import com.renatoboranga.gymflow.dto.response.PageResponse;
import com.renatoboranga.gymflow.dto.response.ProfessorResponse;
import com.renatoboranga.gymflow.exception.ResourceNotFoundException;
import com.renatoboranga.gymflow.mapper.ProfessorMapper;
import com.renatoboranga.gymflow.model.Professor;
import com.renatoboranga.gymflow.repository.ProfessorRepository;
import com.renatoboranga.gymflow.service.ProfessorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class ProfessorServiceImpl implements ProfessorService {

    private final ProfessorRepository repository;

    public ProfessorServiceImpl(ProfessorRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProfessorResponse> listar(Pageable pageable, String nome) {
        Page<Professor> page = StringUtils.hasText(nome)
                ? repository.findByNomeContainingIgnoreCase(nome.trim(), pageable)
                : repository.findAll(pageable);
        return PageResponse.from(page.map(ProfessorMapper::toResponse));
    }

    @Override
    @Transactional(readOnly = true)
    public ProfessorResponse buscarPorId(Long id) {
        return ProfessorMapper.toResponse(findById(id));
    }

    @Override
    @Transactional
    public ProfessorResponse criar(ProfessorCreateRequest request) {
        return ProfessorMapper.toResponse(repository.save(new Professor(request.nome())));
    }

    @Override
    @Transactional
    public ProfessorResponse atualizar(Long id, ProfessorUpdateRequest request) {
        Professor professor = findById(id);
        professor.atualizar(request.nome());
        return ProfessorMapper.toResponse(professor);
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        repository.delete(findById(id));
    }

    private Professor findById(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Professor não encontrado com o id: " + id));
    }
}
