package com.example.strangeclass.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel("用户Id对象")
@Data
public class UserIdVo {

    @ApiModelProperty("用户标识")
    @NotNull(message = "用户标识不能为空")
    private Long id;
}
