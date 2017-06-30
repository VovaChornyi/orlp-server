package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Course;
import com.softserve.academy.spaced.repetition.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    public Collection<Course> getAllCourses() {
        Collection<Course> courses = new ArrayList<>();
        courseRepository.findAll().forEach(courses::add);
        return courses;
    }

    public Course getCourse(Long id) {
        return courseRepository.findOne(id);
    }

    public void addCourse(Course course) {
        courseRepository.save(course);
    }

    public void updateCourse(Long id, Course course) {
        courseRepository.save(course);
    }

    public void deleteCourse(Long id) {
        courseRepository.delete(id);
    }
}
