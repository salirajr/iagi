package com.rj.sysinvest.security.exception;

import lombok.Data;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Data
public class AppSecurityExceptionBean {

    private int code;
    private String type;
    private String message;
}
