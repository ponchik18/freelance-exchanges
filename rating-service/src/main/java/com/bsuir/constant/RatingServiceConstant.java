package com.bsuir.constant;

public interface RatingServiceConstant {
    interface Validation {
        interface Message {
            String NOT_EMPTY = "Заполните поля!";
            String NOT_VALID_FILED = "Заполнение поля корректно";
            String NOT_VALID_RANGE = "Значение должно быть в диапазоне от 1 до 3";
        }
    }
    interface Error {
        String RATING_NOT_FOUND = "Рейтинг с '%d' не найден!";
        String DUPLICATE_RATING = "Рейтинг пользователю с id '%s' от пользователя c id '%s' уже существует!";
    }
    interface DefaultValue {
        int PAGE = 0;
        int ELEMENT_PER_PAGE = 10;
    }
}