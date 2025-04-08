package com.project.marketplace.exceptions;

public class EmailAlreadyExistsException extends IllegalArgumentException {
    public EmailAlreadyExistsException(String email) {
        super("Пользователь с email " + email + " уже существует");
    }
}