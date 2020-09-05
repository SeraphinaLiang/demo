package com.example.strangeclass.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel("删除课程接口入参对象")
@Data
public class DeleteCourseVo {

    @ApiModelProperty("课程标识")
    @NotNull(message = "课程标识不能为空")
    private Long id;
}