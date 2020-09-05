package com.example.strangeclass.vo;

import com.example.strangeclass.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@ApiModel("管理员查看所有用户信息对象")
@Data
public class UserListVoForAdmin {

    @ApiModelProperty("用户列表")
    private List<User> userList = new ArrayList<>();
}
