package org.example.service;

import org.example.dao.UserDao;
import org.example.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    // =====================
    // createUser()
    // =====================

    @Test
    @DisplayName("успешное создание")
    public void createUser_ShouldSaveUser() {
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        userService.createUser("Denis", "denis@com", 26);
        verify(userDao).save(captor.capture());
        User savedUser = captor.getValue();
        assertEquals("Denis", savedUser.getName());
        assertEquals("denis@com", savedUser.getEmail());
        assertEquals(26, savedUser.getAge());
    }

    @Test
    @DisplayName("если имя null")
    public void createUser_ShouldThrowException_WhenNameIsNull() {
        assertThrows(IllegalArgumentException.class, () -> userService
                .createUser(null, "denis@com", 26));
        verify(userDao, never()).save(any(User.class));
    }

    @Test
    @DisplayName("если имя пустое")
    public void createUser_ShouldThrowException_WhenNameIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> userService
                .createUser(" ", "denis@com", 26));
        verify(userDao, never()).save(any(User.class));
    }

    @Test
    @DisplayName("если email null")
    public void createUser_ShouldThrowException_WhenEmailIsNull() {
        assertThrows(IllegalArgumentException.class, () -> userService
                .createUser("Denis", null, 26));
        verify(userDao, never()).save(any(User.class));
    }

    @Test
    @DisplayName("если email пустой")
    public void createUser_ShouldThrowException_WhenEmailIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> userService
                .createUser("Denis", " ", 26));
        verify(userDao, never()).save(any(User.class));
    }

    @Test
    @DisplayName("если email не содержит '@'")
    public void createUser_ShouldThrowException_WhenEmailHasInvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> userService
                .createUser("Denis", "denis.com", 26));
        verify(userDao, never()).save(any(User.class));
    }

    @Test
    @DisplayName("если возраст null")
    public void createUser_ShouldThrowException_WhenAgeIsNull() {
        assertThrows(IllegalArgumentException.class, () -> userService
                .createUser("Denis", "denis@com", null));
        verify(userDao, never()).save(any(User.class));
    }

    @Test
    @DisplayName("если возраст = 0")
    public void createUser_ShouldThrowException_WhenAgeIsZero() {
        assertThrows(IllegalArgumentException.class, () -> userService
                .createUser("Denis", "denis@com", 0));
        verify(userDao, never()).save(any(User.class));
    }

    @Test
    @DisplayName("если возраст < 0")
    public void createUser_ShouldThrowException_WhenAgeIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> userService
                .createUser("Denis", "denis@com", -1));
        verify(userDao, never()).save(any(User.class));
    }

    @Test
    @DisplayName("если возраст > 110")
    public void createUser_ShouldThrowException_WhenAgeIsGreaterThan110() {
        assertThrows(IllegalArgumentException.class, () -> userService
                .createUser("Denis", "denis@com", 111));
        verify(userDao, never()).save(any(User.class));
    }

    // =====================
    // findById()
    // =====================

    @Test
    @DisplayName("успешный поиск")
    public void findByID_ShouldReturnUser() {
        User user = new User();
        when(userDao.findById(1L)).thenReturn(user);
        User result = userService.findById(1L);
        assertEquals(user, result);
        verify(userDao).findById(1L);
    }

    @Test
    @DisplayName("если пользователь не найден")
    public void findByID_ShouldThrowException_WhenUserNotFound() {
        when(userDao.findById(1L)).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> userService
                .findById(1L));
        verify(userDao).findById(1L);
    }

    @Test
    @DisplayName("если ID null")
    public void findByID_ShouldThrowException_WhenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> userService
                .findById(null));
        verify(userDao, never()).findById(any(Long.class));
    }

    @Test
    @DisplayName("если ID = 0")
    public void findByID_ShouldThrowException_WhenIdIsZero() {
        assertThrows(IllegalArgumentException.class, () -> userService
                .findById(0L));
        verify(userDao, never()).findById(any(Long.class));
    }

    @Test
    @DisplayName("если ID < 0")
    public void findByID_ShouldThrowException_WhenIdIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> userService
                .findById(-1L));
        verify(userDao, never()).findById(any(Long.class));
    }

    // =====================
    // findAll()
    // =====================

    @Test
    @DisplayName("успешный список")
    public void findAll_ShouldReturnUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User("Denis", "denis@com", 26));
        users.add(new User("Ivan", "ivan@com", 30));
        when(userDao.findAll()).thenReturn(users);
        List<User> result = userService.findAll();
        assertEquals(users, result);
        verify(userDao).findAll();
    }

    @Test
    @DisplayName("Если список пустой")
    public void findAll_ShouldReturnEmptyList() {
        when(userDao.findAll()).thenReturn(Collections.emptyList());
        List<User> result = userService.findAll();
        assertTrue(result.isEmpty());
        verify(userDao).findAll();
    }

    // =====================
    // updateUser()
    // =====================

    @Test
    @DisplayName("успешное обновление")
    public void updateUser_ShouldUpdateUser() {
        User user = new User();
        when(userDao.findById(1L)).thenReturn(user);
        userService.updateUser(1L, "Denis", "denis@com", 26);
        assertEquals("Denis", user.getName());
        assertEquals("denis@com", user.getEmail());
        assertEquals(26, user.getAge());
        verify(userDao).findById(1L);
        verify(userDao).update(user);
    }

    @Test
    @DisplayName("если ID null")
    public void updateUser_ShouldThrowException_WhenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> userService
                .updateUser(null, "Denis", "denis@com", 26));
        verify(userDao, never()).findById(any(Long.class));
        verify(userDao, never()).update(any(User.class));
    }

    @Test
    @DisplayName("если ID = 0")
    public void updateUser_ShouldThrowException_WhenIdIsZero() {
        assertThrows(IllegalArgumentException.class, () -> userService
                .updateUser(0L, "Denis", "denis@com", 26));
        verify(userDao, never()).findById(any(Long.class));
        verify(userDao, never()).update(any(User.class));
    }

    @Test
    @DisplayName("если ID < 0")
    public void updateUser_ShouldThrowException_WhenIdIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> userService
                .updateUser(-1L, "Denis", "denis@com", 26));
        verify(userDao, never()).findById(any(Long.class));
        verify(userDao, never()).update(any(User.class));
    }

    @Test
    @DisplayName("если пользователь не найден")
    public void updateUser_ShouldThrowException_WhenUserNotFound() {
        when(userDao.findById(1L)).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> userService
                .updateUser(1L, "Denis", "denis@com", 26));
        verify(userDao).findById(1L);
        verify(userDao, never()).update(any(User.class));
    }

    @Test
    @DisplayName("если имя null")
    public void updateUser_ShouldThrowException_WhenNameIsNull() {
        when(userDao.findById(1L)).thenReturn(new User());
        assertThrows(IllegalArgumentException.class, () -> userService
                .updateUser(1L, null, "denis.com", 26));
        verify(userDao).findById(1L);
        verify(userDao, never()).update(any(User.class));
    }

    @Test
    @DisplayName("если имя пустое")
    public void updateUser_ShouldThrowException_WhenNameIsBlank() {
        when(userDao.findById(1L)).thenReturn(new User());
        assertThrows(IllegalArgumentException.class, () -> userService
                .updateUser(1L, " ", "denis.com", 26));
        verify(userDao).findById(1L);
        verify(userDao, never()).update(any(User.class));
    }

    @Test
    @DisplayName("если email null")
    public void updateUser_ShouldThrowException_WhenEmailIsNull() {
        when(userDao.findById(1L)).thenReturn(new User());
        assertThrows(IllegalArgumentException.class, () -> userService
                .updateUser(1L, "Denis", null, 26));
        verify(userDao).findById(1L);
        verify(userDao, never()).update(any(User.class));
    }

    @Test
    @DisplayName("если email пустой")
    public void updateUser_ShouldThrowException_WhenEmailIsBlank() {
        when(userDao.findById(1L)).thenReturn(new User());
        assertThrows(IllegalArgumentException.class, () -> userService
                .updateUser(1L, "Denis", " ", 26));
        verify(userDao).findById(1L);
        verify(userDao, never()).update(any(User.class));
    }

    @Test
    @DisplayName("если email не содержит '@'")
    public void updateUser_ShouldThrowException_WhenEmailHasInvalidFormat() {
        when(userDao.findById(1L)).thenReturn(new User());
        assertThrows(IllegalArgumentException.class, () -> userService
                .updateUser(1L, "Denis", "denis.com", 26));
        verify(userDao).findById(1L);
        verify(userDao, never()).update(any(User.class));
    }

    @Test
    @DisplayName("если возраст null")
    public void updateUser_ShouldThrowException_WhenAgeIsNull() {
        when(userDao.findById(1L)).thenReturn(new User());
        assertThrows(IllegalArgumentException.class, () -> userService
                .updateUser(1L, "Denis", "denis@com", null));
        verify(userDao).findById(1L);
        verify(userDao, never()).update(any(User.class));
    }

    @Test
    @DisplayName("если возраст = 0")
    public void updateUser_ShouldThrowException_WhenAgeIsZero() {
        when(userDao.findById(1L)).thenReturn(new User());
        assertThrows(IllegalArgumentException.class, () -> userService
                .updateUser(1L, "Denis", "denis@com", 0));
        verify(userDao).findById(1L);
        verify(userDao, never()).update(any(User.class));
    }

    @Test
    @DisplayName("если возраст < 0")
    public void updateUser_ShouldThrowException_WhenAgeIsNegative() {
        when(userDao.findById(1L)).thenReturn(new User());
        assertThrows(IllegalArgumentException.class, () -> userService
                .updateUser(1L, "Denis", "denis@com", -1));
        verify(userDao).findById(1L);
        verify(userDao, never()).update(any(User.class));
    }

    @Test
    @DisplayName("если возраст > 110")
    public void updateUser_ShouldThrowException_WhenAgeIsGreaterThan110() {
        when(userDao.findById(1L)).thenReturn(new User());
        assertThrows(IllegalArgumentException.class, () -> userService
                .updateUser(1L, "Denis", "denis@com", 111));
        verify(userDao).findById(1L);
        verify(userDao, never()).update(any(User.class));
    }

    // =====================
    // deleteUser()
    // =====================

    @Test
    @DisplayName("успешно удален")
    public void deleteUser_ShouldDeleteUser() {
        User user = new User();
        when(userDao.findById(1L)).thenReturn(user);
        userService.deleteUser(1L);
        verify(userDao).findById(1L);
        verify(userDao).delete(1L);
    }

    @Test
    @DisplayName("если ID null")
    public void deleteUser_ShouldThrowException_WhenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> userService
                .deleteUser(null));
        verify(userDao, never()).findById(any(Long.class));
        verify(userDao, never()).delete(any(Long.class));
    }

    @Test
    @DisplayName("если ID = 0")
    public void deleteUser_ShouldThrowException_WhenIdIsZero() {
        assertThrows(IllegalArgumentException.class, () -> userService
                .deleteUser(0L));
        verify(userDao, never()).findById(any(Long.class));
        verify(userDao, never()).delete(any(Long.class));
    }

    @Test
    @DisplayName("если ID < 0")
    public void deleteUser_ShouldThrowException_WhenIdIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> userService
                .deleteUser(-1L));
        verify(userDao, never()).findById(any(Long.class));
        verify(userDao, never()).delete(any(Long.class));
    }

    @Test
    @DisplayName("если пользователь не найден")
    public void deleteUser_ShouldThrowException_WhenUserNotFound() {
        when(userDao.findById(1L)).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> userService
                .deleteUser(1L));
        verify(userDao).findById(1L);
        verify(userDao, never()).delete(any(Long.class));
    }
}
