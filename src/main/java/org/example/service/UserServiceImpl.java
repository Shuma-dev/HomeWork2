package org.example.service;

import org.example.dao.UserDao;
import org.example.entity.User;

import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Неверный Id");
        }
    }


    @Override
    public void createUser(String name, String email, Integer age) {
        UserValidator.validate(name, email, age);
        User user = new User(name, email, age);
        userDao.save(user);
    }

    @Override
    public User findById(Long id) {
        validateId(id);
        User user = userDao.findById(id);
        if (user == null) {
            throw new IllegalArgumentException("Пользователь не найден");
        }
        return user;
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public void updateUser(Long id, String name, String email, Integer age) {
        validateId(id);
        User user = userDao.findById(id);
        if (user == null) {
            throw new IllegalArgumentException("Пользователь не найден");
        }
        UserValidator.validate(name, email, age);
        user.setName(name);
        user.setEmail(email);
        user.setAge(age);
        userDao.update(user);
    }

    @Override
    public void deleteUser(Long id) {
        validateId(id);
        User user = userDao.findById(id);
        if (user == null) {
            throw new IllegalArgumentException("Пользователь не найден");
        }
        userDao.delete(id);
    }
}
