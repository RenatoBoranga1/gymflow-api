package com.renatoboranga.gymflow.service.impl;

import com.renatoboranga.gymflow.dto.request.ClienteCreateRequest;
import com.renatoboranga.gymflow.dto.request.ClienteUpdateRequest;
import com.renatoboranga.gymflow.dto.response.ClienteResponse;
import com.renatoboranga.gymflow.dto.response.PageResponse;
import com.renatoboranga.gymflow.exception.ConflictException;
import com.renatoboranga.gymflow.exception.ResourceNotFoundException;
import com.renatoboranga.gymflow.mapper.ClienteMapper;
import com.renatoboranga.gymflow.model.Cliente;
import com.renatoboranga.gymflow.repository.ClienteRepository;
import com.renatoboranga.gymflow.service.ClienteService;
import java.util.Locale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository repository;

    public ClienteServiceImpl(ClienteRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ClienteResponse> listar(Pageable pageable, String nome, String email) {
        String nomeFiltro = normalize(nome);
        String emailFiltro = normalize(email);
        Page<Cliente> page;
        if (nomeFiltro != null && emailFiltro != null) {
            page = repository.findByNomeContainingIgnoreCaseAndEmailContainingIgnoreCase(
                    nomeFiltro, emailFiltro, pageable);
        } else if (nomeFiltro != null) {
            page = repository.findByNomeContainingIgnoreCase(nomeFiltro, pageable);
        } else if (emailFiltro != null) {
            page = repository.findByEmailContainingIgnoreCase(emailFiltro, pageable);
        } else {
            page = repository.findAll(pageable);
        }
        return PageResponse.from(page.map(ClienteMapper::toResponse));
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponse buscarPorId(Long id) {
        return ClienteMapper.toResponse(findById(id));
    }

    @Override
    @Transactional
    public ClienteResponse criar(ClienteCreateRequest request) {
        assertEmailAvailable(request.email(), null);
        Cliente cliente = repository.save(new Cliente(request.nome(), request.email()));
        return ClienteMapper.toResponse(cliente);
    }

    @Override
    @Transactional
    public ClienteResponse atualizar(Long id, ClienteUpdateRequest request) {
        Cliente cliente = findById(id);
        assertEmailAvailable(request.email(), id);
        cliente.atualizar(request.nome(), request.email());
        return ClienteMapper.toResponse(cliente);
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        repository.delete(findById(id));
    }

    private Cliente findById(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Cliente não encontrado com o id: " + id));
    }

    private void assertEmailAvailable(String email, Long currentId) {
        String normalized = email.trim().toLowerCase(Locale.ROOT);
        boolean alreadyUsed = currentId == null
                ? repository.existsByEmailIgnoreCase(normalized)
                : repository.existsByEmailIgnoreCaseAndIdNot(normalized, currentId);
        if (alreadyUsed) {
            throw new ConflictException("Já existe um cliente com este e-mail");
        }
    }

    private String normalize(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }
}
