package org.example.web.controllers;


import org.apache.log4j.Logger;
import org.example.app.services.BookService;
import org.example.web.dto.Book;
import org.example.web.dto.BookIdToRemove;
import org.example.web.dto.BookRegexToRemove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

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
        model = makeModel(model);
        return "book_shelf";
    }

    @PostMapping("/save")
    public String saveBook(@Valid Book book, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            logger.info("SaveBook receiving wrong data " + bindingResult);
            model = makeModel(model);
            return "book_shelf";
        }
        bookService.saveBook(book);
        logger.info("current repository size: " + bookService.getAllBooks().size());
        return "redirect:/books/shelf";

    }


    @PostMapping("/remove")
    public String removeBook(@Valid BookIdToRemove bookIdToRemove, BindingResult bindingResult, Model model) {
        logger.info("Controller remove started " + bindingResult.hasErrors());
        if (bindingResult.hasErrors()) {
            model = makeModel(model);
            return "book_shelf";
        } else {
            if (!bookService.removeBookById(bookIdToRemove.getId())) {
                model.addAttribute("errorMessage", "Wrong id or not found");
                model = makeModel(model);
                return "book_shelf";
            }
        }
        return "redirect:/books/shelf";
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        logger.info("starting upload");
        String name = file.getOriginalFilename();
        assert name != null;
        if (name.length() == 0) {
            model = makeModel(model);
            model.addAttribute("errorMessage", "wrong fileName");
            return "book_shelf";
        }
        byte[] bytes = file.getBytes();

        String rootPath = System.getProperty("catalina.home");
        try {
            File dir = new File(rootPath + File.separator + "external_uploads");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File serverFile = new File(dir.getAbsolutePath() + File.separator + name);
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
            stream.write(bytes);
            stream.close();
            logger.info("new file saved " + serverFile.getAbsolutePath());
        } catch (IOException ex) {
            model = makeModel(model);
            model.addAttribute("errorMessage", ex.getMessage());
            return "book_shelf";

        }
        return "redirect:/books/shelf";
    }

    @PostMapping("/removeByRegex")
    public String removeByRegexBook(@Valid BookRegexToRemove bookRegexToRemove, BindingResult bindingResult, Model model) {
        logger.info("remove by regex started field='" + bookRegexToRemove.getField() + "' regex='" + bookRegexToRemove.getRegEx() + "'");
        if (bindingResult.hasErrors()) {
            model = makeModel(model);
            return "book_shelf";
        }
        List<String> booksColumnsName = bookService.getBooksColumns();
        if (!booksColumnsName.contains(bookRegexToRemove.getField())) {
            model.addAttribute("errorMessage", "Wrong filed name");
            model = makeModel(model);
            return "book_shelf";

        }
        List<Book> listBooksToRemove = bookService.getRegexBooks(
                bookRegexToRemove.getField(),
                bookRegexToRemove.getRegEx());

        if (listBooksToRemove.size() == 0) {
            model.addAttribute("errorMessage", "Wrong regEx or data not found");
            model = makeModel(model);
            return "book_shelf";
        }
        listBooksToRemove.forEach(book -> {
            bookService.removeBookById(book.getId());
        });
        return "redirect:/books/shelf";
    }

    public Model makeModel(Model model) {
        if (isNull(model.getAttribute("book"))) {
            model.addAttribute("book", new Book());
        }
        if (isNull(model.getAttribute("bookIdToRemove"))) {
            model.addAttribute("bookIdToRemove", new BookIdToRemove());
        }
        if (isNull(model.getAttribute("bookList"))) {
            model.addAttribute("bookList", bookService.getAllBooks());
        }
        if (isNull(model.getAttribute("bookRegexToRemove"))) {
            model.addAttribute("bookRegexToRemove", new BookRegexToRemove());
        }
        return model;
    }
}
