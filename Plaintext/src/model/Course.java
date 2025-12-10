package model;

import java.time.LocalDate;

public class Course {
    private String courseCode;
    private String subjectName;
    private String professor;
    private String room;
    private String time;
    private int maxStudents;
    private LocalDate midtermDate;

    public Course(String subjectName) {
        this.subjectName = subjectName;
    }

    // --- Getter & Setter (이것들이 없어서 SchedulePanel이 오류를 뿜었던 겁니다) ---

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public String getProfessor() { return professor; }
    public void setProfessor(String professor) { this.professor = professor; }

    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public int getMaxStudents() { return maxStudents; }
    public void setMaxStudents(int maxStudents) { this.maxStudents = maxStudents; }

    public LocalDate getMidtermDate() { return midtermDate; }
    public void setMidtermDate(LocalDate midtermDate) { this.midtermDate = midtermDate; }
}