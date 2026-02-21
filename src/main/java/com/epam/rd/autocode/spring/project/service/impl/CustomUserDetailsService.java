package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.model.Client;
import com.epam.rd.autocode.spring.project.model.Employee;
import com.epam.rd.autocode.spring.project.repo.ClientRepository;
import com.epam.rd.autocode.spring.project.repo.EmployeeRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepo;
    private final ClientRepository clientRepo;

    public CustomUserDetailsService(EmployeeRepository employeeRepo, ClientRepository clientRepo) {
        this.employeeRepo = employeeRepo;
        this.clientRepo = clientRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Employee> employee = employeeRepo.findByEmail(email);
        if (employee.isPresent()) {
            Employee e = employee.get();
            String role = (e.getEmail().equals("admin@test.com"))
                    ? "ADMIN" : "EMPLOYEE";

            return User.builder()
                    .username(e.getEmail())
                    .password(e.getPassword())
                    .roles(role)
                    .build();
        }

        Optional<Client> client = clientRepo.findByEmail(email);
        if (client.isPresent()) {
            Client c = client.get();
            return User.builder()
                    .username(c.getEmail())
                    .password(c.getPassword())
                    .roles("CLIENT")
                    .build();
        }

        throw new UsernameNotFoundException("User not found: " + email);
    }
}