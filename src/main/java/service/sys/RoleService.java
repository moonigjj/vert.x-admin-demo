/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package service.sys;

import db.JdbcRepositoryWrapper;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author tangyue
 * @version $Id: DeptService.java, v 0.1 2019-02-10 15:24 tangyue Exp $$
 */
@Slf4j
public class RoleService extends JdbcRepositoryWrapper {

    private static final String BASE = " id, parent_id, org_id, name, remark, del_flag, create_time, update_time ";

    private static final String QUERY_All_PAGE = "SELECT" + BASE + "FROM SYS_ROLE " +
            "where org_id = ? order by id LIMIT ?, ?";

    private static final String QUERY_ROLE_ID = "SELECT" + BASE + "FROM SYS_ROLE " +
            "where org_id = ? and id = ?";

    private static final String QUERY_ROLE_NAME = "SELECT" + BASE + "FROM SYS_ROLE " +
            "where org_id = ? and name = ? limit 1";

    private static final String INSERT_ROLE = "INSERT INTO SYS_ROLE " +
            "(parent_id, org_id, name, remark, del_flag, create_time, update_time) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_ROLE = "UPDATE SYS_ROLE SET ";
}
