package com.renatoboranga.gymflow.service;

import com.renatoboranga.gymflow.dto.request.PlanoCreateRequest;
import com.renatoboranga.gymflow.dto.request.PlanoUpdateRequest;
import com.renatoboranga.gymflow.dto.response.PageResponse;
import com.renatoboranga.gymflow.dto.response.PlanoResponse;
import org.springframework.data.domain.Pageable;

public interface PlanoService {

    PageResponse<PlanoResponse> listar(Pageable pageable, Long clienteId);

    PlanoResponse buscarPorId(Long id);

    PlanoResponse criar(PlanoCreateRequest request);

    PlanoResponse atualizar(Long id, PlanoUpdateRequest request);

    void excluir(Long id);
}
