package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.dto.BookDTO;
import com.epam.rd.autocode.spring.project.exception.AlreadyExistException;
import com.epam.rd.autocode.spring.project.exception.NotFoundException;
import com.epam.rd.autocode.spring.project.model.Book;
import com.epam.rd.autocode.spring.project.repo.BookRepository;
import com.epam.rd.autocode.spring.project.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    public BookServiceImpl(BookRepository bookRepository, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Page<BookDTO> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable).map(book -> modelMapper.map(book, BookDTO.class));
    }

    @Override
    public BookDTO getBookByName(String name) {
        Book existingBook = bookRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Book not found"));
        return modelMapper.map(existingBook, BookDTO.class);
    }

    @Override
    public BookDTO updateBookByName(String name, BookDTO book) {
        Book existingBook = bookRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Book not found"));

        modelMapper.map(book, existingBook);

        Book updatedBook = bookRepository.save(existingBook);
        return modelMapper.map(updatedBook, BookDTO.class);
    }

    @Override
    public void deleteBookByName(String name) {
        bookRepository.deleteBookByName(name);
    }

    @Override
    public BookDTO addBook(BookDTO book) {
        if(bookRepository.findByName(book.getName()).isPresent()) {
            throw new AlreadyExistException("Book with this Title already exists");
        }
        bookRepository.save(modelMapper.map(book, Book.class));
        return book;
    }

    @Override
    public Page<BookDTO> searchBooks(String keyword, Pageable pageable) {
        return bookRepository.findByNameContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrGenreContainingIgnoreCase(keyword, keyword, keyword, pageable)
                .map(book -> modelMapper.map(book, BookDTO.class));
    }
}
