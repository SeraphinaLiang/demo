package com.example.strangeclass.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("统一返回对象")
@Data
public class Reply<T> implements Serializable {

    @ApiModelProperty("操作是否成功")
    private boolean successful;

    @ApiModelProperty("描述")
    private String message = "";

    @ApiModelProperty("数据")
    private T data;

    public Reply(boolean successful, T data) {
        this.successful = successful;
        this.data = data;
    }

    public Reply(boolean successful, String message) {
        this.successful = successful;
        this.message = message;
    }

    public Reply(boolean successful, String message, T data) {
        this.successful = successful;
        this.message = message;
        this.data = data;
    }

}
