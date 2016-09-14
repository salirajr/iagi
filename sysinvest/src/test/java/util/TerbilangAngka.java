/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.rj.sysinvest.akad.util.JavaTerbilang;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 *
 * @author salirajr
 */
public class TerbilangAngka {

    public static void main(String[] args) {
        long bilangan = 909;
        for (int i = 0; i < 10; i++) {
            bilangan *= 10;
            System.out.println(bilangan+" "+new JavaTerbilang(bilangan));
        }
        BigDecimal d = new BigDecimal("19005600000090877000");
        System.out.println(d+" "+new JavaTerbilang(d));
    }
}
