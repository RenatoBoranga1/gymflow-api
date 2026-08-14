package com.renatoboranga.gymflow.dto.response;

public record PlanoResponse(
        Long id,
        String nome,
        int numeroTreinos,
        Long clienteId,
        String clienteNome) {
}
