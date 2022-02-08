package com.example.sweater.sweater.controller;

import com.example.sweater.sweater.model.User;
import com.example.sweater.sweater.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class RegistrationController {

    Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registration(Model model, User user) {
        model.addAttribute("user", user);
        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String addUser(@RequestParam ("password2") String passwordConfirm,
            @Valid User user,
            BindingResult bindingResult,
            Model model) {

        boolean isConfirm = StringUtils.isEmpty(passwordConfirm);

        if(isConfirm) {
            model.addAttribute("passwordError2", "Confirmed Password cannot be Empty");
        }

        if(user.getPassword() != null && !user.getPassword().equals(passwordConfirm) ){
            model.addAttribute("passwordError", "Passwords are not equal");
        }

        if(isConfirm || bindingResult.hasErrors()) {
            Map<String, String> errors = ControllerUtil.getErrors(bindingResult);
            model.mergeAttributes(errors);
            return "registration";
        }

        if(!userService.addUser(user)) {
            model.addAttribute("registrationMessage", "User exists");
            return "registration";
        }
        logger.debug("POST MAPPING USER END WORK" + user.getUsername() + user.getPassword());
        return "redirect:/login";
    }

    @RequestMapping(value = "/activate/{code}", method = RequestMethod.GET)
    public String activate(Model model, @PathVariable("code") String code) {
        boolean isActivated = userService.activateUser(code);

        if(isActivated) {
            model.addAttribute("messageType", "-success");
            model.addAttribute("loginMessage", "User successfully activated");
        } else {
            model.addAttribute("messageType", "-danger");
            model.addAttribute("loginMessage", "Activation code is not found");
        }

        return "login";
    }
}
