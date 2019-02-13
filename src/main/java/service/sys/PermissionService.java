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
public class PermissionService extends JdbcRepositoryWrapper {

    private static final String BASE = " id, name, remark, create_time ";

    private static final String QUERY_ALL_PAGE = "SELECT" + BASE + "FROM SYS_PERMISSION " +
            "order by id LIMIT ?, ?";

    private static final String QUERY_PERMISSION_ID = "SELECT" + BASE + "FROM SYS_PERMISSION " +
            "where id = ?";

    private static final String QUERY_PERMISSION_NAME = "SELECT" + BASE + "FROM SYS_PERMISSION " +
            "where name = ? limit 1";

    private static final String INSERT_PERMISSION = "INSERT INTO SYS_PERMISSION " +
            "(name, remark, create_time) " +
            "VALUES (?, ?, ?)";

    private static final String UPDATE_PERMISSION = "UPDATE SYS_PERMISSION SET ";
}
