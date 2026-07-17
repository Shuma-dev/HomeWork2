package org.example;

import org.example.console.ConsoleMenu;
import org.example.dao.UserDaoImpl;
import org.example.service.UserService;
import org.example.service.UserServiceImpl;
import org.example.util.HibernateUtil;

public class Main {
    public static void main(String[] args) {
        UserDaoImpl userDao = new UserDaoImpl(HibernateUtil.getSessionFactory());
        UserService userService = new UserServiceImpl(userDao);
        ConsoleMenu consoleMenu = new ConsoleMenu(userService);

        consoleMenu.start();

        HibernateUtil.shutdown();
    }
}
