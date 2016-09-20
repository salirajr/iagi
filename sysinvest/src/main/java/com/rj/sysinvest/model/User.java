package com.rj.sysinvest.model;

import com.rj.sysinvest.model.Staff;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.Data;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Entity
@Data
public class User implements Serializable {

    @Id
    @Column(length = 100)
    private String userName;
    @Column
    private String password;
    @OneToOne(fetch = FetchType.LAZY)
    private Staff staff;
    @OneToMany(fetch = FetchType.LAZY)
    private List<Role> roles;
}
