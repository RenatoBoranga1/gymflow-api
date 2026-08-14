package com.renatoboranga.gymflow.service;

import com.renatoboranga.gymflow.dto.request.TreinoCreateRequest;
import com.renatoboranga.gymflow.dto.request.TreinoUpdateRequest;
import com.renatoboranga.gymflow.dto.response.PageResponse;
import com.renatoboranga.gymflow.dto.response.TreinoResponse;
import org.springframework.data.domain.Pageable;

public interface TreinoService {

    PageResponse<TreinoResponse> listar(Pageable pageable, Long planoId);

    TreinoResponse buscarPorId(Long id);

    TreinoResponse criar(TreinoCreateRequest request);

    TreinoResponse atualizar(Long id, TreinoUpdateRequest request);

    void excluir(Long id);
}
