/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rj.sysinvest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;

/**
 *
 * @author salirajr
 */

@Entity
@Data
@Table(name = Rank.TABLE_NAME)
public class Rank implements Serializable {
    
    public static final String TABLE_NAME = "rank";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, length = 100)
    private String name;
    
    @JsonIgnore
    @OneToMany(mappedBy = Staff.PROP_RANK)
    private List<Staff> staffs;
    
    
}
