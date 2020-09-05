package com.example.strangeclass.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("用户对象")
@Data
public class UserVo {

    @ApiModelProperty("用户标识")
    private Long id;

    @ApiModelProperty("用户名")
    private String name;

    @ApiModelProperty("用户头像")
    private String image;

    @ApiModelProperty("用户状态")
    private Integer status;

    @ApiModelProperty("用户角色")
    private String role;

    @ApiModelProperty("用户信息")
    private String info;
}
