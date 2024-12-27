package com.notmarra.notcredits.util;

import com.notmarra.notcredits.NotCredits;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Numbers {
    public static String formatBalance(double value) {
        boolean useDecimals = NotCredits.getInstance().getConfig().getBoolean("balance_decimal");
        boolean useShortFormat = NotCredits.getInstance().getConfig().getBoolean("balance_short");

        if (useShortFormat) {
            final String[] suffixes = {"", "k", "M", "B", "T"};
            int suffixIndex = 0;
            double shortValue = value;

            while (shortValue >= 1000 && suffixIndex < suffixes.length - 1) {
                shortValue /= 1000;
                suffixIndex++;
            }

            if (useDecimals) {
                String pattern = NotCredits.getInstance().getConfig().getString("balance_format");
                DecimalFormat decimalFormat = new DecimalFormat(pattern);
                decimalFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
                return decimalFormat.format(shortValue) + suffixes[suffixIndex];
            } else {
                if (shortValue == Math.floor(shortValue)) {
                    return String.format("%.0f%s", shortValue, suffixes[suffixIndex]);
                }
                return String.format("%.2f%s", shortValue, suffixes[suffixIndex]);
            }
        } else {
            if (useDecimals) {
                String pattern = NotCredits.getInstance().getConfig().getString("balance_format");
                DecimalFormat decimalFormat = new DecimalFormat(pattern);
                decimalFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
                return decimalFormat.format(value);
            } else {
                return String.valueOf(Math.round(value));
            }
        }
    }
}
