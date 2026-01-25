package com.learn.demo.enrollment;

public class EnrollmentDTO {

    private final int enrollmentId;
    private final int userId;       // just the ID, not the full User object
    private final int courseId;     // just the ID, not the full Course object
    private final String courseName; // optional
    private final String status;    // EnrollmentStatus as string

    // Constructor that converts Enrollment -> DTO
    public EnrollmentDTO(Enrollment enrollment) {
        this.enrollmentId = enrollment.getEnrollmentId();
        this.userId = enrollment.getUser().getUserId();
        this.courseId = enrollment.getCourse().getCourseId();
        this.courseName = enrollment.getCourse().getDescription();
        this.status = enrollment.getStatus().name();
    }

    // Getters (no setters needed for API read-only)
    public int getEnrollmentId() { return enrollmentId; }
    public int getUserId() { return userId; }
    public int getCourseId() { return courseId; }
    public String getCourseName() { return courseName; }
    public String getStatus() { return status; }
}
