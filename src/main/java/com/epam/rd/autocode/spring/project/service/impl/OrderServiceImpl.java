package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.dto.BookItemDTO;
import com.epam.rd.autocode.spring.project.dto.ClientDTO;
import com.epam.rd.autocode.spring.project.dto.OrderDTO;
import com.epam.rd.autocode.spring.project.exception.InsufficientFundsException;
import com.epam.rd.autocode.spring.project.exception.NotFoundException;
import com.epam.rd.autocode.spring.project.model.*;
import com.epam.rd.autocode.spring.project.repo.BookRepository;
import com.epam.rd.autocode.spring.project.repo.ClientRepository;
import com.epam.rd.autocode.spring.project.repo.EmployeeRepository;
import com.epam.rd.autocode.spring.project.repo.OrderRepository;
import com.epam.rd.autocode.spring.project.service.ClientService;
import com.epam.rd.autocode.spring.project.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final ClientService clientService;
    private final EmployeeRepository employeeRepository;
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    public OrderServiceImpl(OrderRepository orderRepository,ClientRepository clientRepository, ClientService clientService, EmployeeRepository employeeRepository, BookRepository bookRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.clientRepository = clientRepository;
        this.clientService = clientService;
        this.employeeRepository = employeeRepository;
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
    }
    @Override
    public Page<OrderDTO> getOrdersByClient(String clientEmail, Pageable pageable) {
        Client existingClient = clientRepository.findByEmail(clientEmail)
                .orElseThrow(() -> new NotFoundException("Client not found"));

        return orderRepository.findAllByClient(existingClient, pageable)
                .map(order -> modelMapper.map(order,OrderDTO.class));
    }

    @Override
    public Page<OrderDTO> getOrdersByEmployee(String employeeEmail, Pageable pageable) {
        Employee existingEmployee = employeeRepository.findByEmail(employeeEmail)
                .orElseThrow(() -> new NotFoundException("Orders not found"));

        return orderRepository.findAllByEmployee(existingEmployee, pageable)
                .map(order -> modelMapper.map(order,OrderDTO.class));
    }

    @Transactional
    public OrderDTO addOrder(OrderDTO orderDTO) {
        Client client = clientRepository.findByEmail(orderDTO.getClientEmail())
                .orElseThrow(() -> new NotFoundException("Client not found"));

        Employee employee = employeeRepository.findByEmail(orderDTO.getEmployeeEmail())
                .orElseThrow(() -> new NotFoundException("Employee not found"));

        Order order = new Order();
        order.setClient(client);
        order.setEmployee(employee);
        order.setOrderDate(LocalDateTime.now());

        BigDecimal total = BigDecimal.ZERO;
        List<BookItem> bookItems = new ArrayList<>();

        for (BookItemDTO itemDTO : orderDTO.getBookItems()) {
            Book book = bookRepository.findByName(itemDTO.getBookName())
                    .orElseThrow(() -> new NotFoundException("Book not found: " + itemDTO.getBookName()));

            BigDecimal itemTotal = book.getPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity()));
            total = total.add(itemTotal);
            BookItem bookItem = new BookItem();
            bookItem.setBook(book);
            bookItem.setQuantity(itemDTO.getQuantity());
            bookItem.setOrder(order);
            bookItems.add(bookItem);
        }

        if (client.getBalance().compareTo(total) < 0) {
            throw new InsufficientFundsException("Insufficient funds! Required: $" + total + ", Available: $" + client.getBalance());
        }else{
            client.setBalance(client.getBalance().subtract(total));
            clientService.updateClientByEmail(client.getEmail(),modelMapper.map(client, ClientDTO.class));
        }

        order.setPrice(total);
        order.setBookItems(bookItems);

        Order savedOrder = orderRepository.save(order);
        return modelMapper.map(savedOrder, OrderDTO.class);
    }

    @Override
    public Page<OrderDTO> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(order -> modelMapper.map(order, OrderDTO.class));
    }
}
