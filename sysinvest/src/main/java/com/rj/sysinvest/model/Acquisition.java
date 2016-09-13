package com.rj.sysinvest.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
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
    private Timestamp auditTime;

    @OneToMany
    private Collection<Investment> investments;

    @ManyToOne
    private Investor investor;
    public static final String PROP_INVESTOR = "investor";

    @ManyToOne
    private Staff staff;
    public static final String PROP_STAFF = "staff";
    
    @OneToMany
    private Collection<Payment> payments;

    @Column
    private String type;
    
    @Column
    private String reference;

    @Column
    private long rate;
    
    @Column
    private long marketRate;

    @PrePersist
    void prePersist() {
        setAuditTime(new Timestamp(System.currentTimeMillis()));
    }
}
