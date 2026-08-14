package com.renatoboranga.gymflow.mapper;

import com.renatoboranga.gymflow.dto.response.ClienteResponse;
import com.renatoboranga.gymflow.model.Cliente;

public final class ClienteMapper {

    private ClienteMapper() {
    }

    public static ClienteResponse toResponse(Cliente cliente) {
        return new ClienteResponse(cliente.getId(), cliente.getNome(), cliente.getEmail());
    }
}
