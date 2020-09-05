package com.example.strangeclass.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel("课程开播接口入参对象")
@Data
public class ChangeCourseStatusVo {

    @ApiModelProperty("课程标识")
    @NotNull(message = "课程标识不能为空")
    private Long id;

    @ApiModelProperty("直播间标识")
    @NotNull(message = "直播间标识不能为空")
    private String classId;
}
