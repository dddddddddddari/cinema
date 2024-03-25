package ru.cinema.persistence;

import net.jcip.annotations.ThreadSafe;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.cinema.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;


@ThreadSafe
@Repository
public class UserDBStore {
    private static final Logger LOG = LoggerFactory.getLogger(SessionDBStore.class);

    private final BasicDataSource pool;

    public UserDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public Optional<User> add(User user) {
        Optional<User> result = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "INSERT INTO users(username, email, phone, password) VALUES (?, ?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getPassword());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    user.setId(id.getInt(1));
                }
            }
            result = Optional.of(user);
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
        return result;
    }


    public User findById(int id) {
        User result = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "SELECT * FROM users WHERE id = ?")
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    result = new User(it.getString("name"),
                            it.getString("email"), it.getString("phone"),
                            it.getString("password"));
                    result.setId(it.getInt("id"));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
        return result;
    }

    public Optional<User> findUserByNameAndPwd(String email, String password) {
        Optional<User> result = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "SELECT * FROM users WHERE email = ? and password = ?")
        ) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    result = Optional.of(new User(
                            it.getString("username"),
                            it.getString("email"),
                            it.getString("phone"),
                            it.getString("password")));
                    result.get().setId(it.getInt("id"));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
        return result;
    }


    public void update(User user) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("UPDATE users set username = ?, "
                     + "phone = ? where id = ?")
        ) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getPhone());
            ps.setInt(3, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Exception", e);
        }
    }
}
