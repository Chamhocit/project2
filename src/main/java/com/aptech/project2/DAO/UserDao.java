package com.aptech.project2.DAO;

import com.aptech.project2.Model.ConnectDatabase;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    public static UserDao getInstance(){
        return new UserDao();
    }
    private Connection con = ConnectDatabase.getInstance().getConnect();
    public static String userName;
    public boolean checkLogin(String username, String password){
        boolean f = false;
        String sql = "SELECT email, password FROM tbluser WHERE BINARY email = ?";
        try {
            PreparedStatement ptm = con.prepareStatement(sql);
            ptm.setString(1, username);
            ResultSet rs = ptm.executeQuery();
            if(rs.next()){
                String email = rs.getString("email");
                String pass = rs.getString("password");
                if(BCrypt.checkpw(password, pass)){
                    f = true;
                }
            }
            ConnectDatabase.getInstance().closeConnect(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return f;
    }


}
