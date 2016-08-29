package com.rj.sysinvest.model;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import lombok.Data;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Entity
@Data
public class Room implements Serializable {

    @Id
    private String id;
    @Column
    private String name;
    @Column
    private String levelId;
    @Column
    private String positionId;
    @ManyToOne(fetch = FetchType.LAZY)
    private Investor investor;
    @ManyToOne(fetch = FetchType.LAZY)
    private Tower tower;

    @Version
    private Timestamp version;
}
