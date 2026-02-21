package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.ClientDTO;
import com.epam.rd.autocode.spring.project.service.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientController.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ClientService clientService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void showEditClientForm_ShouldLoadDataIntoModel() throws Exception {
        ClientDTO client = new ClientDTO();
        client.setEmail("test@client.com");

        when(clientService.getClientByEmail("test@client.com")).thenReturn(client);

        mockMvc.perform(get("/clients/edit/test@client.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("client-form"))
                .andExpect(model().attribute("isEdit", true))
                .andExpect(model().attribute("client", client));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteClient_ShouldRedirectToList() throws Exception {
        String email = "delete-me@test.com";

        mockMvc.perform(get("/clients/delete/" + email))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/clients"));

        verify(clientService).deleteClientByEmail(email);
    }
}
