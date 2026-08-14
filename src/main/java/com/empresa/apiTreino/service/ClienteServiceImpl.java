package com.empresa.apiTreino.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.empresa.apiTreino.exception.ResourceNotFoundException;
import com.empresa.apiTreino.model.Cliente;
import com.empresa.apiTreino.repository.ClienteRepository;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }

    @Override
    public Optional<Cliente> getClienteById(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente não encontrado com o id: " + id));
        return Optional.of(cliente);
    }

    @Override
    public Cliente createCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente updateCliente(Long id, Cliente cliente) {
        Cliente existingCliente = getClienteById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com o id: " + id));
        existingCliente.setNome(cliente.getNome());
        existingCliente.setEmail(cliente.getEmail());
        return clienteRepository.save(existingCliente);
    }

    @Override
    public void deleteCliente(Long id) {
        Cliente existingCliente = getClienteById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com o id: " + id));
        clienteRepository.delete(existingCliente);
    }

    @Override
    public boolean existsById(Long id) {
        return clienteRepository.existsById(id);
    }
}
