package com.rj.sysinvest.model;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
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

    @OneToOne(optional = false)
    private Aparkost aparkost;
    public static final String PROP_APARKOST = "aparkost";

    @Column(length = 19)
    private Long marketRate;

    @Column
    private Timestamp marketRateUpdate;

    @Column(length = 19)
    private Double soldRate;

    @Column
    private Timestamp timestamp;
}
