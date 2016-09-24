package com.rj.sysinvest.security.repo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
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
public class SecurityUser implements Serializable {

    @Id
    private String userName;
    public static final String PROP_USERNAME = "userName";
    @Column
    private String password;
    @OneToMany(fetch = FetchType.LAZY)
    private List<SecurityRole> roles;
}
