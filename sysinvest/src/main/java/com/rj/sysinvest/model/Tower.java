package com.rj.sysinvest.model;

import java.io.Serializable;
import java.sql.Blob;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import lombok.Data;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Entity
@Data
public class Tower implements Serializable {

    @Id
    private String id;
    @Column
    private String name;
    @Column
    private String siteName;
    @Column
    @Lob
    private byte[] layoutTemplateRaw;
    @OneToMany(fetch = FetchType.LAZY)
    private List<Room> rooms;
}
