package com.aptech.project2.DAO;

import com.aptech.project2.Model.ConnectDatabase;
import com.aptech.project2.Model.Order;
import com.aptech.project2.Model.Product;
import com.aptech.project2.Model.Staff;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class OrderDao {
    private Connection con = ConnectDatabase.getInstance().getConnect();
    public static OrderDao getInstance(){
        return new OrderDao();
    }

    public void insertOrder(Order order){
        String sql = "INSERT INTO orders VALUES(?,?,?,?,?,?)";
        try {
            PreparedStatement ptm = con.prepareStatement(sql);
            ptm.setString(1,order.getId());
            ptm.setInt(2, order.getStaff().getId());
            ptm.setDouble(3, order.getSubTotal());
            ptm.setDouble(4, order.getDiscount());
            ptm.setDouble(5, order.getTotal());
            ptm.setTimestamp(6, Timestamp.valueOf(order.getCreateDate()));
            ptm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ConnectDatabase.getInstance().closeConnect(con);
    }

    public ObservableList<Order> getList(String keyword){
        ObservableList<Order> orders = FXCollections.observableArrayList();
//        String sql = "SELECT * FROM orders";
        String condition = "";
        if(!keyword.isEmpty()){
            condition = " AND id LIKE ? OR staff_id LIKE ? OR create_at LIKE ?";
        }
        String sql = "SELECT * FROM orders WHERE 1=1" + condition;

        try {
            PreparedStatement ptm = con.prepareStatement(sql);
            if(!keyword.isEmpty()){
                ptm.setString(1, "%"+keyword+"%");
                ptm.setString(2, "%"+keyword+"%");
                ptm.setString(3, "%"+keyword+"%");
            }
            ResultSet rs = ptm.executeQuery();
            while (rs.next()){
                String id = rs.getString("id");
                int staffId = rs.getInt("staff_id");
                double subTotal = rs.getDouble("subtotal");
                double discount = rs.getDouble("discount");
                double total = rs.getDouble("total");
                LocalDateTime createDateTime = rs.getTimestamp("create_at").toLocalDateTime();
                Staff staff = StaffDAO.getInstance().finById(staffId);
                Order order = new Order(id, staff, subTotal, discount, total, createDateTime);
                orders.add(0,order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public int countByProduct(Product product){
        int count = 0;
        String sql = "SELECT COUNT(*) FROM orderdetail WHERE  pro_id= ?";
        try {
            PreparedStatement ptm = con.prepareStatement(sql);
            ptm.setString(1, product.getId());
            ResultSet rs = ptm.executeQuery();
            if(rs.next()){
                count = rs.getInt("COUNT(*)");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return count;
    }

    public int countByStaff(Staff staff){
        int count = 0;
        String sql = "SELECT COUNT(*) FROM orders WHERE  staff_id= ?";
        try {
            PreparedStatement ptm = con.prepareStatement(sql);
            ptm.setInt(1, staff.getId());
            ResultSet rs = ptm.executeQuery();
            if(rs.next()){
                count = rs.getInt("COUNT(*)");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ConnectDatabase.getInstance().closeConnect(con);
        return count;
    }

    public int countOrders(){
        int count = 0;
        String sql = "SELECT COUNT(*) FROM orders";
        try {
            PreparedStatement ptm = con.prepareStatement(sql);
            ResultSet rs = ptm.executeQuery();
            if(rs.next()){
                count = rs.getInt("COUNT(*)");
            };
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ConnectDatabase.getInstance().closeConnect(con);
        return count;
    }

    public double getTotalToday(){
        double total = 0;
        String sql = "SELECT SUM(total) FROM orders WHERE DATE(create_at) = CURDATE()";
        try {
            PreparedStatement ptm = con.prepareStatement(sql);
            ResultSet rs = ptm.executeQuery();
            if(rs.next()){
                total = rs.getInt("SUM(total)");
            };
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ConnectDatabase.getInstance().closeConnect(con);
        return total;
    }






}
