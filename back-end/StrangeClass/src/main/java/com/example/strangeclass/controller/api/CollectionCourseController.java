package com.example.strangeclass.controller.api;

import com.example.strangeclass.controller.BaseController;
import com.example.strangeclass.dto.Reply;
import com.example.strangeclass.entity.User;
import com.example.strangeclass.service.CollectionCourseService;
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

@Api(tags = "收藏课程控制层")
@RestController
@RequestMapping("/collectionCourse")
public class CollectionCourseController extends BaseController {

    @Autowired
    private CollectionCourseService collectionCourseService;


    @ApiOperation(value = "收藏一个课程",
            notes = "错误1:该课程不存在\n" +
                    "错误2:该用户已经收藏过该课程\n")
    @PostMapping(value = "/addCollectionCourse")
    public Reply addCollectionCourse(@Valid @RequestBody CourseIdVo courseIdVo,
                                     BindingResult result,
                                     HttpServletRequest request) throws Exception {
        validateError(result);

        User user = (User) request.getSession().getAttribute("user");
        collectionCourseService.addCollectionCourse(courseIdVo, user);

        return new Reply<>(true, null);
    }


    @ApiOperation(value = "查看所有收藏课程")
    @PostMapping(value = "/getCollectionCourseList")
    public Reply<CourseListVo> getCollectionCourseList(HttpServletRequest request) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        return new Reply<>(true, collectionCourseService.getCollectionCourseList(user.getName()));
    }


    @ApiOperation(value = "取消收藏课程",
            notes = "错误1:该课程已不存在\n" +
                    "错误2:该课程已不在用户收藏列表\n")
    @PostMapping(value = "/cancelCollectionCourse")
    public Reply cancelCollectionCourse(@Valid @RequestBody CourseIdVo courseIdVo,
                                        BindingResult result,
                                        HttpServletRequest request) throws Exception {
        validateError(result);

        User user = (User) request.getSession().getAttribute("user");
        collectionCourseService.cancelCollectionCourse(courseIdVo, user);

        return new Reply<>(true, null);
    }
}
