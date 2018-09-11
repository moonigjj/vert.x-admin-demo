/**
 * chenxitech.cn Inc. Copyright (c) 2017-2018 All Rights Reserved.
 */
package service;

import db.HikariCPManager;

/**
 *
 * @author tangyue
 * @version $Id: MerchantService.java, v 0.1 2018-07-23 17:30 tangyue Exp $$
 */
public class MerchantService {

    private static HikariCPManager hikariCPM = HikariCPManager.getInstance();

    private static final String BASE = "id , mer_name merchantName, nation, province, city, address," +
            " mer_dec dec, mer_phone phone, mer_url url, DATE_FORMAT(mer_begin_time,'%Y-%m-%d') beginTime";
}
