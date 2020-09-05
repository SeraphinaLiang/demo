package com.example.strangeclass.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@ApiModel("管理员查看所有课程对象")
@Data
public class CourseListVoForAdmin {

    @ApiModelProperty("用户列表")
    private List<CourseVoForAdmin> courseVoForAdminList = new ArrayList<>();
}
