package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.EmployeeDTO;
import com.epam.rd.autocode.spring.project.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;
    private static final String DEFAULT_PAGE = "0";
    private static final String DEFAULT_SIZE = "5";
    private static final String DEFAULT_SORT = "name";

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public String listEmployees(@RequestParam(name = "keyword", required = false) String keyword,
                                @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                @RequestParam(defaultValue = DEFAULT_SIZE) int size,
                                @RequestParam(defaultValue = DEFAULT_SORT) String sortBy,
                                Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        Page<EmployeeDTO> employeePage;
        if(keyword != null && !keyword.isEmpty()){
            employeePage = employeeService.searchEmployees(keyword,pageable);
        }else {
            employeePage = employeeService.getAllEmployees(pageable);
        }
        model.addAttribute("employees", employeePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", employeePage.getTotalPages());
        model.addAttribute("totalItems", employeePage.getTotalElements());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("keyword", keyword);
        return "employee-list";
    }
    @GetMapping("/delete/{email}")
    public String deleteEmployee(@PathVariable String email) {
        employeeService.deleteEmployeeByEmail(email);
        return "redirect:/employees";
    }

    @GetMapping("/edit/{email}")
    public String showEditEmployeeForm(@PathVariable String email, Model model) {
        EmployeeDTO employee = employeeService.getEmployeeByEmail(email);
        model.addAttribute("employee", employee);
        model.addAttribute("isEdit", true);
        return "employee-form";
    }

    @PostMapping("/edit/{email}")
    public String updateEmployee(@PathVariable String email, @Valid @ModelAttribute("employee") EmployeeDTO employeeDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("isEdit", true);
            return "employee-form";
        }
        employeeService.updateEmployeeByEmail(email, employeeDTO);
        return "redirect:/employees";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("employee", new EmployeeDTO());
        model.addAttribute("isEdit", false);
        return "employee-form";
    }

    @PostMapping("/add")
    public String addEmployee(@Valid @ModelAttribute("employee") EmployeeDTO employeeDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("isEdit", false);
            return "employee-form";
        }
        employeeService.addEmployee(employeeDTO);
        return "redirect:/employees";
    }
}
