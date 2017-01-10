/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author JOVIR
 */
public class IdGenerator {

    public static void main(String[] args) {
int id = 1;
        int gen = 1;
        
        
        DateFormat dateFormat = new SimpleDateFormat("yy-MM");
        
        Date date = new Date();
        System.out.println("BS"+String.format("%04d", id)+dateFormat.format(date)+"."+gen);

    }
}
