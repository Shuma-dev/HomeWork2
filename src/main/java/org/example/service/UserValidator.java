package org.example.service;

public class UserValidator {
    public static void validate(String name, String email, Integer age) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя не может быть пустым");
        }
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new IllegalArgumentException("Неверный email");
        }
        if (age == null || age <= 0 || age > 110) {
            throw new IllegalArgumentException("Неверный возраст");
        }

    }
}
