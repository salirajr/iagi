package com.rj.sysinvest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.Data;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Entity
@Data
@Table(name = Tower.TABLE_NAME)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Tower implements Serializable {

    public static final String TABLE_NAME = "tower";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String name;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Site site;
    public static final String PROP_SITE = "site";

    @JsonIgnore
    @OneToMany(mappedBy = Aparkost.PROP_TOWER, fetch = FetchType.LAZY)
    private List<Aparkost> aparkosts;

    @Version
    private Timestamp version;
}
