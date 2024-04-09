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

    public Optional<Ticket> add(Ticket ticket) {
        return store.add(ticket);
    }

    public List<Ticket> findAll() {
        return new ArrayList<>(store.findAll());
    }

    public List<Ticket> findByUser(User user) {
        return store.findByUser(user);
    }
}
