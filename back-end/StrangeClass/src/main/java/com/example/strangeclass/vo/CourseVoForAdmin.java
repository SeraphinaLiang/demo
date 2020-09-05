package com.example.strangeclass.vo;

import com.example.strangeclass.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("管理员查看课程对象")
@Data
public class CourseVoForAdmin {

    @ApiModelProperty("课程标识")
    private Long id;

    @ApiModelProperty("课程名")
    private String name;

    @ApiModelProperty("课程分类")
    private String classification;

    @ApiModelProperty("课程信息")
    private String info;

    @ApiModelProperty("创建者")
    private User teacher = new User();

    @ApiModelProperty("直播间标识")
    private String classId;

    @ApiModelProperty("课程状态")
    private Integer status;
}
