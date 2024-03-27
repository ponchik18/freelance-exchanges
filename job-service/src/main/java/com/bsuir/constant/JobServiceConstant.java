package com.bsuir.constant;

public interface JobServiceConstant {
    interface Validation {
        interface Message {
            String NOT_EMPTY = "Заполните поля!";
            String NOT_VALID_FILED = "Заполнение поля корректно";
        }
    }
    interface Error {
        String JOB_NOT_FOUND = "Работа с id '%d' не найден!";
        String SKILL_NOT_FOUND = "Навык с id '%s' не найден!";
        String PROPOSAL_NOT_FOUND = "Отклик с id '%d' не найден!";
        String JOB_HAS_ALREADY_CLOSED = "Работа с id '%d' уже закрыта!";
        String SKILL_FOR_FREELANCER_NOT_FOUND = "Навык с id '%d' для исполнителя c id '%s' не найден!";
    }
    interface DefaultValue {
        int PAGE = 0;
        int ELEMENT_PER_PAGE = 10;
    }
}