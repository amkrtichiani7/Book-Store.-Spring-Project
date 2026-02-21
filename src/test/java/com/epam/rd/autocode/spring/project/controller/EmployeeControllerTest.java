package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.EmployeeDTO;
import com.epam.rd.autocode.spring.project.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EmployeeService employeeService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void listEmployees_WithSearchKeyword_ShouldCallSearchService() throws Exception {
        String keyword = "John";
        when(employeeService.searchEmployees(eq(keyword), any(Pageable.class))).thenReturn(Page.empty());

        mockMvc.perform(get("/employees").param("keyword", keyword))
                .andExpect(status().isOk())
                .andExpect(model().attribute("keyword", keyword));

        verify(employeeService).searchEmployees(eq(keyword), any(Pageable.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addEmployee_ValidData_ShouldRedirectToList() throws Exception {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setEmail("new@store.com");
        dto.setName("New Employee");
        dto.setPhone("1234567890");
        dto.setPassword("validPass123");
        dto.setBirthDate(LocalDate.of(1990, 1, 1));

        mockMvc.perform(post("/employees/add")
                        .with(csrf())
                        .flashAttr("employee", dto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employees"));

        verify(employeeService).addEmployee(any(EmployeeDTO.class));
    }
}
