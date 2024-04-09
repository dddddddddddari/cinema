package ru.cinema.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.cinema.model.Seat;
import ru.cinema.model.Session;
import ru.cinema.model.Ticket;
import ru.cinema.persistence.SessionDBStore;

import java.util.ArrayList;
import java.util.List;

@ThreadSafe
@Service
public class SessionService {
    private final SessionDBStore store;
    private final HallService hallService;
    private final TicketService ticketService;

    public SessionService(SessionDBStore store, HallService hallService, TicketService ticketService) {
        this.store = store;
        this.hallService = hallService;
        this.ticketService = ticketService;
    }

    public List<Session> findAll() {
        return new ArrayList<>(store.findAll());
    }

    public Session findById(int id) {
        return store.findById(id);
    }

    public List<Seat> findFreeSeats(int id) {
        List<Ticket> tickets = ticketService.findAll();
        List<Seat> seats = hallService.findById(id);
        List<Seat> reservedSeats = new ArrayList<>();
        for (Seat seat: seats) {
            for (Ticket ticket: tickets) {
                if (seat.getRow() == ticket.getRow()
                        && seat.getCell() == ticket.getCell()
                        && id == ticket.getSessionId()) {
                    reservedSeats.add(seat);
                }
            }
        }
        reservedSeats.forEach(seats::remove);
        return seats;
    }
}
