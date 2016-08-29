package com.rj.sysinvest.model;

import java.io.Serializable;
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

    @Version
    private Timestamp version;
}
