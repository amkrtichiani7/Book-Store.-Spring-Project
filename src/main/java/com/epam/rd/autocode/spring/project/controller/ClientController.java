package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.ClientDTO;
import com.epam.rd.autocode.spring.project.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/clients")
public class ClientController {
    private final ClientService clientService;
    private static final String DEFAULT_PAGE = "0";
    private static final String DEFAULT_SIZE = "5";
    private static final String DEFAULT_SORT = "name";

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public String listClients(@RequestParam(name = "keyword", required = false) String keyword,
                              @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_SIZE) int size,
                              @RequestParam(defaultValue = DEFAULT_SORT) String sortBy,
                              Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        Page<ClientDTO> clientPage;
        if (keyword != null && !keyword.isEmpty()) {
            clientPage = clientService.searchClients(keyword, pageable);
        }else {
            clientPage = clientService.getAllClients(pageable);
        }
        model.addAttribute("clients", clientPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", clientPage.getTotalPages());
        model.addAttribute("totalItems", clientPage.getTotalElements());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("keyword", keyword);
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