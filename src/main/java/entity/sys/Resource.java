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
 * @version $Id: Resource.java, v 0.1 2019-01-30 13:17 tangyue Exp $$
 */
@Data
@NoArgsConstructor
public class Resource implements Serializable {

    private Long id;

    private Long parentId = 0L;

    private Long resourceId;

    private Integer type;

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date createTime;
}
