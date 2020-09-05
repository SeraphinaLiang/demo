package com.example.strangeclass.entity;

import com.example.strangeclass.util.MyTimestamp;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;

@Slf4j
@Data
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 64)
    private Long id;

    @Column(name = "create_at")
    private Timestamp createAt;

    @Column(name = "create_by", length = 20)
    private BigInteger createBy;

    @Column(name = "update_at")
    private Timestamp updateAt;

    @Column(name = "update_by", length = 20)
    private BigInteger updateBy;

    @Column(name = "is_del")
    private Boolean isDel = false;

    public void createTime() {
        this.setCreateAt(MyTimestamp.getTimeGmt0800());
    }

    public void updateTime() {
        this.setUpdateAt(MyTimestamp.getTimeGmt0800());
    }
}

