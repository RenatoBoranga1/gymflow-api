package com.renatoboranga.gymflow.service;

import com.renatoboranga.gymflow.dto.request.ProfessorCreateRequest;
import com.renatoboranga.gymflow.dto.request.ProfessorUpdateRequest;
import com.renatoboranga.gymflow.dto.response.PageResponse;
import com.renatoboranga.gymflow.dto.response.ProfessorResponse;
import org.springframework.data.domain.Pageable;

public interface ProfessorService {

    PageResponse<ProfessorResponse> listar(Pageable pageable, String nome);

    ProfessorResponse buscarPorId(Long id);

    ProfessorResponse criar(ProfessorCreateRequest request);

    ProfessorResponse atualizar(Long id, ProfessorUpdateRequest request);

    void excluir(Long id);
}
