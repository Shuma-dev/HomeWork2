package org.example.console;

import org.example.entity.User;
import org.example.service.UserService;
import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {
    private final Scanner sc = new Scanner(System.in);
    private final UserService userService;

    public ConsoleMenu(UserService userService) {
        this.userService = userService;
    }

    public void start() {
        boolean running = true;
        while (running) {
            printMenu();
            int num = Integer.parseInt(sc.nextLine());
            switch (num) {
                case 1:
                    createUser();
                    break;
                case 2:
                    findUser();
                    break;
                case 3:
                    allUsers();
                    break;
                case 4:
                    updateUser();
                    break;
                case 5:
                    deleteUser();
                    break;
                case 0:
                    System.out.println("До свидания!");
                    running = false;
                    break;
                default:
                    System.out.println("Неверный пункт меню");
            }
        }

    }

    private void createUser() {
        try {
            System.out.print("Введите имя: ");
            String name = sc.nextLine();
            System.out.print("Введите Email: ");
            String email = sc.nextLine();
            System.out.print("Введите возраст: ");
            Integer age = Integer.parseInt(sc.nextLine());
            userService.createUser(name, email, age);
            System.out.println("Пользователь успешно добавлен");
        } catch (NumberFormatException e) {
            System.out.println("Возраст должен быть числом");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void findUser() {
        try {
            System.out.print("Введите ID: ");
            Long id = Long.parseLong(sc.nextLine());
            User user = userService.findById(id);
            if (user != null) {
                System.out.println(user);
            } else {
                System.out.println("Пользователь не найден");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID должен быть числом");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void allUsers() {
        List<User> users = userService.findAll();
        if (!users.isEmpty()) {
            for (User user : users) {
                System.out.println(user);
            }
        } else {
            System.out.println("Пользователей нет");
        }
    }

    private void updateUser() {
        try {
            System.out.print("Введите ID: ");
            Long id = Long.parseLong(sc.nextLine());
            System.out.print("Введите новое имя: ");
            String name = sc.nextLine();
            System.out.print("Введите новый Email: ");
            String email = sc.nextLine();
            System.out.print("Введите новый возраст: ");
            Integer age = Integer.parseInt(sc.nextLine());
            userService.updateUser(id, name, email, age);
            System.out.println("Данные пользователя изменены");
        } catch (NumberFormatException e) {
            System.out.println("ID и возраст должны быть числами");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteUser() {
        try {
            System.out.print("Введите ID: ");
            Long id = Long.parseLong(sc.nextLine());
            userService.deleteUser(id);
            System.out.println("Пользователь успешно удален");
        } catch (NumberFormatException e) {
            System.out.println("ID должен быть числом");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void printMenu() {
        System.out.println("========= USER SERVICE =========\n" +
                "\n" +
                "1. Добавить пользователя\n" +
                "\n" +
                "2. Найти пользователя\n" +
                "\n" +
                "3. Показать всех\n" +
                "\n" +
                "4. Обновить\n" +
                "\n" +
                "5. Удалить\n" +
                "\n" +
                "0. Выход");
    }
}
