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
@Table(name = Booking.TABLE_NAME)
public class Booking implements Serializable {

    public static final String TABLE_NAME = "booking";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Timestamp auditTime;

    @OneToMany
    private Collection<Investment> investments;

    @ManyToOne
    private Staff broker;
    public static final String PROP_BROKER = "broker";
    
    @Column
    private Long rate;
    
    @Column
    private Long marketRate;
    
    @Column
    private Long bookRate;
    
    @Column
    private String bookCode;
    

    @PrePersist
    void prePersist() {
        setAuditTime(new Timestamp(System.currentTimeMillis()));
    }
}
