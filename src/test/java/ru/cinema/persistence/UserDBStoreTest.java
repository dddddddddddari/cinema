package ru.cinema.persistence;

import org.junit.Before;
import org.junit.Test;
import ru.cinema.Main;
import ru.cinema.model.User;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

public class UserDBStoreTest {

    @Before
    public void wipeTableTicket() throws SQLException {
        try (PreparedStatement statement = new Main().loadPool().getConnection()
                .prepareStatement("DELETE FROM ticket")) {
            statement.execute();
        }
    }

    @Before
    public void wipeTableUsers() throws SQLException {
        try (PreparedStatement statement = new Main().loadPool().getConnection()
                .prepareStatement("DELETE FROM users")) {
            statement.execute();
        }
    }

    @Test
    public void whenRegistrationSuccess() {
        UserDBStore store = new UserDBStore(new Main().loadPool());
        User user = new User("user", "email", "phone", "password");
        assertThat(store.add(user).orElse(null), is(user));
    }

    @Test
    public void whenRegistrationFail() {
        UserDBStore store = new UserDBStore(new Main().loadPool());
        User user = new User("user1", "email1", "phone1", "password1");
        store.add(user);
        assertTrue(store.add(user).isEmpty());
    }

    @Test
    public void whenLoginSuccess() {
        UserDBStore store = new UserDBStore(new Main().loadPool());
        User user = new User("user2", "email2", "phone2", "password2");
        store.add(user);
        Optional<User> actual = store.findUserByNameAndPwd(user.getEmail(), user.getPassword());
        assertThat(actual.orElse(null), is(user));
    }

    @Test
    public void whenLoginFail() {
        UserDBStore store = new UserDBStore(new Main().loadPool());
        User user = new User("user3", "email3", "phone3", "password3");
        store.add(user);
        Optional<User> actual = store.findUserByNameAndPwd("email4", "password4");
        assertTrue(actual.isEmpty());
    }
}