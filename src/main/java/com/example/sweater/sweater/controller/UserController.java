package com.example.sweater.sweater.controller;

import com.example.sweater.sweater.model.Role;
import com.example.sweater.sweater.model.User;
import com.example.sweater.sweater.repository.UserRepository;
import com.example.sweater.sweater.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

@Controller
public class UserController {

    public Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/user")
    public String userList(Model model) {
        model.addAttribute("users", userService.findAll());
        return "userList";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public String userEditor(@PathVariable("id") Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        logger.debug("Current ID:  " + user.getId());
        model.addAttribute("roles", Role.values());
        logger.debug("Exists roles: " + Arrays.toString(Role.values()));
        return "userEdit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "userEditS", method = RequestMethod.POST)
    public String editUsers(@RequestParam String username,
                            @RequestParam Map<String, String> form,
                            @RequestParam("userId") User user) {
        userService.saveUser(user, username, form);

        return "redirect:/user";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String getProfile(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("user", user);

        return "profile";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public String updateProfile(@AuthenticationPrincipal User user,
                                @RequestParam String password,
                                @RequestParam String email) {
        userService.updateProfile(user, password, email);

        return "redirect:/user";
    }

    @GetMapping("subscribes/{user}")
    String subscribe(@PathVariable User user,
                     @AuthenticationPrincipal User currentUser){
        userService.subscribe(currentUser, user);
        return "redirect:/user-messages/" + user.getId();
    }
    @GetMapping("unsubscribes/{user}")
    String unsubscribe(@PathVariable User user,
                     @AuthenticationPrincipal User currentUser){
        userService.unsubscribe(currentUser, user);
        return "redirect:/user-messages/" + user.getId();
    }
}
