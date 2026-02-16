package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.BookDTO;
import com.epam.rd.autocode.spring.project.service.BookService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    public  BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public String listBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "book-list";
    }

    @GetMapping("/add")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new BookDTO());
        return "book-form";
    }

    @PostMapping("/add")
    public String addBook(@Valid @ModelAttribute("book") BookDTO bookDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "book-form";
        }
        bookService.addBook(bookDTO);
        return "redirect:/books";
    }

    @GetMapping("/delete/{name}")
    public String deleteBook(@PathVariable String name) {
        bookService.deleteBookByName(name);
        return "redirect:/books";
    }

    @GetMapping("/edit/{name}")
    public String showEditForm(@PathVariable String name, Model model) {
        BookDTO book = bookService.getBookByName(name);
        model.addAttribute("book", book);
        model.addAttribute("isEdit", true);
        return "book-form";
    }

    @PostMapping("/edit/{name}")
    public String updateBook(@PathVariable String name, @Valid @ModelAttribute("book") BookDTO bookDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("isEdit", true);
            return "book-form";
        }
        bookService.updateBookByName(name, bookDTO);
        return "redirect:/books";
    }
}
