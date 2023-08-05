package com.aptech.project2.DAO;

import com.aptech.project2.Generic.IGeneric;
import com.aptech.project2.Model.ConnectDatabase;
import com.aptech.project2.Model.Staff;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class StaffDAO implements IGeneric<Staff> {
    public static Staff staffSession;

    private Connection con = ConnectDatabase.getInstance().getConnect();
    public static StaffDAO getInstance(){
        return new StaffDAO();
    }

    @Override
    public void insert(Staff staff) {
        String sql = "INSERT staff(username, password, staff_name, staff_phone, staff_role) VALUES(?,?,?,?,?)";
        try {
            PreparedStatement ptm = con.prepareStatement(sql);
            ptm.setString(1, staff.getUsername());
            ptm.setString(2, staff.getPassword());
            ptm.setString(3, staff.getFullName());
            ptm.setString(4, staff.getPhone());
            ptm.setString(5, staff.getRole());
            ptm.executeUpdate();
        } catch (SQLException e) {
           e.printStackTrace();
        }
        ConnectDatabase.getInstance().closeConnect(con);
    }


    public void update(Staff customer, String id) {

    }

    @Override
    public void delete(Staff staff) {
        String sql = "DELETE FROM staff WHERE id = ?";
        try {
            PreparedStatement ptm = con.prepareStatement(sql);
            ptm.setInt(1, staff.getId());
            ptm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ObservableList<Staff> getAll() {
        ObservableList<Staff> staffs = FXCollections.observableArrayList();
        String sql = "SELECT * FROM staff";
        try {
            PreparedStatement ptm = con.prepareStatement(sql);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()){
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String fullName = rs.getString("staff_name");
                String phone = rs.getString("staff_phone");
                String role = rs.getString("staff_role");
                Staff staff = new Staff(id, username, password, fullName, phone, role);
                staffs.add(0, staff);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staffs;
    }

    public Staff findUserName(String staffUserName){
        Staff staff = null;
        String sql = "SELECT * FROM staff WHERE username = ?";
        try {
            PreparedStatement ptm = con.prepareStatement(sql);
            ptm.setString(1, staffUserName);
            ResultSet rs = ptm.executeQuery();
            if(rs.next()){
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String fullName = rs.getString("staff_name");
                String phone = rs.getString("staff_phone");
                String role = rs.getString("staff_role");
                staff = new Staff(id, username, password, fullName, phone, role);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ConnectDatabase.getInstance().closeConnect(con);
        return staff;
    }

    public Staff finById(int id){
        Staff staff = null;
        String sql = "SELECT * FROM staff WHERE id = ?";
        try {
            PreparedStatement ptm = con.prepareStatement(sql);
            ptm.setInt(1, id);
            ResultSet rs = ptm.executeQuery();
            if(rs.next()){
                int staffId = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String fullName = rs.getString("staff_name");
                String phone = rs.getString("staff_phone");
                String role = rs.getString("staff_role");
                staff = new Staff(id, username, password, fullName, phone, role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staff;
    }

}
