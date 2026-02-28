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

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
        Client existingClient = new Client();
        existingClient.setEmail(email);
        existingClient.setName("Old Name");
        existingClient.setBalance(BigDecimal.ZERO);

        ClientDTO updateDto = new ClientDTO();
        updateDto.setName("New Name");
        updateDto.setBalance(new BigDecimal("100.00"));

        when(clientRepository.findByEmail(email)).thenReturn(Optional.of(existingClient));
        when(clientRepository.save(any(Client.class))).thenReturn(existingClient);

        when(modelMapper.map(any(Client.class), eq(ClientDTO.class))).thenReturn(updateDto);

        clientService.updateClientByEmail(email, updateDto);

        assertEquals("New Name", existingClient.getName());
        assertEquals(new BigDecimal("100.00"), existingClient.getBalance());
        verify(clientRepository).save(existingClient);
    }
}
