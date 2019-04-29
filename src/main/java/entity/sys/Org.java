/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package entity.sys;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import common.CustomDateSerializer;

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

    private Long parentId = 0L;

    private String code;

    private String name;

    private String address = "";

    private String contact = "";

    private String contactName = "";

    private Integer delFlag = 0;

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date createTime;

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date updateTime;
}
