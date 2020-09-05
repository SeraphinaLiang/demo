package com.example.strangeclass.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@ApiModel("课程信息对象")
@Data
public class CourseInformationVo {

    @ApiModelProperty("所修改课程的标识")
    @NotNull(message = "所修改课程的标识为空")
    private Long id;

    @ApiModelProperty("新课程名")
    @Length(min = 1, message = "新课程名为空")
    private String name;

    @ApiModelProperty("新课程分类")
    @Length(min = 1, message = "新课程分类为空")
    private String classification;

    @ApiModelProperty("新课程信息")
    private String info;
}
