package com.example.strangeclass.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@ApiModel("用户信息对象")
@Data
public class UserInformationVo {
    @ApiModelProperty("用户名")
    @Length(min = 1, message = "新用户名格式不正确")
    private String name;

    @ApiModelProperty("用户头像")
    private String image;

    @ApiModelProperty("用户信息")
    private String info;
}
