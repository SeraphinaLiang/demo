package com.example.strangeclass.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel("聊天记录对象")
@Data
public class ChatRecordVo {

    @ApiModelProperty("课程")
    @NotNull(message = "课程不能为空")
    private Long courseId;

    @ApiModelProperty("聊天信息")
    @NotNull(message = "聊天信息不能为空")
    private String content;
}
