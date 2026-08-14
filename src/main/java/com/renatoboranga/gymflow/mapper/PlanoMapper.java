package com.renatoboranga.gymflow.mapper;

import com.renatoboranga.gymflow.dto.response.PlanoResponse;
import com.renatoboranga.gymflow.model.Plano;

public final class PlanoMapper {

    private PlanoMapper() {
    }

    public static PlanoResponse toResponse(Plano plano) {
        return new PlanoResponse(
                plano.getId(),
                plano.getNome(),
                plano.getNumeroTreinos(),
                plano.getCliente().getId(),
                plano.getCliente().getNome());
    }
}
