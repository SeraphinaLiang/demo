package com.example.strangeclass.controller.api;

import com.example.strangeclass.controller.BaseController;
import com.example.strangeclass.dto.Reply;
import com.example.strangeclass.entity.User;
import com.example.strangeclass.service.ChatRecordService;
import com.example.strangeclass.vo.ChatRecordVo;
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

@Api(tags = "聊天记录控制层")
@RestController
@RequestMapping("/chatRecord")
public class ChatRecordController extends BaseController {

    @Autowired
    private ChatRecordService chatRecordService;


    @ApiOperation(value = "保存聊天记录",
            notes = "错误1:聊天记录所在课程不存在\n")
    @PostMapping(value = "/saveChatRecord")
    public Reply saveChatRecord(@Valid @RequestBody ChatRecordVo chatRecordVo,
                                BindingResult result,
                                HttpServletRequest request) throws Exception {
        validateError(result);

        User user = (User) request.getSession().getAttribute("user");
        chatRecordService.saveChatRecord(user.getName(), chatRecordVo);

        return new Reply<>(true, null);
    }
}
