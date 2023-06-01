package org.example.web.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.Date;

@Controller
@RequestMapping(value = "/")
public class MainController {

    private final Logger logger = Logger.getLogger(MainController.class);

    @Autowired
    public MainController() {

    }
    @GetMapping
    public String printMainPage(Model model) {
        logger.info("Printing main page");
        model.addAttribute("mainForm", new MainController());
        model.addAttribute("date", new Date());
        return "main_page";
    }

}