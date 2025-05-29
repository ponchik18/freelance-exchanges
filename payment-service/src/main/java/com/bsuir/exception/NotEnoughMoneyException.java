package com.bsuir.exception;

public class NotEnoughMoneyException extends RuntimeException {
    public NotEnoughMoneyException() {
        super("Недостаточно средст для продолжения операции!");
    }
}