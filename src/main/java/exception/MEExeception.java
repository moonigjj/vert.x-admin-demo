/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package exception;

/**
 *
 * @author tangyue
 * @version $Id: MEExeception.java, v 0.1 2019-02-13 16:06 tangyue Exp $$
 */
public class MEExeception extends RuntimeException {

    public MEExeception(String message) {
        super(message);
    }

    public MEExeception(String message, Throwable cause) {
        super(message, cause);
    }

    public MEExeception(Throwable cause) {
        super(cause);
    }

    protected MEExeception(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
