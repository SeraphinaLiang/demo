package com.example.strangeclass.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@ApiModel("课程列表对象")
@Data
public class CourseListVo {

    @ApiModelProperty("课程列表")
    private List<CourseVo> courseVoList = new ArrayList<>();
}
