package com.example.strangeclass.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("用户头像URL对象")
@Data
public class UserImageVo {

    @ApiModelProperty("用户头像URL")
    private String imageUrl;

    public UserImageVo(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
