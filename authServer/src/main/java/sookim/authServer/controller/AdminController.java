package sookim.authServer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sookim.authServer.domain.User;
import sookim.authServer.service.UserService;

import java.util.List;

@RequestMapping("/admin")
@RequiredArgsConstructor
@Controller
public class AdminController {
    private final UserService userService;

    @GetMapping("")
    public String getAdmin(Model model){
        try {
            List<User> userList = userService.getUserList();
            model.addAttribute("userList", userList);
        }catch(Exception e){

        }
        return "admin";
    }
}
