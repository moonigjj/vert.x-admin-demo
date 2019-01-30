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
 * @version $Id: Resource.java, v 0.1 2019-01-30 13:17 tangyue Exp $$
 */
@Data
@NoArgsConstructor
public class Resource implements Serializable {

    private Long id;

    private Long parentId;

    private Long resourceId;

    private Integer type;

    private Date createTime;
}
