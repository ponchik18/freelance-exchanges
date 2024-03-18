package com.bsuir.constant;

public interface JobServiceConstant {
    interface Validation {
        interface Message {
            String NOT_EMPTY = "Заполните поля!";
            String NOT_VALID_FILED = "Заполнение поля корректно";
        }
    }
    interface Error {
        String RESUME_NOT_FOUND = "Резюме с '%d' не найден!";
        String FREELANCER_WITH_NOT_FOUND = "Исполнитель с '%s' не найден!";
        String FREELANCER_NOT_FOUND = "Исполнитель не найден!";
        String DUPLiCATE_EMAIL = "Пользовать с email '%s' уже зарегистрирован!";
    }
    interface DefaultValue {
        int PAGE = 0;
        int ELEMENT_PER_PAGE = 10;
    }
}