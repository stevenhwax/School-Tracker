package Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.util.List;

@Entity (tableName = "course_table")
public class Course {

    @PrimaryKey(autoGenerate = true)
    private Integer courseId;
    private String courseName;
    private LocalDate courseStart;
    private LocalDate courseEnd;
    private String courseStatus;
    private String courseInstructor;
    private String instructorPhone;
    private String instructorEmail;
    private String courseNotes;
    private List<Integer> associatedAssessments;

    public Course(String courseName, LocalDate courseStart, LocalDate courseEnd, String courseStatus, String courseInstructor, String instructorPhone, String instructorEmail, String courseNotes, List<Integer> associatedAssessments) {
        this.courseName = courseName;
        this.courseStart = courseStart;
        this.courseEnd = courseEnd;
        this.courseStatus = courseStatus; //In Progress, Completed, Dropped, Plan to Take
        this.courseInstructor = courseInstructor;
        this.instructorPhone = instructorPhone;
        this.instructorEmail = instructorEmail;
        this.courseNotes = courseNotes;
        this.associatedAssessments = associatedAssessments;
    }

    public String getCourseNotes() {
        return courseNotes;
    }

    public void setCourseNotes(String courseNotes) {
        this.courseNotes = courseNotes;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public LocalDate getCourseStart() {
        return courseStart;
    }

    public void setCourseStart(LocalDate courseStart) {
        this.courseStart = courseStart;
    }

    public LocalDate getCourseEnd() {
        return courseEnd;
    }

    public void setCourseEnd(LocalDate courseEnd) {
        this.courseEnd = courseEnd;
    }

    public String getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(String courseStatus) {
        this.courseStatus = courseStatus;
    }

    public String getCourseInstructor() {
        return courseInstructor;
    }

    public void setCourseInstructor(String courseInstructor) {
        this.courseInstructor = courseInstructor;
    }

    public String getInstructorPhone() {
        return instructorPhone;
    }

    public void setInstructorPhone(String instructorPhone) {
        this.instructorPhone = instructorPhone;
    }

    public String getInstructorEmail() {
        return instructorEmail;
    }

    public void setInstructorEmail(String instructorEmail) {
        this.instructorEmail = instructorEmail;
    }

    public List<Integer> getAssociatedAssessments() {
        return associatedAssessments;
    }

    public void setAssociatedAssessments(List<Integer> associatedAssessments) {
        this.associatedAssessments = associatedAssessments;
    }

    public String toString() {
        return courseId + ": " + courseName;
    }

}
