package com.example.strangeclass.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel("创建课程接口入参对象")
@Data
public class CreateCourseVo {

    @ApiModelProperty("课程名")
    @NotBlank(message = "课程名不能为空")
    private String name;

    @ApiModelProperty("课程分类")
    @NotBlank(message = "课程分类不能为空")
    private String classification;
}
