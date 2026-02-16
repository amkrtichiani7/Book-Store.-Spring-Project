package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.BookDTO;
import com.epam.rd.autocode.spring.project.dto.BookItemDTO;
import com.epam.rd.autocode.spring.project.dto.OrderDTO;
import com.epam.rd.autocode.spring.project.exception.InsufficientFundsException;
import com.epam.rd.autocode.spring.project.service.BookService;
import com.epam.rd.autocode.spring.project.service.ClientService;
import com.epam.rd.autocode.spring.project.service.EmployeeService;
import com.epam.rd.autocode.spring.project.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

        // Ensure bookItems list contains one item with nested BookDTO
        List<BookItemDTO> items = new ArrayList<>();
        BookItemDTO initial = new BookItemDTO();
        initial.setBook(new BookDTO());          // <<< important so form binding to book.name works
        initial.setQuantity(1);
        items.add(initial);
        order.setBookItems(items);

        model.addAttribute("order", order);
        model.addAttribute("employees", employeeService.getAllEmployees());
        model.addAttribute("clients", clientService.getAllClients());
        model.addAttribute("allBooks", bookService.getAllBooks());
        return "order-form";
    }

    @PostMapping("/new")
    public String placeOrder(@Valid @ModelAttribute("order") OrderDTO orderDTO,
                             BindingResult result, Principal principal, Model model, RedirectAttributes ra) {

        System.out.println("DEBUG: HIT THE CONTROLLER!");

        orderDTO.setClientEmail(principal.getName());
        if (orderDTO.getEmployeeEmail() == null || orderDTO.getEmployeeEmail().isBlank()) {
            orderDTO.setEmployeeEmail("admin@test.com");
        }

        if (result.hasErrors()) {
            result.getFieldErrors().forEach(f -> System.out.println(f.getField() + ": " + f.getDefaultMessage()));

            model.addAttribute("allBooks", bookService.getAllBooks());
            return "order-form";
        }

        try {
            OrderDTO savedOrder = orderService.addOrder(orderDTO);

            ra.addFlashAttribute("successMessage", "Order placed successfully!");
            ra.addFlashAttribute("totalPrice", savedOrder.getPrice());

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
        // If user opens success page directly, there's no flash → redirect to form
        if (!model.containsAttribute("successMessage")) {
            return "redirect:/orders/new";
        }
        // totalPrice should be in flash; if not, set to 0.00
        if (!model.containsAttribute("totalPrice")) {
            model.addAttribute("totalPrice", new BigDecimal("0.00"));
        }
        return "order-success";
    }
}

