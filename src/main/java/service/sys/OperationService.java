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
public class OperationService extends JdbcRepositoryWrapper {

    private static final String BASE = " id, name, operation, create_time ";

    private static final String QUERY_ALL_PAGE = "SELECT" + BASE + "FROM SYS_OPERATION " +
            "order by id LIMIT ?, ?";

    private static final String QUERY_OPERATIONMENU_ID = "SELECT" + BASE + "FROM SYS_OPERATION " +
            "where id = ?";

    private static final String QUERY_OPERATIONMENU_NAME = "SELECT" + BASE + "FROM SYS_OPERATION " +
            "where name = ? limit 1";

    private static final String INSERT_OPERATIONMENU = "INSERT INTO SYS_OPERATION " +
            "(name, operation, create_time) " +
            "VALUES (?, ?, ?)";

    private static final String UPDATE_OPERATIONMENU = "UPDATE SYS_OPERATION SET ";
}
