package ru.cinema.persistence;

import net.jcip.annotations.ThreadSafe;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Repository;
import ru.cinema.model.Ticket;
import ru.cinema.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ThreadSafe
@Repository
public class TicketDBStore {
    private static final Logger LOG = LoggerFactory.getLogger(SessionDBStore.class);

    private final BasicDataSource pool;

    public TicketDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public Optional<Ticket> add(Ticket ticket) {
        Optional<Ticket> result = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "INSERT INTO ticket(session_id, seat_row, seat_cell, user_id) VALUES (?, ?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setInt(1, ticket.getSessionId());
            ps.setInt(2, ticket.getRow());
            ps.setInt(3, ticket.getCell());
            ps.setInt(4, ticket.getUserId());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    ticket.setId(id.getInt(1));
                }
                result = Optional.of(ticket);
            }
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
        return result;
    }


    public List<Ticket> findAll() {
        List<Ticket> tickets = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM ticket")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    Ticket result = new Ticket(it.getInt("id"), it.getInt("session_id"),
                            it.getInt("seat_row"), it.getInt("seat_cell"), it.getInt("user_id"));
                    tickets.add(result);
                }
            }
        } catch (Exception e) {
            LOG.error("Exception",  e);
        }
        return tickets;
    }



    public List<Ticket> findByUser(User user) {
        List<Ticket> tickets = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM ticket WHERE user_id = ?")
        ) {
            ps.setInt(1, user.getId());
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    Ticket result = new Ticket(it.getInt("id"), it.getInt("session_id"),
                            it.getInt("seat_row"), it.getInt("seat_cell"), it.getInt("user_id"));
                    tickets.add(result);
                }
            }
        } catch (Exception e) {
            LOG.error("Exception",  e);
        }
        return tickets;
    }
}
