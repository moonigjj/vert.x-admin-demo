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
public class MenuService extends JdbcRepositoryWrapper {

    private static final String BASE = " id, parent_id, name, url, icon, sort_name, status, remark, create_time, update_time ";

    private static final String QUERY_ALL_PAGE = "SELECT" + BASE + "FROM SYS_MENU " +
            "where org_id = ? order by id LIMIT ?, ?";

    private static final String QUERY_MENU_ID = "SELECT" + BASE + "FROM SYS_MENU " +
            "where org_id = ? and id = ?";

    private static final String QUERY_MENU_NAME = "SELECT" + BASE + "FROM SYS_MENU " +
            "where org_id = ? and name = ? limit 1";

    private static final String INSERT_MENU = "INSERT INTO SYS_MENU " +
            "(parent_id, name, url, icon, sort_name, status, remark, create_time, update_time) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_MENU = "UPDATE SYS_MENU SET ";
}
