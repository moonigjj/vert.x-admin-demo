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
