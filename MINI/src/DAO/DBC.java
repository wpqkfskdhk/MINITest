package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import DAO.SQL;

public class DBC {
    public static Connection DBConnect(){
        // DB에 접속정보 저장을 위한 Connection 객체 con선언
        Connection con = null;

        // DB에 접속할 계정정보
        String user = "ICIA";
        String password = "1111";

        // DB에 접속할 주소정보
        String url = "jdbc:oracle:thin:@localhost:1521:XE";

        try {
            // (1) 오라클 데이터베이스 드라이버(ojdbc8)
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // (2) 오라클 데이터베이스 접속 정보
            con = DriverManager.getConnection(url,user, password);

        } catch (ClassNotFoundException e) {
            System.out.println("DB접속 실패 : 드라이버 로딩 실패");
            throw new RuntimeException(e);
        } catch (SQLException e) {
            System.out.println("DB접속 실패: 접속 정보 오류");
            throw new RuntimeException(e);
        }
        return con;
    }
}
