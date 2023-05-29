package org.example.web.controllers;

import org.apache.log4j.Logger;
import org.example.app.services.BookService;
import org.example.web.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/books")
public class BookShelfController {

    private Logger logger = Logger.getLogger(BookShelfController.class);
    private BookService bookService;

    @Autowired
    public BookShelfController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/shelf")
    public String books(Model model) {
        logger.info("got book shelf");
        model.addAttribute("book", new Book());
        model.addAttribute("bookList", bookService.getAllBooks());
        return "book_shelf";
    }

    @PostMapping("/save")
    public String saveBook(Book book) {
        bookService.saveBook(book);
        logger.info("current repository size: " + bookService.getAllBooks().size());
        return "redirect:/books/shelf";
    }

    @PostMapping("/remove")
    public String removeBook(@RequestParam(value = "bookIdToRemove") Integer bookIdToRemove) {
        if (bookService.removeBookById(bookIdToRemove)) {
            return "redirect:/books/shelf";
        } else {
            return "redirect:/books/shelf";
        }
    }

    @PostMapping("/removeByRegex")
    public String removeByRegexBook(@RequestParam(value = "fieldRegex") String fieldRegex,
                                    @RequestParam(value = "queryRegex") String queryRegex) {
        logger.info("remove by regex started field='" + fieldRegex + "' regex='" + queryRegex + "'");
        List<Integer> listBooksToRemove = new ArrayList<>();
        for (Book book : bookService.getAllBooks()) {
            String fieldToSearch = book.getSize() == null ? "0" : book.getSize().toString();
            switch (fieldRegex.toLowerCase()) {
                case "author" -> fieldToSearch = book.getAuthor();
                case "title" -> fieldToSearch = book.getTitle();
            }
            if (fieldToSearch.matches(queryRegex)) {
                listBooksToRemove.add(book.getId());
            }
        }
        logger.info(bookService.getAllBooks());
        listBooksToRemove.forEach(bookService::removeBookById);
        return "redirect:/books/shelf";
    }
}
