package com.rj.sysinvest.model;

/**
 *
 * @author salirajr
 */
import com.rj.sysinvest.security.repo.SecurityUser;
import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = Staff.TABLE_NAME)
public class Staff implements Serializable {

    public static final String TABLE_NAME = "staff";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String fullName;

    @Column(length = 1)
    private String gender;

    @Column(length = 50)
    private String birthPlace;

    @Column
    private Date birthDate;

    @Column(length = 200)
    private String address;

    @Column(length = 7)
    private String postalCode;

    @Column(length = 100)
    private String province;

    @Column(unique = true, length = 100)
    private String nationalId;

    @Column(length = 100)
    private String scannedNationalIdPath;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = SecurityUser.PROP_USERNAME, unique = true)
    private SecurityUser userLogin;

    @ManyToOne
    private Rank rank;
    public static final String PROP_RANK = "rank";

}
