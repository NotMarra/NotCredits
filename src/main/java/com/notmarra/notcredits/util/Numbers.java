package com.notmarra.notcredits.util;

import com.notmarra.notcredits.NotCredits;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Numbers {
    public static String formatBalance(double value) {
        if (NotCredits.getInstance().getConfig().getBoolean("balance_decimal")) {
            DecimalFormat decimalFormat = new DecimalFormat(NotCredits.getInstance().getConfig().getString("balance_format"));
            decimalFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
            return String.valueOf(Double.parseDouble(decimalFormat.format(value).replace(',', '.')));
        } else {
            return String.valueOf(Math.round(value));
        }
    }
}
