-- Drop table

-- DROP TABLE public.sys_app

CREATE TABLE public.sys_app (
                              id bigserial NOT NULL, -- 主键
                              "name" text NOT NULL DEFAULT ''::text, -- 名称
                              url text NOT NULL DEFAULT ''::text, -- 地址
                              icon text NOT NULL DEFAULT ''::text, -- 图标
                              status int2 NOT NULL DEFAULT 0, -- 状态0正常，1禁用
                              remark text NOT NULL DEFAULT ''::text, -- 备注
                              create_time timestamp NOT NULL, -- 创建时间
                              update_time timestamp NOT NULL, -- 修改时间
                              CONSTRAINT sys_app_pk PRIMARY KEY (id)
);
COMMENT ON TABLE public.sys_app IS '系统app应用表';

-- Column comments

COMMENT ON COLUMN public.sys_app.id IS '主键';
COMMENT ON COLUMN public.sys_app."name" IS '名称';
COMMENT ON COLUMN public.sys_app.url IS '地址';
COMMENT ON COLUMN public.sys_app.icon IS '图标';
COMMENT ON COLUMN public.sys_app.status IS '状态0正常，1禁用';
COMMENT ON COLUMN public.sys_app.remark IS '备注';
COMMENT ON COLUMN public.sys_app.create_time IS '创建时间';
COMMENT ON COLUMN public.sys_app.update_time IS '修改时间';

-- Permissions

ALTER TABLE public.sys_app OWNER TO postgres;
GRANT ALL ON TABLE public.sys_app TO postgres;


-- Drop table

-- DROP TABLE public.sys_dept

CREATE TABLE public.sys_dept (
                               id bigserial NOT NULL,
                               org_id int8 NOT NULL DEFAULT 0, -- 组织id
                               "name" text NOT NULL DEFAULT ''::text, -- 部门名称
                               "level" int2 NOT NULL DEFAULT 0, -- 等级
                               remark text NOT NULL DEFAULT ''::text, -- 备注
                               parent_id int8 NOT NULL DEFAULT 0, -- 上级部门id
                               del_flag int2 NOT NULL DEFAULT 0, -- 状态0正常,1禁用
                               create_time timestamp NOT NULL, -- 创建时间
                               update_time timestamp NOT NULL, -- 更新时间
                               CONSTRAINT sys_dept_pk PRIMARY KEY (id)
);
COMMENT ON TABLE public.sys_dept IS '部门表';

-- Column comments

COMMENT ON COLUMN public.sys_dept.org_id IS '组织id';
COMMENT ON COLUMN public.sys_dept."name" IS '部门名称';
COMMENT ON COLUMN public.sys_dept."level" IS '等级';
COMMENT ON COLUMN public.sys_dept.remark IS '备注';
COMMENT ON COLUMN public.sys_dept.parent_id IS '上级部门id';
COMMENT ON COLUMN public.sys_dept.del_flag IS '状态0正常,1禁用';
COMMENT ON COLUMN public.sys_dept.create_time IS '创建时间';
COMMENT ON COLUMN public.sys_dept.update_time IS '更新时间';

-- Permissions

ALTER TABLE public.sys_dept OWNER TO postgres;
GRANT ALL ON TABLE public.sys_dept TO postgres;


-- Drop table

-- DROP TABLE public.sys_dept_role

CREATE TABLE public.sys_dept_role (
                                    id bigserial NOT NULL,
                                    dept_id int8 NOT NULL DEFAULT 0, -- 部门id
                                    role_id int8 NOT NULL DEFAULT 0, -- 角色id
                                    create_time timestamp NOT NULL, -- 创建时间
                                    CONSTRAINT sys_dept_role_pk PRIMARY KEY (id)
);
COMMENT ON TABLE public.sys_dept_role IS '部门角色关联表';

-- Column comments

COMMENT ON COLUMN public.sys_dept_role.dept_id IS '部门id';
COMMENT ON COLUMN public.sys_dept_role.role_id IS '角色id';
COMMENT ON COLUMN public.sys_dept_role.create_time IS '创建时间';

-- Permissions

ALTER TABLE public.sys_dept_role OWNER TO postgres;
GRANT ALL ON TABLE public.sys_dept_role TO postgres;


-- Drop table

-- DROP TABLE public.sys_menu

CREATE TABLE public.sys_menu (
                               id bigserial NOT NULL,
                               parent_id int8 NOT NULL DEFAULT 0, -- 父级id
                               "name" text NOT NULL DEFAULT ''::text, -- 菜单名称
                               url text NOT NULL DEFAULT ''::text, -- 菜单地址
                               icon text NOT NULL DEFAULT ''::text, -- 菜单图标
                               sort_num int2 NOT NULL DEFAULT 0, -- 排序字段
                               status int2 NOT NULL DEFAULT 0, -- 状态0正常1禁用
                               remark text NOT NULL DEFAULT ''::text, -- 备注
                               create_time timestamp NOT NULL, -- 创建时间
                               update_time timestamp NOT NULL, -- 更新时间
                               CONSTRAINT sys_menu_pk PRIMARY KEY (id)
);
COMMENT ON TABLE public.sys_menu IS '菜单资源表';

-- Column comments

COMMENT ON COLUMN public.sys_menu.parent_id IS '父级id';
COMMENT ON COLUMN public.sys_menu."name" IS '菜单名称';
COMMENT ON COLUMN public.sys_menu.url IS '菜单地址';
COMMENT ON COLUMN public.sys_menu.icon IS '菜单图标';
COMMENT ON COLUMN public.sys_menu.sort_num IS '排序字段';
COMMENT ON COLUMN public.sys_menu.status IS '状态0正常1禁用';
COMMENT ON COLUMN public.sys_menu.remark IS '备注';
COMMENT ON COLUMN public.sys_menu.create_time IS '创建时间';
COMMENT ON COLUMN public.sys_menu.update_time IS '更新时间';

-- Permissions

ALTER TABLE public.sys_menu OWNER TO postgres;
GRANT ALL ON TABLE public.sys_menu TO postgres;

-- Drop table

-- DROP TABLE public.sys_operation

CREATE TABLE public.sys_operation (
                                    id bigserial NOT NULL,
                                    "name" text NOT NULL DEFAULT ''::text, -- 操作名称
                                    operation text NOT NULL DEFAULT ''::text, -- 操作标志
                                    create_time timestamp NOT NULL, -- 创建时间
                                    CONSTRAINT sys_operation_pk PRIMARY KEY (id)
);
COMMENT ON TABLE public.sys_operation IS '操作表';

-- Column comments

COMMENT ON COLUMN public.sys_operation."name" IS '操作名称';
COMMENT ON COLUMN public.sys_operation.operation IS '操作标志';
COMMENT ON COLUMN public.sys_operation.create_time IS '创建时间';

-- Permissions

ALTER TABLE public.sys_operation OWNER TO postgres;
GRANT ALL ON TABLE public.sys_operation TO postgres;

-- Drop table

-- DROP TABLE public.sys_org

CREATE TABLE public.sys_org (
                              id bigserial NOT NULL,
                              parent_id int8 NOT NULL DEFAULT 0, -- 上级公司id
                              code text NOT NULL DEFAULT ''::text, -- 简称
                              "name" text NOT NULL DEFAULT ''::text, -- 公司名字
                              address text NOT NULL DEFAULT ''::text, -- 地址
                              contact text NOT NULL DEFAULT ''::text, -- 联系人
                              contact_number text NOT NULL DEFAULT ''::text, -- 联系电话
                              del_flag int2 NOT NULL DEFAULT 0, -- 是否删除0否1是
                              create_time timestamp NOT NULL, -- 创建时间
                              update_time timestamp NOT NULL, -- 更新时间
                              CONSTRAINT sys_org_pk PRIMARY KEY (id)
);
COMMENT ON TABLE public.sys_org IS '组织机构表';

-- Column comments

COMMENT ON COLUMN public.sys_org.parent_id IS '上级公司id';
COMMENT ON COLUMN public.sys_org.code IS '简称';
COMMENT ON COLUMN public.sys_org."name" IS '公司名字';
COMMENT ON COLUMN public.sys_org.address IS '地址';
COMMENT ON COLUMN public.sys_org.contact IS '联系人';
COMMENT ON COLUMN public.sys_org.contact_number IS '联系电话';
COMMENT ON COLUMN public.sys_org.del_flag IS '是否删除0否1是';
COMMENT ON COLUMN public.sys_org.create_time IS '创建时间';
COMMENT ON COLUMN public.sys_org.update_time IS '更新时间';

-- Permissions

ALTER TABLE public.sys_org OWNER TO postgres;
GRANT ALL ON TABLE public.sys_org TO postgres;

-- Drop table

-- DROP TABLE public.sys_permission

CREATE TABLE public.sys_permission (
                                     id bigserial NOT NULL,
                                     "name" text NOT NULL DEFAULT ''::text, -- 权限名称 英文
                                     remark text NOT NULL DEFAULT ''::text, -- 备注
                                     create_time timestamp NOT NULL, -- 创建时间
                                     CONSTRAINT sys_permission_pk PRIMARY KEY (id)
);
COMMENT ON TABLE public.sys_permission IS '权限表';

-- Column comments

COMMENT ON COLUMN public.sys_permission."name" IS '权限名称 英文';
COMMENT ON COLUMN public.sys_permission.remark IS '备注';
COMMENT ON COLUMN public.sys_permission.create_time IS '创建时间';

-- Permissions

ALTER TABLE public.sys_permission OWNER TO postgres;
GRANT INSERT, SELECT, UPDATE, DELETE ON TABLE public.sys_permission TO postgres;


-- Drop table

-- DROP TABLE public.sys_role

CREATE TABLE public.sys_role (
                               id bigserial NOT NULL,
                               parent_id int8 NOT NULL DEFAULT 0, -- 上级id
                               org_id int8 NOT NULL DEFAULT 0, -- 组织Id
                               "name" text NOT NULL DEFAULT ''::text, -- 角色名
                               remark text NOT NULL DEFAULT ''::text, -- 备注
                               del_flag int2 NOT NULL DEFAULT 0, -- 0正常， 1删除
                               create_time timestamp NOT NULL, -- 创建时间
                               update_time timestamp NOT NULL, -- 更新时间
                               CONSTRAINT sys_role_pk PRIMARY KEY (id)
);
COMMENT ON TABLE public.sys_role IS '角色表';

-- Column comments

COMMENT ON COLUMN public.sys_role.parent_id IS '上级id';
COMMENT ON COLUMN public.sys_role.org_id IS '组织Id';
COMMENT ON COLUMN public.sys_role."name" IS '角色名';
COMMENT ON COLUMN public.sys_role.remark IS '备注';
COMMENT ON COLUMN public.sys_role.del_flag IS '0正常， 1删除';
COMMENT ON COLUMN public.sys_role.create_time IS '创建时间';
COMMENT ON COLUMN public.sys_role.update_time IS '更新时间';

-- Permissions

ALTER TABLE public.sys_role OWNER TO postgres;
GRANT ALL ON TABLE public.sys_role TO postgres;


-- Drop table

-- DROP TABLE public.sys_role_permission

CREATE TABLE public.sys_role_permission (
                                          id bigserial NOT NULL,
                                          role_id int8 NOT NULL DEFAULT 0, -- 角色id
                                          permission_id int8 NOT NULL DEFAULT 0, -- 权限id
                                          create_time timestamp NOT NULL, -- 创建时间
                                          CONSTRAINT sys_role_permission_pk PRIMARY KEY (id)
);
COMMENT ON TABLE public.sys_role_permission IS '角色权限关联';

-- Column comments

COMMENT ON COLUMN public.sys_role_permission.role_id IS '角色id';
COMMENT ON COLUMN public.sys_role_permission.permission_id IS '权限id';
COMMENT ON COLUMN public.sys_role_permission.create_time IS '创建时间';

-- Permissions

ALTER TABLE public.sys_role_permission OWNER TO postgres;
GRANT ALL ON TABLE public.sys_role_permission TO postgres;


-- Drop table

-- DROP TABLE public.sys_user

CREATE TABLE public.sys_user (
                               id bigserial NOT NULL,
                               user_name text NOT NULL DEFAULT ''::text, -- 登录用户名
                               real_name text NOT NULL DEFAULT ''::text, -- 真实姓名
                               nick_name text NOT NULL DEFAULT ''::text, -- 昵称
                               avatar text NOT NULL DEFAULT ''::text, -- 头像
                               pwd text NOT NULL DEFAULT ''::text, -- 登录密码
                               salt text NOT NULL DEFAULT ''::text, -- 加密盐
                               org_id int8 NOT NULL DEFAULT 0, -- 组织id
                               open_id text NOT NULL DEFAULT ''::text, -- 微信open_id
                               union_id text NOT NULL DEFAULT ''::text, -- 微信union_id
                               status int2 NOT NULL DEFAULT 0, -- 状态，1正常，0禁用，
                               online int2 NOT NULL DEFAULT 0, -- 是否在线0否1是
                               del_flag int2 NOT NULL DEFAULT 0, -- 是否删除0正常1删除
                               create_time timestamp NOT NULL, -- 创建时间
                               update_time timestamp NOT NULL, -- 更新时间
                               CONSTRAINT sys_user_pk PRIMARY KEY (id)
);
COMMENT ON TABLE public.sys_user IS '用户基本信息表';

-- Column comments

COMMENT ON COLUMN public.sys_user.user_name IS '登录用户名';
COMMENT ON COLUMN public.sys_user.real_name IS '真实姓名';
COMMENT ON COLUMN public.sys_user.nick_name IS '昵称';
COMMENT ON COLUMN public.sys_user.avatar IS '头像';
COMMENT ON COLUMN public.sys_user.pwd IS '登录密码';
COMMENT ON COLUMN public.sys_user.salt IS '加密盐';
COMMENT ON COLUMN public.sys_user.org_id IS '组织id';
COMMENT ON COLUMN public.sys_user.open_id IS '微信open_id';
COMMENT ON COLUMN public.sys_user.union_id IS '微信union_id';
COMMENT ON COLUMN public.sys_user.status IS '状态，1正常，0禁用，';
COMMENT ON COLUMN public.sys_user.online IS '是否在线0否1是';
COMMENT ON COLUMN public.sys_user.del_flag IS '是否删除0正常1删除';
COMMENT ON COLUMN public.sys_user.create_time IS '创建时间';
COMMENT ON COLUMN public.sys_user.update_time IS '更新时间';

-- Permissions

ALTER TABLE public.sys_user OWNER TO postgres;
GRANT ALL ON TABLE public.sys_user TO postgres;


-- Drop table

-- DROP TABLE public.sys_user_dept

CREATE TABLE public.sys_user_dept (
                                    id bigserial NOT NULL,
                                    user_id int8 NOT NULL DEFAULT 0, -- 用户id
                                    dept_id int8 NOT NULL DEFAULT 0, -- 部门id
                                    create_time timestamp NOT NULL, -- 创建时间
                                    CONSTRAINT sys_user_dept_pk PRIMARY KEY (id)
);
COMMENT ON TABLE public.sys_user_dept IS '用户部门关联表';

-- Column comments

COMMENT ON COLUMN public.sys_user_dept.user_id IS '用户id';
COMMENT ON COLUMN public.sys_user_dept.dept_id IS '部门id';
COMMENT ON COLUMN public.sys_user_dept.create_time IS '创建时间';

-- Permissions

ALTER TABLE public.sys_user_dept OWNER TO postgres;
GRANT ALL ON TABLE public.sys_user_dept TO postgres;


-- Drop table

-- DROP TABLE public.sys_user_role

CREATE TABLE public.sys_user_role (
                                    id bigserial NOT NULL,
                                    user_id int8 NOT NULL DEFAULT 0, -- 用户id
                                    role_id int8 NOT NULL DEFAULT 0, -- 角色id
                                    create_time timestamp NOT NULL, -- 创建时间
                                    CONSTRAINT sys_user_role_pk PRIMARY KEY (id)
);
COMMENT ON TABLE public.sys_user_role IS '用户角色关联表';

-- Column comments

COMMENT ON COLUMN public.sys_user_role.user_id IS '用户id';
COMMENT ON COLUMN public.sys_user_role.role_id IS '角色id';
COMMENT ON COLUMN public.sys_user_role.create_time IS '创建时间';

-- Permissions

ALTER TABLE public.sys_user_role OWNER TO postgres;
GRANT ALL ON TABLE public.sys_user_role TO postgres;



