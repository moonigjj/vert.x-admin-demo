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
 * @version $Id: Operation.java, v 0.1 2019-01-29 11:24 tangyue Exp $$
 */
@Data
@NoArgsConstructor
public class Operation implements Serializable {

    private Long id;

    @NotBlank(message = "操作名称不能为空")
    private String name;

    private String operation = "";

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date createTime;
}
