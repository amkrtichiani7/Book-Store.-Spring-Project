package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.EmployeeDTO;
import com.epam.rd.autocode.spring.project.dto.OrderDTO;
import com.epam.rd.autocode.spring.project.service.ClientService;
import com.epam.rd.autocode.spring.project.service.EmployeeService;
import com.epam.rd.autocode.spring.project.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final ClientService clientService;
    private final OrderService orderService;
    private final EmployeeService employeeService; // Add this

    public ProfileController(ClientService clientService, OrderService orderService, EmployeeService employeeService) {
        this.clientService = clientService;
        this.orderService = orderService;
        this.employeeService = employeeService;
    }

    @GetMapping
    public String showProfile(Principal principal, Model model) {
        String email = principal.getName();

        Pageable recentOrders = PageRequest.of(0, 10, Sort.by("orderDate").descending());
        Page<OrderDTO> ordersPage = orderService.getOrdersByClient(email, recentOrders);

        Map<String, EmployeeDTO> employeeDetails = new HashMap<>();
        for (OrderDTO order : ordersPage.getContent()) {
            String empEmail = order.getEmployeeEmail();
            if (empEmail != null && !employeeDetails.containsKey(empEmail)) {
                try {
                    employeeDetails.put(empEmail, employeeService.getEmployeeByEmail(empEmail));
                } catch (Exception e) {
                }
            }
        }

        model.addAttribute("client", clientService.getClientByEmail(email));
        model.addAttribute("orders", ordersPage.getContent());
        model.addAttribute("employeeDetails", employeeDetails);
        return "personal-info";
    }
}
