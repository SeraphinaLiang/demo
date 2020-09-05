package com.example.strangeclass.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "user")
public class User extends BaseEntity {

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @Column(name = "password", length = 20, nullable = false)
    private String password;

    @Column(name = "image")
    private String image;

    @Column(name = "status", length = 10, nullable = false)
    private Integer status;

    @Column(name = "role", length = 20, nullable = false)
    private String role;

    @Column(name = "info")
    private String info;

    @JsonIgnore
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    private List<Course> courseList;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<HistoryCourse> historyCourseList;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CollectionCourse> collectionCourseList;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ChatRecord> chatRecordList;

    public User() {

    }

    public User(String name, String password, Integer status, String role) {
        this.name = name;
        this.password = password;
        this.status = status;
        this.role = role;
    }
}
