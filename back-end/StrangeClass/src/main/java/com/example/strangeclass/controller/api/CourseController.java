package com.example.strangeclass.controller.api;

import com.example.strangeclass.controller.BaseController;
import com.example.strangeclass.dto.Reply;
import com.example.strangeclass.entity.User;
import com.example.strangeclass.service.CourseService;
import com.example.strangeclass.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Api(tags = "课程控制层")
@RestController
@RequestMapping("/course")
public class CourseController extends BaseController {

    private static final String ADMINISTRATOR = "Admin";
    @Autowired
    private CourseService courseService;


    @ApiOperation(value = "创建课程",
            notes = "错误1:用户不是Teacher\n" +
                    "错误2:课程名或分类为空\n")
    @PostMapping(value = "/createCourse")
    public Reply<CourseVo> createCourse(@Valid @RequestBody CreateCourseVo createCourseVo,
                                        BindingResult result,
                                        HttpServletRequest request) throws Exception {
        validateError(result);

        User user = (User) request.getSession().getAttribute("user");
        return new Reply<>(true, courseService.addCourse(createCourseVo, user));
    }


    @ApiOperation(value = "删除课程",
            notes = "错误1:用户不是该课程的创建者\n" +
                    "错误2:课程不存在\n")
    @PostMapping(value = "/deleteCourse")
    public Reply deleteCourse(@Valid @RequestBody DeleteCourseVo deleteCourseVo,
                              BindingResult result,
                              HttpServletRequest request) throws Exception {
        validateError(result);

        User user = (User) request.getSession().getAttribute("user");
        courseService.deleteCourse(deleteCourseVo, user);

        return new Reply(true, null);
    }


    @ApiOperation(value = "查看所有课程")
    @PostMapping(value = "/getAllCourse")
    public Reply<CourseListVo> getAllCourse(HttpServletRequest request) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        return new Reply<>(true, courseService.getAllCourse(user));
    }


    @ApiOperation(value = "课程开播或下播",
            notes = "错误1:用户不是课程的创建者\n" +
                    "错误2:课程标识错误\n")
    @PostMapping(value = "/openCourseLive")
    public Reply<CourseVo> changeCourseStatus(@Valid @RequestBody ChangeCourseStatusVo changeCourseStatusVo,
                                              BindingResult result,
                                              HttpServletRequest request) throws Exception {
        validateError(result);

        User user = (User) request.getSession().getAttribute("user");
        return new Reply<>(true, courseService.changeCourseStatus(changeCourseStatusVo, user.getId()));
    }


    @ApiOperation(value = "课程信息修改",
            notes = "错误1:不存在该课程\n" +
                    "错误2:课程ID为空\n" +
                    "错误3:课程名字格式错误\n" +
                    "错误4:课程分类格式错误\n" +
                    "错误5:您不是该课程的创建者\n" +
                    "错误6:该课程不存在\n")
    @PostMapping(value = "/changeUserInformation")
    public Reply<CourseVo> changeCourseInformation(@Valid @RequestBody CourseInformationVo courseInformationVo,
                                                   BindingResult result,
                                                   HttpServletRequest request) throws Exception {
        validateError(result);

        User user = (User) request.getSession().getAttribute("user");
        return new Reply<>(true, courseService.changeCourseInformation(user, courseInformationVo));
    }


    @ApiOperation(value = "教师查看创建课程",
            notes = "错误1:该用户不存在\n" +
                    "错误2:该用户不是教师\n")
    @PostMapping(value = "/viewCourse")
    public Reply<CourseListVo> viewCourse(@Valid @RequestBody UserIdVo userIdVo,
                                          BindingResult result) throws Exception {
        validateError(result);

        return new Reply<>(true, courseService.viewCourse(userIdVo));

    }

    @ApiOperation(value = "管理员查看所有课程")
    @PostMapping(value = "/getAllCourseForAdmin")
    public Reply<CourseListVoForAdmin> viewAllInformationForAdmin(HttpServletRequest request) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        if (!user.getRole().equals(ADMINISTRATOR)) {
            throw new Exception("没有管理员权限不能调用");
        }
        return new Reply<>(true, courseService.getAllCourseForAdmin());
    }
}
