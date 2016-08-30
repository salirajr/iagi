package com.rj.sysinvest.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import lombok.Data;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Entity
@Data
public class Investor implements Serializable {

    @Id
    private String id;
    @Column
    private String name;
    @OneToMany(mappedBy = "investor", fetch = FetchType.LAZY)
    private List<Room> rooms;
    @Column
    private String job;
    @Column
    private String address;
    @Column
    private String idCardNumber;
    @Column
    private String birthPlace;
    @Column
    private Date birthDate;
    @Version
    private Timestamp version;
}
