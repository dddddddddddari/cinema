package ru.cinema.control;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.cinema.model.User;
import ru.cinema.service.SessionService;

import javax.servlet.http.HttpSession;

@ThreadSafe
@Controller
public class IndexController {

    private final SessionService sessionService;

    public IndexController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/index")
    public String index(Model model, HttpSession session,
                        @RequestParam(name = "fail", required = false) Boolean fail) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Гость");
        }
        model.addAttribute("user", user);
        model.addAttribute("sessions", sessionService.findAll());
        model.addAttribute("fail", fail != null);
        return "index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/loginPage";
    }
}
