package com.rj.sysinvest.layout;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
public class StringUtil {

    public static String buildString(String prefix, String infix, String suffix, String... strings) {
        StringBuilder sb = new StringBuilder();
        if (prefix != null) {
            sb.append(prefix);
        }
        int last = strings.length - 1;
        for (int i = 0; i < last; i++) {
            sb.append(strings[i]);
            if (infix != null) {
                sb.append(infix);
            }
        }
        sb.append(strings[last]);
        if (suffix != null) {
            sb.append(suffix);
        }
        return sb.toString();
    }
}
