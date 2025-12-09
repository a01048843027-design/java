package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    // MySQL 접속 정보 (비밀번호 1234 맞는지 확인!)
    private static final String URL = "jdbc:mysql://localhost:3306/haksa_db?serverTimezone=UTC&useUnicode=true&characterEncoding=utf8";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    static {
        try {
            // 드라이버 로딩 (기름통 연결)
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}