package com.rj.sysinvest.model;

// History Tracks
// Rab Sep 7, 09:32     salirajr    add constraints to column name and towerId
//                                  - set tower relation optional=false

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import lombok.Data;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Entity
@Data
@Table(name = Aparkost.TABLE_NAME, uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name", "tower_id"})
})
public class Aparkost implements Serializable {

    public static final String TABLE_NAME = "aparkost";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String name;

    @Column(length = 2)
    private String floor;

    @Column(length = 3)
    private Long index;

    @ManyToOne(optional = false)
    private Tower tower;
    public static final String PROP_TOWER = "tower";

    @ManyToOne
    private Investor investor;
    public static final String PROP_INVESTOR = "investor";

    @Version
    private Timestamp version;

}
