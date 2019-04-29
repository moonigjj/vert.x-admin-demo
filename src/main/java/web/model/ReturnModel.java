/**
 * chenxitech.cn Inc. Copyright (c) 2017-2018 All Rights Reserved.
 */
package web.model;

import java.io.Serializable;

import lombok.Data;

/**
 *
 * @author tangyue
 * @version $Id: GenericReturnModel.java, v 0.1 2018-05-02 14:08 tangyue Exp $$
 */
@Data
public class ReturnModel<T> implements Serializable {

    private boolean suc;
    private String reason = "操作成功";
    private T result;
}
