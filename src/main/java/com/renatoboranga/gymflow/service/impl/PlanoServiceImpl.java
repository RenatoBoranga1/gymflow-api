package com.renatoboranga.gymflow.service.impl;

import com.renatoboranga.gymflow.dto.request.PlanoCreateRequest;
import com.renatoboranga.gymflow.dto.request.PlanoUpdateRequest;
import com.renatoboranga.gymflow.dto.response.PageResponse;
import com.renatoboranga.gymflow.dto.response.PlanoResponse;
import com.renatoboranga.gymflow.exception.ResourceNotFoundException;
import com.renatoboranga.gymflow.mapper.PlanoMapper;
import com.renatoboranga.gymflow.model.Cliente;
import com.renatoboranga.gymflow.model.Plano;
import com.renatoboranga.gymflow.repository.ClienteRepository;
import com.renatoboranga.gymflow.repository.PlanoRepository;
import com.renatoboranga.gymflow.service.PlanoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlanoServiceImpl implements PlanoService {

    private final PlanoRepository repository;
    private final ClienteRepository clienteRepository;

    public PlanoServiceImpl(PlanoRepository repository, ClienteRepository clienteRepository) {
        this.repository = repository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PlanoResponse> listar(Pageable pageable, Long clienteId) {
        Page<Plano> page = clienteId == null
                ? repository.findAll(pageable)
                : repository.findByClienteId(clienteId, pageable);
        return PageResponse.from(page.map(PlanoMapper::toResponse));
    }

    @Override
    @Transactional(readOnly = true)
    public PlanoResponse buscarPorId(Long id) {
        return PlanoMapper.toResponse(findById(id));
    }

    @Override
    @Transactional
    public PlanoResponse criar(PlanoCreateRequest request) {
        Cliente cliente = findCliente(request.clienteId());
        Plano plano = repository.save(new Plano(request.nome(), request.numeroTreinos(), cliente));
        return PlanoMapper.toResponse(plano);
    }

    @Override
    @Transactional
    public PlanoResponse atualizar(Long id, PlanoUpdateRequest request) {
        Plano plano = findById(id);
        Cliente cliente = findCliente(request.clienteId());
        plano.atualizar(request.nome(), request.numeroTreinos(), cliente);
        return PlanoMapper.toResponse(plano);
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        repository.delete(findById(id));
    }

    private Plano findById(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Plano não encontrado com o id: " + id));
    }

    private Cliente findCliente(Long id) {
        return clienteRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Cliente não encontrado com o id: " + id));
    }
}
