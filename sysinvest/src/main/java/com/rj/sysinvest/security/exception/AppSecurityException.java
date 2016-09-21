package com.rj.sysinvest.security.exception;

import lombok.Data;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
//@ResponseStatus(value = HttpStatus.UNAUTHORIZED) // 401
@Data
public class AppSecurityException extends Exception {

    private int code;

    public AppSecurityException(int code, String message) {
        super(message);
        this.code = code;
    }

    public AppSecurityException(int code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public AppSecurityException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

}
