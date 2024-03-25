package ru.cinema.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.cinema.model.Ticket;
import ru.cinema.model.User;
import ru.cinema.persistence.TicketDBStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ThreadSafe
@Service
public class TicketService {
    private final TicketDBStore store;

    public TicketService(TicketDBStore store, HallService hallService) {
        this.store = store;
    }

    /**
     * Метод добавляет билет.
     * @param ticket билет
     * @return добавленный билет.
     */
    public Optional<Ticket> add(Ticket ticket) {
        return store.add(ticket);
    }

    /**
     * Метод возвращает все купленные билеты.
     * @return Список купленных билетов.
     */
    public List<Ticket> findAll() {
        return new ArrayList<>(store.findAll());
    }

    /**
     * Метод возвращает все билеты купленные конкретным пользователем.
     * @param user пользователь.
     * @return список билетов.
     */
    public List<Ticket> findByUser(User user) {
        return store.findByUser(user);
    }
}
