package com.example.strangeclass.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "tic_account")
public class TicAccount extends BaseEntity {

    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_token", length = 500)
    private String userToken;

    @Column(name = "who_used", length = 20)
    private String whoUsed;

    public TicAccount() {

    }

    public TicAccount(String userId, String userToken) {
        this.userId = userId;
        this.userToken = userToken;
    }
}
