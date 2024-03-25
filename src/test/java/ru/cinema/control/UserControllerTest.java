package ru.cinema.control;

import org.junit.Test;
import org.springframework.ui.Model;
import ru.cinema.model.User;
import ru.cinema.service.SessionService;
import ru.cinema.service.UserService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Test
    public void whenRegistration() {
        User user = new User("user", "email", "phone", "password");
        Model model = mock(Model.class);
        UserService userService = mock(UserService.class);
        SessionService sessionService =  mock(SessionService.class);
        UserController userController = new UserController(userService, sessionService);
        userController.registration(model, user);
        verify(userService).add(user);
    }

    @Test
    public void whenRegistrationFail() {
        User user = new User("user", "email", "phone", "password");
        Model model = mock(Model.class);
        UserService userService = mock(UserService.class);
        SessionService sessionService =  mock(SessionService.class);
        UserController userController = new UserController(userService, sessionService);
        String page = userController.registration(model, user);
        verify(userService).add(user);
        assertThat(page, is("redirect:/userRegistrationFail"));
    }

}