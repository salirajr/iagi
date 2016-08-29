package com.rj.sysinvest.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
}
