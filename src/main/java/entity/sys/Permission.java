/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package entity.sys;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import common.CustomDateSerializer;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author tangyue
 * @version $Id: Permission.java, v 0.1 2019-02-10 14:19 tangyue Exp $$
 */
@Data
@NoArgsConstructor
public class Permission implements Serializable {

    private Long id;

    @NotBlank(message = "权限名称不能为空")
    private String name;

    private String remark = "";

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date createTime;
}
