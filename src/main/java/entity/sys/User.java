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
 * @version $Id: User.java, v 0.1 2019-02-10 14:28 tangyue Exp $$
 */
@Data
@NoArgsConstructor
public class User implements Serializable {

    private Long id;

    private Long orgId;

    private String userName;

    private String realName;

    private String nickName;

    private String pwd;

    private String salt;

    private String openId;

    private String unionId;

    private Integer status;

    private Integer online;

    private Integer delFlag;

    private Date createTime;

    private Date updateTime;
}
