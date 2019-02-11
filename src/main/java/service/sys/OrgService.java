/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package service.sys;

import db.JdbcRepositoryWrapper;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author tangyue
 * @version $Id: OrgService.java, v 0.1 2019-02-10 15:26 tangyue Exp $$
 */
@Slf4j
public class OrgService extends JdbcRepositoryWrapper {

    private static final String BASE = " id, parent_id, code, name, address, contact, contact_number, del_flag, create_time, update_time ";

    private static final String QUERY_All_PAGE = "SELECT" + BASE + "FROM SYS_ORG " +
            "order by id LIMIT ?, ?";

    private static final String QUERY_ORG_ID = "SELECT" + BASE + "FROM SYS_ORG " +
            "where id = ?";

    private static final String QUERY_ORG_NAME = "SELECT" + BASE + "FROM SYS_ORG " +
            "where name = ? limit 1";

    private static final String INSERT_ORG = "INSERT INTO SYS_ORG " +
            "(parent_id, code, name, address, contact, contact_number, del_flag, create_time, update_time) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_ORG = "UPDATE SYS_ORG SET ";
}
