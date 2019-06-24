/**
 * chenxitech.cn Inc. Copyright (c) 2017-2018 All Rights Reserved.
 */
package entity.dish;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import common.CustomDateSerializer;

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

    private Long orgId;

    private String dishName;

    private BigDecimal dishPrice;

    private BigDecimal dishDiscountPrice = new BigDecimal("0");

    private String dishIcon = "";

    private Integer dishTakeout;

    private String remark = "";

    private Integer dishStatus = 0;

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date createTime;

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date updateTime;
}
