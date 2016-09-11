/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rj.sysinvest.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 *
 * @author salirajr
 */
@Entity
@Data
@Table(name = Payment.TABLE_NAME)
public class Payment implements Serializable {

    public static final String TABLE_NAME = "payment";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   
    @Column
    private int index;
    
    @Column
    private long nominal;
    
    @Column
    private String description;

}
