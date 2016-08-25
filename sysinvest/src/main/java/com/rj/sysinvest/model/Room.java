package com.rj.sysinvest.model;

import java.awt.Polygon;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
    // [[x,y],[x,y],[x,y],[x,y]]
    private String areaAsJsonArray;
    @Column
    private String investorId;
    @Column
    private String towerId;

}
