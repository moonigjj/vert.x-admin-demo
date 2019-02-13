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
 * @version $Id: Menu.java, v 0.1 2019-01-30 10:20 tangyue Exp $$
 */
@Data
@NoArgsConstructor
public class Menu implements Serializable {

    private Long id;

    private Long parentId = 0L;

    private String name;

    private String url = "";

    private String icon = "";

    private Integer sortNum = 0;

    private Integer status = 0;

    private String remark = "";

    private Date createTime;

    private Date updateTime;
}
