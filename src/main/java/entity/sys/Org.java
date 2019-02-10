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
 * @version $Id: Org.java, v 0.1 2019-02-10 14:25 tangyue Exp $$
 */
@Data
@NoArgsConstructor
public class Org implements Serializable {

    private Long id;

    private Long parentId;

    private String code;

    private String name;

    private String address;

    private String contact;

    private String contactName;

    private Integer delFlag;

    private Date createTime;

    private Date updateTime;
}
