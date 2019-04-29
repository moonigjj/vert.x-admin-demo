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
 * @version $Id: User.java, v 0.1 2019-02-10 14:28 tangyue Exp $$
 */
@Data
@NoArgsConstructor
public class User implements Serializable {

    private Long id;

    private Long orgId;

    private String userName;

    private String realName = "";

    private String nickName = "";

    private String avatar = "";

    private String pwd;

    private String salt;

    private String openId = "";

    private String unionId = "";

    private Integer status = 1;

    private Integer online = 0;

    private Integer delFlag = 0;

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date createTime;

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date updateTime;
}
