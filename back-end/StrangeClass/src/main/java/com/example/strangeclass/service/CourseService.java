package com.example.strangeclass.service;

import com.example.strangeclass.dao.CourseRepository;
import com.example.strangeclass.entity.CollectionCourse;
import com.example.strangeclass.entity.Course;
import com.example.strangeclass.entity.User;
import com.example.strangeclass.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CollectionCourseService collectionCourseService;

    private static final String STUDENT = "Student";
    private static final String TEACHER = "Teacher";

    /**
     * 创建课程
     *
     * @param createCourseVo
     * @param user
     * @return
     */
    public CourseVo addCourse(CreateCourseVo createCourseVo, User user) throws Exception {

        if (!user.getRole().equals(TEACHER)) {
            throw new Exception("用户不是Teacher");
        }

        User userDB = userService.findByName(user.getName());

        Course courseDB = new Course();
        BeanUtils.copyProperties(createCourseVo, courseDB);
        courseDB.setTeacher(userDB);
        courseDB.setStatus(0);
        courseDB.createTime();
        courseRepository.save(courseDB);

        CourseVo courseVo = new CourseVo();
        BeanUtils.copyProperties(courseDB, courseVo);
        BeanUtils.copyProperties(courseDB.getTeacher(), courseVo.getTeacher());

        return courseVo;
    }

    /**
     * 删除课程
     *
     * @param deleteCourseVo
     * @param user
     * @return
     */
    public void deleteCourse(DeleteCourseVo deleteCourseVo, User user) throws Exception {
        Optional<Course> courseOptional = courseRepository.findById(deleteCourseVo.getId());
        if (!courseOptional.isPresent()) {
            throw new Exception("课程不存在");
        }

        Course courseDB = courseOptional.get();
        if (!courseDB.getTeacher().getId().equals(user.getId())) {
            throw new Exception("用户不是该课程的创建者");
        }

        courseRepository.delete(courseDB);
    }

    /**
     * 查看所有课程
     *
     * @return
     */
    public CourseListVo getAllCourse(User user) throws Exception {
        CourseListVo courseListVo = new CourseListVo();
        List<Course> list = courseRepository.findAll();
        for (Course course : list) {
            CourseVo courseVo = new CourseVo();
            BeanUtils.copyProperties(course, courseVo);
            BeanUtils.copyProperties(course.getTeacher(), courseVo.getTeacher());
            courseVo.setCollected(isCollected(course.getId(), user.getId()));
            courseListVo.getCourseVoList().add(courseVo);
        }

        return courseListVo;
    }

    /**
     * 课程开播或下播
     *
     * @param changeCourseStatusVo
     * @param userId
     * @return
     */
    public CourseVo changeCourseStatus(ChangeCourseStatusVo changeCourseStatusVo, Long userId) throws Exception {

        Optional<Course> courseOptional = courseRepository.findById(changeCourseStatusVo.getId());
        if (!courseOptional.isPresent()) {
            throw new Exception("课程标识错误");
        }

        Course courseDB = courseOptional.get();
        if (!courseDB.getTeacher().getId().equals(userId)) {
            throw new Exception("用户不是课程的创建者");
        }

        if (courseDB.getStatus() == 0) {
            courseDB.setStatus(1);
            courseDB.setClassId(changeCourseStatusVo.getClassId());
        } else {
            courseDB.setStatus(0);
            courseDB.setClassId(null);
        }
        courseDB.updateTime();
        courseRepository.save(courseDB);

        CourseVo courseVo = new CourseVo();
        BeanUtils.copyProperties(courseDB, courseVo);
        BeanUtils.copyProperties(courseDB.getTeacher(), courseVo.getTeacher());
        courseVo.setCollected(isCollected(courseDB.getId(), userId));

        return courseVo;
    }

    public Optional<Course> findById(Long courseId) {
        return courseRepository.findById(courseId);
    }

    public CourseVo changeCourseInformation(User user, CourseInformationVo courseInformationVo) throws Exception {
        if (!courseRepository.existsById(courseInformationVo.getId())) {
            throw new Exception("该课程不存在");
        }
        Optional<Course> optionalCourse = courseRepository.findById(courseInformationVo.getId());
        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            if (!course.getTeacher().getId().equals(user.getId())) {
                throw new Exception("您不是该课程的创建者");
            }
            if (courseInformationVo.getName() != null) {
                course.setName(courseInformationVo.getName());
            }
            if (courseInformationVo.getClassification() != null) {
                course.setClassification(courseInformationVo.getClassification());
            }
            if (courseInformationVo.getInfo() != null) {
                course.setInfo(courseInformationVo.getInfo());
            }
            courseRepository.save(course);

            CourseVo courseVo = new CourseVo();
            BeanUtils.copyProperties(course, courseVo);
            BeanUtils.copyProperties(course.getTeacher(), courseVo.getTeacher());
            courseVo.setCollected(isCollected(course.getId(), user.getId()));

            return courseVo;
        } else {
            throw new Exception("不存在该课程");
        }
    }

    public CourseListVo viewCourse(UserIdVo userIdVo) throws Exception {
        Optional<User> optionalUser = userService.findById(userIdVo.getId());
        if (!optionalUser.isPresent()) {
            throw new Exception("该用户不存在");
        }
        User user = optionalUser.get();
        if (!user.getRole().equals("Teacher")) {
            throw new Exception("该用户不是教师");
        }

        List<Course> courseList = courseRepository.findAllByTeacher(user);
        CourseListVo courseListVo = new CourseListVo();

        for (Course course : courseList) {
            CourseVo courseVo = new CourseVo();
            courseVo.setCollected(isCollected(course.getId(), user.getId()));
            BeanUtils.copyProperties(course, courseVo);
            BeanUtils.copyProperties(course.getTeacher(), courseVo.getTeacher());
            courseListVo.getCourseVoList().add(courseVo);
        }
        return courseListVo;

    }

    public Boolean isCollected(Long courseId, Long userId) throws Exception {
        Optional<User> optionalUser = userService.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new Exception("该用户不存在");
        }
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if (!optionalCourse.isPresent()) {
            throw new Exception("该课程不存在");
        }
        CollectionCourse collectionCourse = collectionCourseService.findByUserAndCourse(optionalUser.get(), optionalCourse.get());
        return collectionCourse != null;
    }

    public CourseListVoForAdmin getAllCourseForAdmin() {
        CourseListVoForAdmin courseListVoForAdmin = new CourseListVoForAdmin();
        List<CourseVoForAdmin> courseVoForAdminList = courseListVoForAdmin.getCourseVoForAdminList();
        List<Course> courseList = courseRepository.findAll();
        for (Course course : courseList) {
            CourseVoForAdmin courseVoForAdmin = new CourseVoForAdmin();
            BeanUtils.copyProperties(course, courseVoForAdmin);
            courseVoForAdminList.add(courseVoForAdmin);
        }

        return courseListVoForAdmin;
    }
}
