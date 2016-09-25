package com.rj.sysinvest.security.repo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.UniqueConstraint;
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
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            joinColumns = @JoinColumn(name = "USER_NAME"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_NAME"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"USER_NAME", "ROLE_NAME"})
    )
    private List<SecurityRole> roles;
}
