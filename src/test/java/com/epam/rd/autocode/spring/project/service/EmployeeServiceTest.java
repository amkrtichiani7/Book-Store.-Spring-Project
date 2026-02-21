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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
    void testFindAllEmployees_Paginated() {
        Pageable pageable = PageRequest.of(0, 5);
        List<Employee> employeeList = List.of(new Employee(), new Employee());
        Page<Employee> employeePage = new PageImpl<>(employeeList, pageable, employeeList.size());

        when(employeeRepository.findAll(pageable)).thenReturn(employeePage);
        when(modelMapper.map(any(Employee.class), eq(EmployeeDTO.class))).thenReturn(new EmployeeDTO());

        Page<EmployeeDTO> resultPage = employeeService.getAllEmployees(pageable);

        assertEquals(2, resultPage.getContent().size());
        verify(employeeRepository).findAll(pageable);
    }
}