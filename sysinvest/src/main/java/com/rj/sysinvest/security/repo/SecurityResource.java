package com.rj.sysinvest.security.repo;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Entity
@Data
public class SecurityResource implements Serializable {

    @Id
    private String uriPattern;
}
