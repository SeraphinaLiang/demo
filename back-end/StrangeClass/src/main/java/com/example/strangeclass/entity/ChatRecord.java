package com.example.strangeclass.entity;

import lombok.Data;

import javax.persistence.*;


@Data
@Entity
@Table(name = "chat_record")
public class ChatRecord extends BaseEntity {

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "content", nullable = false)
    private String content;

    public ChatRecord() {

    }

    public ChatRecord(User user, Course course, String content) {
        this.user = user;
        this.course = course;
        this.content = content;
    }

}
