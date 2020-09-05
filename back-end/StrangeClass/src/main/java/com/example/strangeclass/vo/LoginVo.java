package com.example.strangeclass.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel("登录接口入参对象")
@Data
public class LoginVo {

    @ApiModelProperty("用户名")
    @NotBlank(message = "填写的用户名不能为空")
    private String name;

    @ApiModelProperty("密码")
    @NotBlank(message = "填写的密码不能为空")
    private String password;
}
