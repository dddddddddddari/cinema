package ru.cinema.control;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.cinema.model.Ticket;
import ru.cinema.model.User;
import ru.cinema.service.SessionService;
import ru.cinema.service.TicketService;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@ThreadSafe
@Controller
public class TicketController {

    private final SessionService sessionService;
    private final TicketService ticketService;

    public TicketController(SessionService sessionService, TicketService ticketService) {
        this.sessionService = sessionService;
        this.ticketService = ticketService;
    }

    @GetMapping("/seats/{sessionId}")
    public String seats(Model model, @PathVariable("sessionId") int id, HttpSession session) {
        model.addAttribute("seats", sessionService.findFreeSeats(id));
        model.addAttribute("sessionId", id);
        model.addAttribute("user", currentUser(session));
        return "seats";
    }

    @GetMapping("/confirmOrder/{sessionId}/{row}/{cell}")
    public String confirmOrder(Model model,
                                @PathVariable("sessionId") int id,
                                @PathVariable("row") int row,
                                @PathVariable("cell") int cell,
                                HttpSession session) {
        model.addAttribute("sessionId", id);
        model.addAttribute("row", row);
        model.addAttribute("cell", cell);
        model.addAttribute("movie", sessionService.findById(id));
        model.addAttribute("user", currentUser(session));
        return "confirmOrder";
    }

    @PostMapping("/addTicket")
    public String addTicket(@ModelAttribute Ticket ticket) {
        Optional<Ticket> addedTicket = ticketService.add(ticket);
        if (addedTicket.isEmpty()) {
            return "redirect:/index?fail=true";
        }
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
