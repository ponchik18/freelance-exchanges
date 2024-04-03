package com.bsuir.constants;

public interface PaymentServiceConstants {
    interface Error {
        interface Message {
            String PAY_PAL_NOT_FOUND = "PayPal Account для пользователя с id '%s' не найден!";
            String DUPLICATE_VALUE = "Уже существует пользователь со значением '%s' в поле %s уже существует!";
        }
    }
}