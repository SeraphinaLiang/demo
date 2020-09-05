package com.example.strangeclass.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "history_course")
public class HistoryCourse extends BaseEntity {

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "course_id")
    private Course course;

    public HistoryCourse() {

    }

    public HistoryCourse(User user, Course course) {
        this.user = user;
        this.course = course;
    }
}
