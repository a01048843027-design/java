package model;

public class Student {
    private boolean selected;
    private String id;
    private String name;
    private String major;
    private String status;
    private String attendance;
    private String note;

    public Student(String id, String name, String major, String status, String attendance, String note) {
        this.selected = false;
        this.id = id;
        this.name = name;
        this.major = major;
        this.status = status;
        this.attendance = attendance;
        this.note = note;
    }

    public boolean isSelected() { return selected; }
    public void setSelected(boolean selected) { this.selected = selected; }
    public String getId() { return id; }
    public String getName() { return name; }
    public String getMajor() { return major; }
    public String getStatus() { return status; }
    public String getAttendance() { return attendance; }
    public void setAttendance(String attendance) { this.attendance = attendance; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}