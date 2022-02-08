package com.example.sweater.sweater.controller;

import com.example.sweater.sweater.model.Message;
import com.example.sweater.sweater.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Set;

@Controller
public class MessageController {

    public Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/user-messages/{user}")
    public String userMessages(
            @AuthenticationPrincipal User currentUser,
            @PathVariable(name = "user") User user,
            Model model) {

        Set<Message> messageSet = user.getMessageSet();
        model.addAttribute("messages", messageSet);
        model.addAttribute("userChannel", user);
        model.addAttribute("subscriptionsCount", user.getSubscriptions().size());
        model.addAttribute("subscribersCount", user.getSubscribers().size());
        model.addAttribute("currentUser", currentUser.equals(user));
        model.addAttribute("isSubscriber", user.getSubscriptions().contains(currentUser));
        logger.info("currentUser: " + currentUser);
        Message message = new Message();
        model.addAttribute("message", message);
        return "userMessages";
    }

}
