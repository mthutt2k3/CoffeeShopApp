package com.android.coffeeshop.utils;

import java.util.Locale;

public class FormartCurrency {
    public static String formatVNCurrency(double value) {
        Locale locale = new Locale("vi", "VN");
        java.util.Currency currency = java.util.Currency.getInstance(locale);
        java.text.NumberFormat format = java.text.NumberFormat.getCurrencyInstance(locale);

        format.setCurrency(currency);
        format.setMaximumFractionDigits(0);

        return format.format(value);
    }
}
