package com.renatoboranga.gymflow.controller;

import com.renatoboranga.gymflow.dto.request.TreinoCreateRequest;
import com.renatoboranga.gymflow.dto.request.TreinoUpdateRequest;
import com.renatoboranga.gymflow.dto.response.PageResponse;
import com.renatoboranga.gymflow.dto.response.TreinoResponse;
import com.renatoboranga.gymflow.service.TreinoService;
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
@RequestMapping("/api/v1/treinos")
public class TreinoController {

    private final TreinoService service;

    public TreinoController(TreinoService service) {
        this.service = service;
    }

    @GetMapping
    public PageResponse<TreinoResponse> listar(
            @PageableDefault(size = 20, sort = "data") Pageable pageable,
            @RequestParam(required = false) Long planoId) {
        return service.listar(pageable, planoId);
    }

    @GetMapping("/{id}")
    public TreinoResponse buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<TreinoResponse> criar(
            @Valid @RequestBody TreinoCreateRequest request) {
        TreinoResponse response = service.criar(request);
        return ResponseEntity.created(location(response.id())).body(response);
    }

    @PutMapping("/{id}")
    public TreinoResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody TreinoUpdateRequest request) {
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
