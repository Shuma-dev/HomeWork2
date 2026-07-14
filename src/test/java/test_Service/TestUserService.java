package test_Service;

import org.example.dao.UserDao;
import org.example.entity.User;
import org.example.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class TestUserService {
    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    public void testCreateUser() {
        userService.createUser("Denis", "denis@com", 26);
        Mockito.verify(userDao).save(any(User.class));
    }

    @Test
    public void testFindById() {
        User user = new User();
        Mockito.when(userDao.findById(1L)).thenReturn(user);
        User result = userService.findById(1L);
        assertEquals(user, result);
        Mockito.verify(userDao).findById(1L);
    }

    @Test
    public void testFindAll() {
        List<User> users = new ArrayList<>();
        users.add(new User("Denis", "denis@com", 26));
        users.add(new User("Ivan", "ivan@com", 30));
        Mockito.when(userDao.findAll()).thenReturn(users);
        List<User> result = userService.findAll();
        assertEquals(users, result);
        Mockito.verify(userDao).findAll();
    }

    @Test
    public void testUpdateUser() {
        User user = new User();
        Mockito.when(userDao.findById(1L)).thenReturn(user);
        userService.updateUser(1L, "Denis", "denis@com", 26);
        assertEquals("Denis", user.getName());
        assertEquals("denis@com", user.getEmail());
        assertEquals(26, user.getAge());
        Mockito.verify(userDao).findById(1L);
        Mockito.verify(userDao).update(user);
    }

    @Test
    public void testDeleteUser() {
        User user = new User();
        Mockito.when(userDao.findById(1L)).thenReturn(user);
        userService.deleteUser(1L);
        Mockito.verify(userDao).findById(1L);
        Mockito.verify(userDao).delete(1L);
    }

}
