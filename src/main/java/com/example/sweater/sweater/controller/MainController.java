package com.example.sweater.sweater.controller;

import com.example.sweater.sweater.model.Message;
import com.example.sweater.sweater.model.User;
import com.example.sweater.sweater.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class MainController {

    @Value("${upload.path}")
    private String uploadPath;

    private final MessageRepository messageRepository;
    private Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    public MainController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @GetMapping("/")
    public String greeting(){
            return "greeting";
    }

    @GetMapping("/index")
    public String index(Model model, @AuthenticationPrincipal User user,
                        @RequestParam(required = false, defaultValue = "")
                        String filter) {

        List<Message> messages = messageRepository.findAll();
        Message message = new Message();
        model.addAttribute("message", message);
        model.addAttribute("user", user);

        if(filter != null && !filter.isEmpty()) {
            messages = messageRepository.findByText(filter);
        } else {
            messages = messageRepository.findAll();
        }

        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);
        return "index";
    }

    @PostMapping("/index")
    String add(@AuthenticationPrincipal User user,
               @Valid Message message, BindingResult bindingResult, Model model,
               @RequestParam("file") MultipartFile file) throws IOException {

        logger.debug("USER: " + user.getUsername() );
        message.setAuthor(user);
        logger.debug("Upload PATH: " + uploadPath);
        logger.debug("Original Filename : " + file.getOriginalFilename());

        if(bindingResult.hasErrors()){
            Map<String, String> errorMap = ControllerUtil.getErrors(bindingResult);
            model.mergeAttributes(errorMap);

            model.addAttribute("message", message);
            return "index";
        }
        else {
            if(file != null && !file.getOriginalFilename().isEmpty()) {
                File uploadDir = new File(uploadPath);
                logger.debug("uploadDir :" + uploadDir);
                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }
                String uuidFile = UUID.randomUUID().toString();
                String resultFileName = uuidFile + "#" + file.getOriginalFilename();
                logger.debug("transformTo: " + uploadPath + File.separator + resultFileName);
                file.transferTo(new File(uploadPath + File.separator + resultFileName));
                message.setFilename(resultFileName);
            }
        }

        model.addAttribute("message", null);
        messageRepository.save(message);
        return "redirect:/index";
    }
}

