/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rj.sysinvest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.Data;

/**
 *
 * @author salirajr
 */
@Entity
@Data
@Table(name = Acquisition.TABLE_NAME)
public class Acquisition implements Serializable {

    public static final String TABLE_NAME = "acquisition";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Timestamp timestamp;

    @OneToMany(mappedBy = Investment.PROP_ACQUISITION)
    private List<Investment> investments;
    
    @ManyToOne
    private Investor investor;
    public static final String PROP_INVESTOR = "investor";
    
    @Column
    private String type;

    @Column
    private long dpFee;

    @Column
    private long bookingFee;

    @Column
    private long totalFee;

    @Column
    private long nPeriod;

    @Column
    private Date startDate;

    @Column
    private Date endDate;

    @Column
    private double rate;

    @Version
    private int version;

    @PrePersist
    void prePersist() {
        setTimestamp(new Timestamp(System.currentTimeMillis()));
    }
}
