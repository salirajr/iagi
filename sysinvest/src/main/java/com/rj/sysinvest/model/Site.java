package com.rj.sysinvest.model;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.Data;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Entity
@Data
@Table(name = Site.TABLE_NAME)
public class Site implements Serializable {

    public static final String TABLE_NAME = "site";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 100)
    private String name;

    @Column(length = 200)
    private String address;
    
    @Column(length = 7)
    private String postalCode;

    @Column(length = 100)
    private String province;

    @Version
    private Timestamp version;
}
