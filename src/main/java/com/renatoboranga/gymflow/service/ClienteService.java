package com.renatoboranga.gymflow.service;

import com.renatoboranga.gymflow.dto.request.ClienteCreateRequest;
import com.renatoboranga.gymflow.dto.request.ClienteUpdateRequest;
import com.renatoboranga.gymflow.dto.response.ClienteResponse;
import com.renatoboranga.gymflow.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

public interface ClienteService {

    PageResponse<ClienteResponse> listar(Pageable pageable, String nome, String email);

    ClienteResponse buscarPorId(Long id);

    ClienteResponse criar(ClienteCreateRequest request);

    ClienteResponse atualizar(Long id, ClienteUpdateRequest request);

    void excluir(Long id);
}
