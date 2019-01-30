/**
 * chenxitech.cn Inc. Copyright (c) 2017-2018 All Rights Reserved.
 */
package entity;

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

    private Long merchantId;

    private String deskNum;

    private String url;

    private String remark;

    private Integer deskStatus;

    private Date createTime;

    private Date updateTime;
}
