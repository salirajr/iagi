package com.rj.sysinvest.model;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Version;
import lombok.Data;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Entity
@Data
public class Investment implements Serializable {

    @Id
    private String id;
    @Column
    private Timestamp trxTimestamp;
    @ManyToOne(fetch = FetchType.LAZY)
    private Investor investor;
    @ManyToOne(fetch = FetchType.LAZY)
    private Room room;

    @Version
    private int version;

    @PrePersist
    void prePersist() {
        setTrxTimestamp(new Timestamp(System.currentTimeMillis()));
    }

}
