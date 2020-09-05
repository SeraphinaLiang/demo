package com.example.strangeclass.controller.api;

import com.example.strangeclass.controller.BaseController;
import com.example.strangeclass.dto.Reply;
import com.example.strangeclass.entity.User;
import com.example.strangeclass.service.UserService;
import com.example.strangeclass.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Api(tags = "用户控制层")
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    private static final String ADMINISTRATOR = "Admin";

    @Autowired
    private UserService userService;


    @ApiOperation(value = "用户注册",
            notes = "错误1:用户名已被注册\n" +
                    "错误2:填写的用户名或密码为空\n" +
                    "错误3:角色参数错误\n")
    @PostMapping(value = "/register")
    public Reply register(@Valid @RequestBody RegisterVo registerVo,
                          BindingResult result) throws Exception {
        validateError(result);

        userService.addUser(registerVo);
        return new Reply<>(true, null);
    }


    @ApiOperation(value = "用户登录",
            notes = "错误1:用户名或密码错误\n" +
                    "错误2:填写的用户名或密码为空\n" +
                    "错误3:该用户已登录\n" +
                    "错误4:SDK账号无法分配\n")
    @PostMapping(value = "/login")
    public Reply<ReplyLoginVo> login(@Valid @RequestBody LoginVo loginVo,
                                     BindingResult result,
                                     HttpServletRequest request,
                                     HttpServletResponse response) throws Exception {
        validateError(result);

        User user = (User) request.getSession().getAttribute("user");
        if (user != null && !user.getName().equals(loginVo.getName())) {
            logout(request, response);
        }

        return new Reply<>(true, userService.login(loginVo, request, response));

    }


    @ApiOperation(value = "用户登出",
            notes = "错误1:用户名不存在\n" +
                    "错误2:该用户已登出\n")
    @PostMapping(value = "/logout")
    public Reply logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Cookie cookie = new Cookie("JSESSIONID", request.getSession().getId());
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        userService.logout(request.getSession());
        return new Reply<>(true, null);
    }


    @ApiOperation(value = "查看任意用户信息",
            notes = "错误1:该用户不存在\n" +
                    "错误2:用户标识为空\n")
    @PostMapping(value = "/viewInformation")
    public Reply<UserVo> viewInformation(@Valid @RequestBody UserIdVo userIdVo,
                                         BindingResult result) throws Exception {
        validateError(result);

        return new Reply<>(true, userService.viewInformation(userIdVo));

    }

    @ApiOperation(value = "用户密码修改",
            notes = "错误1:用户密码为空\n" +
                    "错误2:用户密码长度为0\n" +
                    "错误3:旧密码错误\n")
    @PostMapping(value = "/changePassword")
    public Reply changePassword(@Valid @RequestBody PasswordVo passwordVo,
                                BindingResult result,
                                HttpServletRequest request) throws Exception {
        validateError(result);

        User user = (User) request.getSession().getAttribute("user");
        userService.changePassword(user.getName(), passwordVo);
        return new Reply<>(true, null);
    }


    @ApiOperation(value = "用户信息修改",
            notes = "错误1:用户名已存在\n" +
                    "错误2:用户名格式不正确\n" +
                    "错误3:头像格式不正确\n")
    @PostMapping(value = "/changeInformation")
    public Reply<UserVo> changeInformation(@Valid @RequestBody UserInformationVo userInformationVo,
                                           BindingResult result,
                                           HttpServletRequest request) throws Exception {
        validateError(result);

        return new Reply<>(true, userService.changeInformation(userInformationVo, request));
    }


    /**
     * 接收转发图片
     *
     * @param request
     * @param imageFile
     * @return
     */
    @ApiOperation(value = "上传头像图片")
    @PostMapping(value = "/uploadImage")
    public Reply<UserImageVo> handleUploadImage(@RequestParam("image") MultipartFile imageFile,
                                                HttpServletRequest request) throws Exception {
        return new Reply<>(true, "上传成功", userService.handleUploadImage(imageFile, request));
    }


    @ApiOperation(value = "管理员查看所有用户信息")
    @PostMapping(value = "/viewAllInformationForAdmin")
    public Reply<UserListVoForAdmin> viewAllInformationForAdmin(HttpServletRequest request) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        if (!user.getRole().equals(ADMINISTRATOR)) {
            throw new Exception("没有管理员权限不能调用");
        }
        return new Reply<>(true, userService.viewAllInformationForAdmin());
    }
}
