package org.example.web.controllers;

import org.apache.log4j.Logger;
import org.example.app.services.UserService;
import org.example.web.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/users")
public class UserController {

    private final Logger logger = Logger.getLogger(UserController.class);
    private UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;

    }

    @GetMapping("/")
    public String list(Model model) {
        logger.info("Printing user page");
        model.addAttribute("user", new User());
        model.addAttribute("userList", userService.getAllUsers());
        return "users_page";
    }
    @PostMapping("/save")
    public String save(User user) {
        userService.saveUser(user);
        return "redirect:/users/";
    }
}