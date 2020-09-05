package com.example.strangeclass.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "collection_course")
public class CollectionCourse extends BaseEntity {

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "course_id")
    private Course course;

    public CollectionCourse() {

    }

    public CollectionCourse(User user, Course course) {
        this.user = user;
        this.course = course;
    }
}
