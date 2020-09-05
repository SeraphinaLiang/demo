package com.example.strangeclass.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel("课程Id对象")
@Data
public class CourseIdVo {

    @ApiModelProperty("课程标识")
    @NotNull(message = "课程标识不能为空")
    private Long courseId;
}
