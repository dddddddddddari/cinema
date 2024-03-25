package ru.cinema.persistence;

import net.jcip.annotations.ThreadSafe;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Repository;
import ru.cinema.model.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@ThreadSafe
@Repository
public class SessionDBStore {
    private static final Logger LOG = LoggerFactory.getLogger(SessionDBStore.class);

    private final BasicDataSource pool;

    public SessionDBStore(BasicDataSource pool) {
        this.pool = pool;
    }


    public List<Session> findAll() {
        List<Session> sessions = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM sessions")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    Session result = new Session(it.getInt("id"), it.getString("name"));
                    sessions.add(result);
                }
            }
        } catch (Exception e) {
            LOG.error("Exception",  e);
        }
        return sessions;
    }


    public Session findById(int id) {
        Session result = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM sessions WHERE id = ?")
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    result = new Session(it.getInt("id"), it.getString("name"));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception",  e);
        }
        return result;
    }
}
