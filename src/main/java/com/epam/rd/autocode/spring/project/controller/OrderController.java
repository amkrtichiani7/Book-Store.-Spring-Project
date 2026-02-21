package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.BookItemDTO;
import com.epam.rd.autocode.spring.project.dto.EmployeeDTO;
import com.epam.rd.autocode.spring.project.dto.OrderDTO;
import com.epam.rd.autocode.spring.project.exception.InsufficientFundsException;
import com.epam.rd.autocode.spring.project.service.BookService;
import com.epam.rd.autocode.spring.project.service.ClientService;
import com.epam.rd.autocode.spring.project.service.EmployeeService;
import com.epam.rd.autocode.spring.project.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final EmployeeService employeeService;
    private final ClientService clientService;
    private final BookService bookService;

    public OrderController(OrderService orderService, EmployeeService employeeService,
                           ClientService clientService, BookService bookService) {
        this.orderService = orderService;
        this.employeeService = employeeService;
        this.clientService = clientService;
        this.bookService = bookService;
    }

    @GetMapping("/new")
    public String showOrderForm(Model model) {
        OrderDTO order = new OrderDTO();

        List<BookItemDTO> items = new ArrayList<>();
        BookItemDTO initial = new BookItemDTO();
        initial.setBookName("");
        initial.setQuantity(1);
        items.add(initial);
        order.setBookItems(items);
        Pageable allItems = PageRequest.of(0, 100);

        model.addAttribute("order", order);
        model.addAttribute("employees", employeeService.getAllEmployees(allItems).getContent());
        model.addAttribute("clients", clientService.getAllClients(allItems).getContent());
        model.addAttribute("allBooks", bookService.getAllBooks(allItems).getContent());
        return "order-form";
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    public String listOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "orderDate") String sortBy,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());

        Page<OrderDTO> orderPage = orderService.getAllOrders(pageable);

        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("sortBy", sortBy);

        return "order-list";
    }

    @PostMapping("/new")
    public String placeOrder(@Valid @ModelAttribute("order") OrderDTO orderDTO,
                             BindingResult result, Principal principal, Model model, RedirectAttributes ra) {
        orderDTO.setClientEmail(principal.getName());

        if (result.hasErrors()) {
            Pageable allItems = PageRequest.of(0, 100);
            model.addAttribute("employees", employeeService.getAllEmployees(allItems).getContent());
            model.addAttribute("allBooks", bookService.getAllBooks(allItems).getContent());
            return "order-form";
        }

        try {
            OrderDTO savedOrder = orderService.addOrder(orderDTO);

            String selectedEmpEmail = orderDTO.getEmployeeEmail();
            EmployeeDTO assignedEmployee = null;

            try {
                if (selectedEmpEmail != null && !selectedEmpEmail.isBlank()) {
                    assignedEmployee = employeeService.getEmployeeByEmail(selectedEmpEmail);
                }
            } catch (Exception e) {
                System.err.println("Note: Could not fetch details for employee: " + selectedEmpEmail);
            }

            ra.addFlashAttribute("successMessage", "Order placed successfully!");
            ra.addFlashAttribute("totalPrice", savedOrder.getPrice());

            ra.addFlashAttribute("empEmail", assignedEmployee != null ? assignedEmployee.getEmail() : selectedEmpEmail);
            ra.addFlashAttribute("empPhone", assignedEmployee != null ? assignedEmployee.getPhone() : "Not Provided");

            return "redirect:/orders/order-success";

        } catch (InsufficientFundsException e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/orders/new";
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "System Error: " + e.getMessage());
            return "redirect:/orders/new";
        }
    }

    @GetMapping("/order-success")
    public String showSuccess(Model model) {
        if (!model.containsAttribute("successMessage")) {
            return "redirect:/orders/new";
        }
        if (!model.containsAttribute("totalPrice")) {
            model.addAttribute("totalPrice", new BigDecimal("0.00"));
        }
        return "order-success";
    }
}

