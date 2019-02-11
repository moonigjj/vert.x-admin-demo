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
public class ResourceService extends JdbcRepositoryWrapper {

    private static final String BASE = " id, content_id, type, parent_id, create_time ";

    private static final String QUERY_All_PAGE = "SELECT" + BASE + "FROM SYS_RESOURCE " +
            "order by id LIMIT ?, ?";

    private static final String QUERY_RESOURCE_ID = "SELECT" + BASE + "FROM SYS_RESOURCE " +
            "where id = ?";

    private static final String QUERY_RESOURCE_NAME = "SELECT" + BASE + "FROM SYS_RESOURCE " +
            "where name = ? limit 1";

    private static final String INSERT_RESOURCE = "INSERT INTO SYS_RESOURCE " +
            "(content_id, type, parent_id, create_time) " +
            "VALUES (?, ?, ?, ?)";

    private static final String UPDATE_RESOURCE = "UPDATE SYS_RESOURCE SET ";
}
