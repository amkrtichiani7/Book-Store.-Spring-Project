package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.BookDTO;
import com.epam.rd.autocode.spring.project.service.BookService;
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
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private static final String DEFAULT_PAGE = "0";
    private static final String DEFAULT_SIZE = "5";
    private static final String DEFAULT_SORT = "name";

    public  BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public String listBooks(@RequestParam(name = "keyword", required = false) String keyword,
                            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                            @RequestParam(defaultValue = DEFAULT_SIZE) int size,
                            @RequestParam(defaultValue = DEFAULT_SORT) String sortBy,
                            Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        Page<BookDTO> bookPage;
        if (keyword != null && !keyword.isEmpty()) {
            bookPage = bookService.searchBooks(keyword, pageable);
        } else {
            bookPage = bookService.getAllBooks(pageable);
        }
        model.addAttribute("books", bookPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bookPage.getTotalPages());
        model.addAttribute("totalItems", bookPage.getTotalElements());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("keyword", keyword);
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
