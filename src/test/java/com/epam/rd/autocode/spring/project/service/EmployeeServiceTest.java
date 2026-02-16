package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.dto.EmployeeDTO;
import com.epam.rd.autocode.spring.project.model.Employee;
import com.epam.rd.autocode.spring.project.repo.EmployeeRepository;
import com.epam.rd.autocode.spring.project.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    void testDeleteEmployee_Success() {
        String email = "staff@store.com";
        Employee employee = new Employee();
        when(employeeRepository.findByEmail(email)).thenReturn(Optional.of(employee));

        employeeService.deleteEmployeeByEmail(email);

        verify(employeeRepository).delete(employee);
    }

    @Test
    void testFindAllEmployees() {
        List<Employee> employees = List.of(new Employee(), new Employee());
        when(employeeRepository.findAll()).thenReturn(employees);
        when(modelMapper.map(any(Employee.class), eq(EmployeeDTO.class))).thenReturn(new EmployeeDTO());

        List<EmployeeDTO> results = employeeService.getAllEmployees();

        assertEquals(2, results.size());
        verify(employeeRepository).findAll();
    }
}