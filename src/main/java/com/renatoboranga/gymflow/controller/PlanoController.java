package com.renatoboranga.gymflow.controller;

import com.renatoboranga.gymflow.dto.request.PlanoCreateRequest;
import com.renatoboranga.gymflow.dto.request.PlanoUpdateRequest;
import com.renatoboranga.gymflow.dto.response.PageResponse;
import com.renatoboranga.gymflow.dto.response.PlanoResponse;
import com.renatoboranga.gymflow.service.PlanoService;
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
@RequestMapping("/api/v1/planos")
public class PlanoController {

    private final PlanoService service;

    public PlanoController(PlanoService service) {
        this.service = service;
    }

    @GetMapping
    public PageResponse<PlanoResponse> listar(
            @PageableDefault(size = 20, sort = "nome") Pageable pageable,
            @RequestParam(required = false) Long clienteId) {
        return service.listar(pageable, clienteId);
    }

    @GetMapping("/{id}")
    public PlanoResponse buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<PlanoResponse> criar(
            @Valid @RequestBody PlanoCreateRequest request) {
        PlanoResponse response = service.criar(request);
        return ResponseEntity.created(location(response.id())).body(response);
    }

    @PutMapping("/{id}")
    public PlanoResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PlanoUpdateRequest request) {
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
