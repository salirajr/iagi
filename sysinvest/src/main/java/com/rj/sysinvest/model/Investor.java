package com.rj.sysinvest.model;

import java.io.Serializable;
import java.sql.Date;
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
@Table(name = Investor.TABLE_NAME)
public class Investor implements Serializable {

    public static final String TABLE_NAME = "investor";

    // 1) ID investor juga sudah termasuk menjadi nomor virtual account
    // 2) Angka wajib pendahulu yakni 88443
    // 3) Dua angka berikutnya adalah dua digit trakhir tahun akad, mis 2016 maka diinput 16
    // 4) satu angka berikutnya adalah golongan jatuh tempo, (Jatuh tempo 1 adalah tnggal 7, jatuh tempo 2 adalah tanggal 14, jatuh tempo 3 adalah 21, jatuh tempo 4 adalah 28 dan 29)
    // 5) Empat angka berikutnya adalah nomor urut yang digenerate berdasarkan urutan inputan di sistem atau sifatnya otomatis, misal 0001
    //    Contohnyaa, Ani akad di tahun 2014, masuk kedalam Jatuh Tempo tanggal 28, maka IDnya adalah 884431440001
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 10)
    private String accountId;
    

    @Column(nullable = false, length = 200)
    private String fullName;

    @Column(length = 100)
    private String nickName;

    @Column(nullable = false,length = 1)
    private String gender;

    @Column(nullable = false,length = 50)
    private String birthPlace;

    @Column(nullable = false)
    private Date birthDate;

    @Column(length = 100)
    private String emailAddress;

    @Column(length = 15)
    private String phoneNo;

    @Column(length = 15)
    private String mobilePhoneNo;

    @Column(length = 100)
    private String socialMediaId;

    @Column(length = 200)
    private String address;

    @Column(length = 7)
    private String postalCode;

    @Column(length = 100)
    private String province;

    @Column(nullable = false, unique = true, length = 100)
    private String nationalId;

    @Column(length = 100)
    private String nationality;

    @Column(length = 100)
    private String occupation;

    @Column(length = 100)
    private String jobSector;

    @Column(length = 100)
    private String jobSectorDetail;

    @Column(length = 100)
    private String employer;

    @Column(length = 100)
    private String employerAddress;

    @Column(length = 100)
    private String employerPhoneNo;

    @Column(length = 100)
    private String bankAccount;

    @Column(length = 100)
    private String bankAccountNo;

    @Column(length = 100)
    private String bankAccountReference;

    @Column(length = 100)
    private String scannedNationalIdPath;

    @Version
    private Timestamp version;
}
