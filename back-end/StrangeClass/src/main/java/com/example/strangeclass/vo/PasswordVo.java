package com.example.strangeclass.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel("用户密码对象")
@Data
public class PasswordVo {

    @ApiModelProperty("用户旧密码")
    @NotBlank(message = "用户旧密码不能为空")
    private String oldPassword;

    @ApiModelProperty("用户新密码")
    @NotBlank(message = "用户新密码不能为空")
    private String newPassword;
}
