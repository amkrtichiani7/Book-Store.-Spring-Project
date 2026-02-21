package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.dto.BookDTO;
import com.epam.rd.autocode.spring.project.dto.BookItemDTO;
import com.epam.rd.autocode.spring.project.dto.ClientDTO;
import com.epam.rd.autocode.spring.project.dto.OrderDTO;
import com.epam.rd.autocode.spring.project.model.Book;
import com.epam.rd.autocode.spring.project.model.Client;
import com.epam.rd.autocode.spring.project.model.Employee;
import com.epam.rd.autocode.spring.project.model.Order;
import com.epam.rd.autocode.spring.project.repo.BookRepository;
import com.epam.rd.autocode.spring.project.repo.ClientRepository;
import com.epam.rd.autocode.spring.project.repo.EmployeeRepository;
import com.epam.rd.autocode.spring.project.repo.OrderRepository;
import com.epam.rd.autocode.spring.project.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void testAddOrder_Success() {
        String clientEmail = "rich-client@test.com";
        String employeeEmail = "admin@test.com";
        String bookName = "The Great Gatsby";
        BigDecimal bookPrice = new BigDecimal("20.00");

        Client client = new Client();
        client.setEmail(clientEmail);
        client.setBalance(new BigDecimal("50.00"));

        Employee employee = new Employee();
        employee.setEmail(employeeEmail);

        Book bookEntity = new Book();
        bookEntity.setName(bookName);
        bookEntity.setPrice(bookPrice);

        BookDTO bookDto = new BookDTO();
        bookDto.setName(bookName);
        BookItemDTO itemDto = new BookItemDTO();
        itemDto.setBookName(bookDto.getName());
        itemDto.setQuantity(1);

        OrderDTO orderDto = new OrderDTO();
        orderDto.setClientEmail(clientEmail);
        orderDto.setEmployeeEmail(employeeEmail);
        orderDto.setPrice(bookPrice);
        orderDto.setBookItems(List.of(itemDto));

        when(clientRepository.findByEmail(clientEmail)).thenReturn(Optional.of(client));
        when(employeeRepository.findByEmail(employeeEmail)).thenReturn(Optional.of(employee));
        when(bookRepository.findByName(bookName)).thenReturn(Optional.of(bookEntity));

        when(orderRepository.save(any(Order.class))).thenReturn(new Order());

        lenient().when(modelMapper.map(any(OrderDTO.class), eq(Order.class))).thenReturn(new Order());
        lenient().when(modelMapper.map(any(Client.class), eq(ClientDTO.class))).thenReturn(new ClientDTO());
        lenient().when(modelMapper.map(any(Order.class), eq(OrderDTO.class))).thenReturn(new OrderDTO());

        orderService.addOrder(orderDto);

        assertTrue(new BigDecimal("30.00").compareTo(client.getBalance()) == 0, "Balance deduction failed");
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(clientService, times(1)).updateClientByEmail(eq(clientEmail), any(ClientDTO.class));
    }

    @Test
    void testAddOrder_InsufficientBalance() {
        String clientEmail = "low-money@test.com";
        Client client = new Client();
        client.setBalance(new BigDecimal("10.00"));

        OrderDTO orderDto = new OrderDTO();
        orderDto.setClientEmail(clientEmail);
        orderDto.setPrice(new BigDecimal("100.00"));

        when(clientRepository.findByEmail(clientEmail)).thenReturn(Optional.of(client));

        assertThrows(RuntimeException.class, () -> orderService.addOrder(orderDto));
        verify(orderRepository, never()).save(any());
    }
}