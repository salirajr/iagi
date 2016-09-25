package com.rj.sysinvest.security.repo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.UniqueConstraint;
import lombok.Data;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Entity
@Data
public class SecurityRole implements Serializable {

    @Id
    private String roleName;
//    @OneToMany(fetch = FetchType.LAZY)
//    private List<SecurityResource> resources;
    @ElementCollection
    @CollectionTable(
            joinColumns = @JoinColumn(name = "role_name"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"role_name", "resources"})
    )
    private List<String> resources;
}
