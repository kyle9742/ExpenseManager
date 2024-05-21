package com.mysite.expense.controller;

import com.mysite.expense.dto.UserDTO;
import com.mysite.expense.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService uService;

    @GetMapping({"/login", "/"})
    public String showLoginPage(Principal principal) {
        if (principal == null) {
            return "login";
        }
        return "redirect:/expenses";
    }

    @GetMapping("/register")
    private String showRegisterPage(Model model) {
        model.addAttribute("user", new UserDTO());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") UserDTO user,
                           BindingResult result,
                           Model model) {
        System.out.println("유저DTO객체 : " + user);

        if (result.hasErrors()) {
            return "register";
        }
        uService.save(user);
        model.addAttribute("successMsg", true);
        return "response";
    }
}
