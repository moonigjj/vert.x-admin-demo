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
 * @version $Id: Dept.java, v 0.1 2019-02-10 14:23 tangyue Exp $$
 */
@Data
@NoArgsConstructor
public class Dept implements Serializable {

    private Long id;

    private Long parentId;

    private Long orgId;

    private String name;

    private Integer level = 0;

    private String remark = "";

    private Integer delFlag;

    private Date createTime;

    private Date updateTime;
}
