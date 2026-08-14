package com.renatoboranga.gymflow.mapper;

import com.renatoboranga.gymflow.dto.response.ProfessorResponse;
import com.renatoboranga.gymflow.model.Professor;

public final class ProfessorMapper {

    private ProfessorMapper() {
    }

    public static ProfessorResponse toResponse(Professor professor) {
        return new ProfessorResponse(professor.getId(), professor.getNome());
    }
}
