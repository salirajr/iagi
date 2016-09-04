package com.rj.sysinvest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Investor investor;
    public static final String PROP_INVESTOR = "investor";

    @OneToOne
    private Aparkost aparkost;
    public static final String PROP_APARKOST = "aparkost";

    @ManyToOne(fetch = FetchType.LAZY)
    private Acquisition acquisition;
    public static final String PROP_ACQUISITION = "acquisition";

    @Column(length = 19)
    private long marketRate;

    @Column
    private Timestamp marketRateUpdate;

    @Column(length = 19)
    private long soldRate;

    @Column
    private Timestamp timestamp;
}
