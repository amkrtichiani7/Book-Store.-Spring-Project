package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.BookItemDTO;
import com.epam.rd.autocode.spring.project.dto.OrderDTO;
import com.epam.rd.autocode.spring.project.service.BookService;
import com.epam.rd.autocode.spring.project.service.ClientService;
import com.epam.rd.autocode.spring.project.service.EmployeeService;
import com.epam.rd.autocode.spring.project.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OrderService orderService;
    @MockBean private EmployeeService employeeService;
    @MockBean private ClientService clientService;
    @MockBean private BookService bookService;

    @Test
    @WithMockUser(username = "client@test.com", roles = "CLIENT")
    void placeOrder_Success_RedirectsToSuccessPage() throws Exception {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setClientEmail("client@test.com");
        orderDTO.setEmployeeEmail("admin@test.com");

        BookItemDTO item = new BookItemDTO();
        item.setBookName("Java Basics");
        item.setQuantity(1);
        orderDTO.setBookItems(List.of(item));

        OrderDTO savedOrder = new OrderDTO();
        savedOrder.setPrice(new BigDecimal("50.00"));
        when(orderService.addOrder(any(OrderDTO.class))).thenReturn(savedOrder);

        mockMvc.perform(post("/orders/new")
                        .with(csrf())
                        .flashAttr("order", orderDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders/order-success"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void listOrders_ShouldShowPaginatedView() throws Exception {
        when(orderService.getAllOrders(any(Pageable.class))).thenReturn(Page.empty());

        mockMvc.perform(get("/orders")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("order-list"))
                .andExpect(model().attributeExists("orders"));
    }
}
