/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
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
 * @version $Id: Order.java, v 0.1 2019-06-24 15:42 tangyue Exp $$
 */
@Data
@NoArgsConstructor
public class Order implements Serializable {

    private Long id;

    private Long orgId;

    private String sn;

    private Long userId;

    private String deskNum;

    private BigDecimal orderAmount;

    private BigDecimal orderPrice;

    private BigDecimal orderPay;

    private Integer orderStatus;

    private Integer payStatus;

    private Integer payMethod;

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date createTime;

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date updateTime;
}
