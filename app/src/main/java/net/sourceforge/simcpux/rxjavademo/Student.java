package net.sourceforge.simcpux.rxjavademo;

import java.util.List;

/**
 * Created by bluesky on 16/11/3.
 */

public class Student {
    String name;
    List<Course> courseList;

    public Student() {
    }

    public Student(String name, List<Course> courseList) {
        this.name = name;
        this.courseList = courseList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }
}
