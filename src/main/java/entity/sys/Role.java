/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package entity.sys;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import common.CustomDateSerializer;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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

    @NotNull
    private Long orgId;

    @NotBlank(message = "角色名称不能为空")
    private String name;

    private String remark = "";

    private Integer delFlag = 0;

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date createTime;

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date updateTime;
}
