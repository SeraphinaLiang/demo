package com.example.strangeclass.controller.api;

import com.example.strangeclass.controller.BaseController;
import com.example.strangeclass.dto.Reply;
import com.example.strangeclass.entity.User;
import com.example.strangeclass.service.HistoryCourseService;
import com.example.strangeclass.vo.CourseIdVo;
import com.example.strangeclass.vo.CourseListVo;
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

@Api(tags = "历史观看课程控制层")
@RestController
@RequestMapping("/historyCourse")
public class HistoryCourseController extends BaseController {

    @Autowired
    private HistoryCourseService historyCourseService;


    @ApiOperation(value = "加入一次历史观看记录",
            notes = "错误1:该课程不存在\n")
    @PostMapping(value = "/addHistoryCourse")
    public Reply addHistoryCourse(@Valid @RequestBody CourseIdVo courseIdVo,
                                  BindingResult result,
                                  HttpServletRequest request) throws Exception {
        validateError(result);

        User user = (User) request.getSession().getAttribute("user");
        historyCourseService.addHistoryCourse(courseIdVo, user);

        return new Reply<>(true, null);
    }


    @ApiOperation(value = "删除一次历史观看记录",
            notes = "错误1:该课程不存在\n")
    @PostMapping(value = "/deleteHistoryCourse")
    public Reply deleteHistoryCourse(@Valid @RequestBody CourseIdVo courseIdVo,
                                     BindingResult result,
                                     HttpServletRequest request) throws Exception {
        validateError(result);

        User user = (User) request.getSession().getAttribute("user");
        historyCourseService.deleteHistoryCourse(courseIdVo, user);

        return new Reply<>(true, null);
    }


    @ApiOperation(value = "查看历史观看记录")
    @PostMapping(value = "/getHistoryCourseList")
    public Reply<CourseListVo> getHistoryCourseList(HttpServletRequest request) throws Exception {

        User user = (User) request.getSession().getAttribute("user");

        return new Reply<>(true, historyCourseService.getHistoryCourseList(user));
    }
}
