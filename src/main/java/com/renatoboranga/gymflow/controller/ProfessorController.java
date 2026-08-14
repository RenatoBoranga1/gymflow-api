package com.renatoboranga.gymflow.controller;

import com.renatoboranga.gymflow.dto.request.ProfessorCreateRequest;
import com.renatoboranga.gymflow.dto.request.ProfessorUpdateRequest;
import com.renatoboranga.gymflow.dto.response.PageResponse;
import com.renatoboranga.gymflow.dto.response.ProfessorResponse;
import com.renatoboranga.gymflow.service.ProfessorService;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Validated
@RestController
@RequestMapping("/api/v1/professores")
public class ProfessorController {

    private final ProfessorService service;

    public ProfessorController(ProfessorService service) {
        this.service = service;
    }

    @GetMapping
    public PageResponse<ProfessorResponse> listar(
            @PageableDefault(size = 20, sort = "nome") Pageable pageable,
            @RequestParam(required = false) String nome) {
        return service.listar(pageable, nome);
    }

    @GetMapping("/{id}")
    public ProfessorResponse buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<ProfessorResponse> criar(
            @Valid @RequestBody ProfessorCreateRequest request) {
        ProfessorResponse response = service.criar(request);
        return ResponseEntity.created(location(response.id())).body(response);
    }

    @PutMapping("/{id}")
    public ProfessorResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProfessorUpdateRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

    private URI location(Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
