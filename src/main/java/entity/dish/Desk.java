/**
 * chenxitech.cn Inc. Copyright (c) 2017-2018 All Rights Reserved.
 */
package entity.dish;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import common.CustomDateSerializer;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author tangyue
 * @version $Id: Desk.java, v 0.1 2018-06-11 15:47 tangyue Exp $$
 */
@Data
@NoArgsConstructor
public class Desk implements Serializable {

    private Long id;

    private Long orgId;

    private String deskNum;

    private String url = "";

    private String remark = "";

    private Integer deskStatus = 0;

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date createTime;

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date updateTime;
}
