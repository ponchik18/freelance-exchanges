package com.bsuir.constant;

public interface CustomerServiceConstant {
    interface Validation {
        interface Message {
            String NOT_EMPTY = "Заполните поля!";
            String NOT_VALID_FILED = "Заполнение поля корректно";
        }
    }
    interface Error {
        String CUSTOMER_NOT_FOUND = "Заказчик с '%d' не найден!";
        String DUPLiCATE_EMAIL = "Пользовать с email '%s' уже зарегистрирован!";
    }
    interface DefaultValue {
        int PAGE = 1;
        int ELEMENT_PER_PAGE = 10;
    }
}