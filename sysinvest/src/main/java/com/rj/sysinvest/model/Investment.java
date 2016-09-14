package com.rj.sysinvest.model;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Entity
@Data
@Table(name = Investment.TABLE_NAME)
public class Investment implements Serializable {

    public static final String TABLE_NAME = "investment";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Aparkost aparkost;
    public static final String PROP_APARKOST = "aparkost";

    @Column(length = 19)
    private Long marketRate;
    
    @Column(length=1)
    private String flag;

    @Column
    private Timestamp marketRateUpdate;

    @Column(length = 19)
    private Long soldRate;
    
    @Column(nullable = true)
    private Timestamp soldTrx = null;

}
