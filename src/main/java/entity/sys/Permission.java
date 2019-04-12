/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package entity.sys;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author tangyue
 * @version $Id: Permission.java, v 0.1 2019-02-10 14:19 tangyue Exp $$
 */
@Data
@NoArgsConstructor
public class Permission implements Serializable {

    private Long id;

    @NotBlank
    private String name;

    private String remark = "";

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
