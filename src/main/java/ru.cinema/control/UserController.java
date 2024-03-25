package ru.cinema.control;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.cinema.model.User;
import ru.cinema.service.SessionService;
import ru.cinema.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@ThreadSafe
@Controller
public class UserController {

    private final UserService userService;
    private final SessionService sessionService;

    public UserController(UserService userService, SessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @GetMapping("/registration")
    public String fromRegistration(Model model, HttpSession session) {
        model.addAttribute("user", currentUser(session));
        return "/registration";
    }

    @PostMapping("/registration")
    public String registration(Model model, @ModelAttribute User user) {
        Optional<User> regUser = userService.add(user);
        if (regUser.isEmpty()) {
            model.addAttribute("message", "Пользователь с таким именем уже существует!");
            return "redirect:/userRegistrationFail";
        }
        return "redirect:/userRegistrationSuccess";
    }

    @GetMapping("/loginPage")
    public String loginPage(Model model, @RequestParam(name = "fail", required = false) Boolean fail) {
        model.addAttribute("fail", fail != null);
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, HttpServletRequest req) {
        Optional<User> userDb = userService.findUserByEmailAndPwd(
                user.getEmail(), user.getPassword()
        );
        if (userDb.isEmpty()) {
            return "redirect:/loginPage?fail=true";
        }
        HttpSession session = req.getSession();
        session.setAttribute("user", userDb.get());
        return "redirect:/index";
    }

    @GetMapping("/userRegistrationFail")
    public String userRegistrationFail() {
        return "/userRegistrationFail";
    }

    @GetMapping("/userRegistrationSuccess")
    public String userRegistrationSuccess() {
        return "/userRegistrationSuccess";
    }

    @GetMapping("/profile")
    public String profile(Model model, HttpSession session) {
        User user = currentUser(session);
        model.addAttribute("user", user);
        model.addAttribute("tickets", userService.findUserTickets(user));
        model.addAttribute("sessions", sessionService.findAll());
        return "accInfo";
    }

    @GetMapping("/formEditProfile")
    public String formEditeProfile(Model model, HttpSession session) {
        User user = currentUser(session);
        model.addAttribute("user", user);
        return "updateUser";
    }

    @PostMapping("/editProfile")
    public String editProfile(@ModelAttribute User user, HttpServletRequest req) {
        userService.update(user);
        HttpSession session = req.getSession();
        session.setAttribute("user", user);
        return "redirect:/profile";
    }

    private User currentUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Гость");
        }
        return user;
    }
}
