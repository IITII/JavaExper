package webadv.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import webadv.example.model.User;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * @author IITII
 */
@Controller
public class HomeController implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("login");
        registry.addViewController("/home").setViewName("home");
    }

    @GetMapping("/")   //界面跳转
    public String login(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String check(@Validated User user, BindingResult bindingResult, Model model, HttpSession session) {
        if (bindingResult.hasFieldErrors()) {
            model.addAttribute(user);
            return "login";
        } else {
            session.setAttribute("Account", user.getAccount());
            //session.setAttribute("Password",user.getPassword());
            System.out.println(user.getAccount());
            return "redirect:home";
        }
    }

    @GetMapping("/home")   //界面跳转
    public String goHome(HttpSession session, Model model) {
        //   session.setAttribute("id", value);
        model.addAttribute("Account", session.getAttribute("Account"));
        return "home";
    }
}