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
 * @version $Id: Role.java, v 0.1 2019-02-10 14:21 tangyue Exp $$
 */
@Data
@NoArgsConstructor
public class Role implements Serializable {

    private Long id;

    private Long parentId = 0L;

    private Long orgId;

    private String name;

    private String remark = "";

    private Integer delFlag = 0;

    private Date createTime;

    private Date updateTime;
}
