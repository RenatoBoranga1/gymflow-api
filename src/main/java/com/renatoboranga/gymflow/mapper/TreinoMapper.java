package com.renatoboranga.gymflow.mapper;

import com.renatoboranga.gymflow.dto.response.TreinoResponse;
import com.renatoboranga.gymflow.model.Treino;

public final class TreinoMapper {

    private TreinoMapper() {
    }

    public static TreinoResponse toResponse(Treino treino) {
        return new TreinoResponse(
                treino.getId(),
                treino.getDescricao(),
                treino.getData(),
                treino.getPlano().getId(),
                treino.getPlano().getNome(),
                treino.getProfessor().getId(),
                treino.getProfessor().getNome());
    }
}
