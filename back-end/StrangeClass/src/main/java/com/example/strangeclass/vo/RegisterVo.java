package com.example.strangeclass.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@ApiModel("注册接口入参对象")
@Data
public class RegisterVo implements Serializable {

    @ApiModelProperty("用户名")
    @NotBlank(message = "用户名不能为空")
    private String name;

    @ApiModelProperty("密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty("用户角色")
    @Pattern(regexp = "Student|Teacher", message = "角色参数错误")
    private String role;
}
