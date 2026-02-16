package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.dto.ClientDTO;
import com.epam.rd.autocode.spring.project.model.Client;
import com.epam.rd.autocode.spring.project.repo.ClientRepository;
import com.epam.rd.autocode.spring.project.service.impl.ClientServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ClientServiceImpl clientService;

    @Test
    void testGetClientByEmail_Success() {
        String email = "test@client.com";
        Client client = new Client();
        ClientDTO clientDto = new ClientDTO();

        when(clientRepository.findByEmail(email)).thenReturn(Optional.of(client));
        when(modelMapper.map(client, ClientDTO.class)).thenReturn(clientDto);

        ClientDTO result = clientService.getClientByEmail(email);

        assertNotNull(result);
        verify(clientRepository).findByEmail(email);
    }

    @Test
    void testUpdateClient_Success() {
        String email = "test@client.com";
        Client client = new Client();
        ClientDTO dto = new ClientDTO();
        dto.setEmail(email);

        when(clientRepository.findByEmail(email)).thenReturn(Optional.of(client));
        when(modelMapper.map(dto, Client.class)).thenReturn(client);

        clientService.updateClientByEmail(email, dto);

        verify(clientRepository).save(client);
    }
}
