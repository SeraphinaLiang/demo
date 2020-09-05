package com.example.strangeclass.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "course")
public class Course extends BaseEntity {

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @Column(name = "classification", nullable = false)
    private String classification;

    @Column(name = "info")
    private String info;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
    @JoinColumn(name = "teacher_id")
    private User teacher;

    @Column(name = "class_id")
    private String classId;

    @Column(name = "status", length = 10, nullable = false)
    private Integer status;

    @JsonIgnore
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<HistoryCourse> historyCourseList;

    @JsonIgnore
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<CollectionCourse> collectionCourseList;

    @JsonIgnore
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<ChatRecord> chatRecordList;

    public Course() {

    }

    public Course(String name, String classification, User teacher, Integer status) {
        this.name = name;
        this.classification = classification;
        this.teacher = teacher;
        this.status = status;
    }
}
