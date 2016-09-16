/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rj.sysinvest.model.util;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Data;

/**
 *
 * @author salirajr
 */
@Entity
@Data
@Table(name = Lookup.TABLE_NAME, uniqueConstraints = {
    @UniqueConstraint(columnNames = {"groupName", "keyName"})
})
public class Lookup implements Serializable {

    public static final String TABLE_NAME = "lookup";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String groupName;

    @Column
    private String keyName;

    @Column
    private String text;

}
