package com.notmarra.notcredits.util;

import com.notmarra.notcredits.Notcredits;

import java.text.DecimalFormat;

public class Decimal {
    public static String formatBalance(double value) {
        if (Notcredits.getInstance().getConfig().getBoolean("balance_decimal")) {
            DecimalFormat decimalFormat = new DecimalFormat(Notcredits.getInstance().getConfig().getString("balance_format"));
            return String.valueOf(Double.parseDouble(decimalFormat.format(value)));
        } else {
            return String.valueOf(Math.round(value));
        }
    }
}
