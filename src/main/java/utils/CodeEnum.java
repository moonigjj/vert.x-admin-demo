/**
 * chenxitech.cn Inc. Copyright (c) 2017-2018 All Rights Reserved.
 */
package utils;

/**
 *
 * @author tangyue
 * @version $Id: CodeEnum.java, v 0.1 2018-05-14 13:31 tangyue Exp $$
 */
public enum CodeEnum {

    SYS_SUC(200, ""),
    SYS_ERROR(500, "系统异常"),
    SYS_REQUEST(400, "提交参数有误!"),
    SYS_NO_DATA(400, "数据不存在"),

    DESK_NUM_EXIST(400, "餐号已存在"),
    DEPT_NUM_EXIST(400, "部门已存在"),
    MENU_NUM_EXIST(400, "菜单已存在"),
    OPERATION_NUM_EXIST(400, "操作已存在"),
    PERMISSION_NUM_EXIST(400, "权限已存在"),
    RESOURCE_NUM_EXIST(400, "资源已存在"),
    ORG_NUM_EXIST(400, "机构已存在"),
    ROLE_NUM_EXIST(400, "角色已存在"),
    USER_NUM_EXIST(400, "用户名已存在"),
    DISH_NAME_EXIST(400, "菜品已存在")
    ;


    private Integer code;

    private String message;

    CodeEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
