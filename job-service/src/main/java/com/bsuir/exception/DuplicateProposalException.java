package com.bsuir.exception;

public class DuplicateProposalException extends RuntimeException {
    public DuplicateProposalException() {
        super("Ошибка! Отклик уже был оставлен!");
    }
}