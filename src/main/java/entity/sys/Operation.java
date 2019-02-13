/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package entity.sys;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author tangyue
 * @version $Id: Operation.java, v 0.1 2019-01-29 11:24 tangyue Exp $$
 */
@Data
@NoArgsConstructor
public class Operation implements Serializable {

    private Long id;

    private String name;

    private String operation = "";

    private Date createTime;
}
