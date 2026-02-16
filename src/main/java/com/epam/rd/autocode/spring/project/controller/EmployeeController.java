package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.EmployeeDTO;
import com.epam.rd.autocode.spring.project.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public String listEmployees(Model model) {
        model.addAttribute("employees", employeeService.getAllEmployees());
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
