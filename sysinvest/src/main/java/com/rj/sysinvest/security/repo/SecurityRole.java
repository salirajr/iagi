package com.rj.sysinvest.security.repo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
    @OneToMany(fetch = FetchType.LAZY)
    private List<SecurityResource> resources;
}
