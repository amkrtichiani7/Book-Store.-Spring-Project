package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.ClientDTO;
import com.epam.rd.autocode.spring.project.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clients")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public String listClients(Model model) {
        model.addAttribute("clients", clientService.getAllClients());
        return "client-list";
    }

    @GetMapping("/add")
    public String showAddClient(Model model) {
        model.addAttribute("client", new ClientDTO());
        model.addAttribute("isEdit", false);
        return "client-form";
    }

    @PostMapping("/add")
    public String addClient(@ModelAttribute("client") ClientDTO clientDTO,
                            BindingResult result,
                            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("isEdit", false);
            return "client-form";
        }
        clientService.addClient(clientDTO);
        return "redirect:/clients";
    }

    @GetMapping("/delete/{email}")
    public String deleteClient(@PathVariable String email) {
        clientService.deleteClientByEmail(email);
        return "redirect:/clients";
    }

    @GetMapping("/edit/{email}")
    public String showEditClientForm(@PathVariable String email, Model model) {
        ClientDTO client = clientService.getClientByEmail(email);
        model.addAttribute("client", client);
        model.addAttribute("isEdit", true);
        return "client-form";
    }

    @PostMapping("/edit/{email}")
    public String updateClient(@PathVariable String email, @Valid @ModelAttribute("client") ClientDTO clientDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("isEdit", true);
            return "client-form";
        }
        clientService.updateClientByEmail(email, clientDTO);
        return "redirect:/clients";
    }
}