package ru.cinema.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.cinema.model.Ticket;
import ru.cinema.model.User;
import ru.cinema.persistence.UserDBStore;
import ru.cinema.service.SessionService;
import ru.cinema.service.TicketService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ThreadSafe
@Service
public class UserService {

    private final UserDBStore store;
    private final TicketService ticketService;
    private final SessionService sessionService;

    public UserService(UserDBStore store, TicketService ticketService, SessionService sessionService) {
        this.store = store;
        this.ticketService = ticketService;
        this.sessionService = sessionService;
    }

    /**
     * Метод добавляет пользователя.
     * @param user пользователь.
     * @return Добавленный пользователь.
     */
    public Optional<User> add(User user) {
        return store.add(user);
    }

    /**
     * Метод обновляет данные пользователя.
     * @param user пользователь.
     */
    public void update(User user) {
        store.update(user);
    }

    /**
     * Метод ищет и возвращает пользователя по
     * идентификационному номеру.
     * @param id идентификационный номер.
     * @return Пользователь.
     */
    public User findById(int id) {
        return store.findById(id);
    }

    /**
     * Метод ищет и возвращает пользователя по
     * почте и паролю.
     * @param email почта.
     * @param password пароль.
     * @return Пользователь.
     */
    public Optional<User> findUserByEmailAndPwd(String email, String password) {
        return store.findUserByNameAndPwd(email, password);
    }

    /**
     * Метод возвращает список билетов купленых пользователем.
     * @param user Пользователь.
     * @return Список купленных билетов.
     */
    public List<Ticket> findUserTickets(User user) {
        List<Ticket> tickets = new ArrayList<>(ticketService.findByUser(user));
        tickets.forEach(
                ticket -> ticket.setSession(
                        sessionService.findById(ticket.getSessionId())
                )
        );
        return tickets;
    }
}
