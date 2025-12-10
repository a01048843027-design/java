package manager;

import model.Course;
import util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseManager {
    private static CourseManager instance;
    private Map<String, Course> courseMap = new HashMap<>();

    private CourseManager() {
        loadCoursesFromDB();
    }

    public static CourseManager getInstance() {
        if (instance == null) instance = new CourseManager();
        return instance;
    }

    // DB에서 강의 목록 불러오기
    public void loadCoursesFromDB() {
        courseMap.clear();
        String sql = "SELECT * FROM courses";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Course c = new Course(rs.getString("subject_name"));
                c.setCourseCode(rs.getString("course_code"));
                c.setProfessor(rs.getString("professor"));
                c.setRoom(rs.getString("room"));
                c.setTime(rs.getString("class_time"));
                c.setMaxStudents(rs.getInt("max_students"));

                courseMap.put(c.getSubjectName(), c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 교수님 강의 목록 가져오기 (리스트가 비어있으면 다시 로드)
    public List<Course> getProfessorCourses(String professorName) {
        if (courseMap.isEmpty()) loadCoursesFromDB(); // 안전장치 추가

        List<Course> list = new ArrayList<>();
        for (Course c : courseMap.values()) {
            if (professorName.equals(c.getProfessor())) {
                list.add(c);
            }
        }
        return list;
    }

    // [NEW] 실제 수강생 수 세기 (DB 연동)
    public int getCurrentStudentCount(String courseCode) {
        String sql = "SELECT COUNT(*) FROM enrollments WHERE course_code = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, courseCode);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Course getCourse(String subjectName) { return courseMap.get(subjectName); }

    // 강의 정보 업데이트
    public void updateCourse(String code, String newRoom, String newTime, int newMax) {
        String sql = "UPDATE courses SET room = ?, class_time = ?, max_students = ? WHERE course_code = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newRoom);
            pstmt.setString(2, newTime);
            pstmt.setInt(3, newMax);
            pstmt.setString(4, code);
            pstmt.executeUpdate();
            loadCoursesFromDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}