/**
 * chenxitech.cn Inc. Copyright (c) 2017-2018 All Rights Reserved.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author tangyue
 * @version $Id: DishFood.java, v 0.1 2018-06-14 10:03 tangyue Exp $$
 */
@Data
@NoArgsConstructor
public class DishFood implements Serializable {

    private Long id;

    private Long merchantId;

    private String dishName;

    private BigDecimal dishPrice;

    private BigDecimal dishDiscountPrice;

    private String dishIcon;

    private Integer dishTakeout;

    private String remark;

    private Integer dishStatus;

    private Date createTime;

    private Date updateTime;
}
