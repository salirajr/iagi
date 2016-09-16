package com.rj.sysinvest.akad.util;

/**
 *
 * original author https://gist.github.com/nugraha16
 * https://gist.github.com/nugraha16/5676332
 */
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.springframework.stereotype.Component;

@Component
public class Terbilang {

    private final String[] DIGITS = {"nol", "satu", "dua", "tiga",
        "empat", "lima", "enam", "tujuh", "delapan", "sembilan"};
    private final Map<Integer, String> ORDERS = new HashMap(7);

    public Terbilang() {
        ORDERS.put(0, "");
        ORDERS.put(1, "puluh");
        ORDERS.put(2, "ratus");
        ORDERS.put(3, "ribu");
        ORDERS.put(6, "juta");
        ORDERS.put(9, "miliar");
        ORDERS.put(12, "triliun");
        ORDERS.put(15, "kuadriliun");
    }

    public String getTerbilang(Number number) {
        Class clazz = number.getClass();
        String num = number.toString();
        boolean is_neg;
        if (Double.class.equals(clazz) || Float.class.equals(clazz)) {
            Double chk = Double.parseDouble(num);
            is_neg = chk < 0;
        } else {
            is_neg = num.startsWith("-");
        }
        String ints = "";
        Pattern regex = Pattern.compile("^[+-]?(\\d+)");
        Matcher regexMatcher = regex.matcher(num);
        if (regexMatcher.find()) {
            ints = regexMatcher.group(1);
        }
        int mult = 0;
        String wint = "";
        while (!ints.isEmpty()) {
            try {
                regex = Pattern.compile("(\\d{1,3})$");
                regexMatcher = regex.matcher(ints);
                while (regexMatcher.find()) {
                    // ambil satuan, puluhan, dan ratusan
                    int m = Integer.parseInt(regexMatcher.group());
                    int s = (m % 10);
                    int p = ((m % 100 - s) / 10);
                    int r = ((m - p * 10 - s) / 100);
                    // konversi ratusan
                    String g;
                    if (r == 0) {
                        g = "";
                    } else if (r == 1) {
                        g = "se" + ORDERS.get(2);
                    } else {
                        g = DIGITS[r] + " " + ORDERS.get(2);
                    }

                    // konversi puluhan dan satuan
                    if (p == 0) {
                        if (s == 0) {
                        } else if (s == 1) {
                            g = (!g.isEmpty() ? (g + " " + DIGITS[s]) : (mult == 0 ? DIGITS[1] : "se"));
                        } else {
                            g = (!g.isEmpty() ? g + " " : "") + DIGITS[s];
                        }
                    } else if (p == 1) {
                        if (s == 0) {
                            g = (!g.isEmpty() ? g + " " : "") + "se" + ORDERS.get(1);
                        } else if (s == 1) {
                            g = (!g.isEmpty() ? g + " " : "") + "sebelas";
                        } else {
                            g = (!g.isEmpty() ? g + " " : "") + DIGITS[s] + " belas";
                        }
                    } else {
                        g = (!g.isEmpty() ? g + " " : "") + DIGITS[p] + " puluh" + (s > 0 ? " " + DIGITS[s] : "");
                    }

                    // gabungkan dengan hasil sebelumnya
                    wint = (!g.isEmpty() ? (g + (g.equals("se") ? "" : " ") + ORDERS.get(mult)) : "") + (!wint.isEmpty() ? " " + wint : "");

                    // pangkas ribuan/jutaan/dsb yang sudah dikonversi
                    String resultString;
                    try {
                        Pattern tsRegex = Pattern.compile("\\d{1,3}$");
                        Matcher regexMatchers = tsRegex.matcher(ints);
                        try {
                            resultString = regexMatchers.replaceAll("");
                            ints = resultString;
                        } catch (IllegalArgumentException | IndexOutOfBoundsException ex1) {
                        }
                    } catch (PatternSyntaxException ex3) {
                    }
                    mult += 3;
                }
            } catch (PatternSyntaxException ex) {
            }
        }
        if (wint.isEmpty()) {
            wint = DIGITS[0];
        }
        //// angka di kanan desimal
        String frac = "";
        try {
            Pattern regexf = Pattern.compile("\\.(\\d+)");
            Matcher regexMatcherf = regexf.matcher(num);
            if (regexMatcherf.find()) {
                frac = regexMatcherf.group();
            }
        } catch (PatternSyntaxException ex) {
        }
        String wfrac = "";
        for (int i = 0; i < frac.length(); i++) {
            try {
                int indexf = Integer.parseInt(frac.substring(i, i + 1));
                if (DIGITS.length >= indexf) {
                    wfrac += (!wfrac.isEmpty() ? " " : "") + DIGITS[indexf];
                }
            } catch (NumberFormatException ex) {
            }
        }
        String result = ((is_neg ? "minus " : "") + wint + ((!wfrac.isEmpty()) ? " koma " + wfrac : ""));
        result = result.replaceAll("\\s{2,}", " ");
        result = result.replaceAll("(null )|(\\s{1,}$)", "");
        return result;
    }
}
