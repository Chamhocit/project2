package com.aptech.project2.DAO;

import com.aptech.project2.Model.ConnectDatabase;
import com.aptech.project2.Model.Order;
import com.aptech.project2.Model.OrderDetail;
import com.aptech.project2.Model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderDetailDao {
    private Connection con = ConnectDatabase.getInstance().getConnect();
    public static OrderDetailDao getInstance(){
        return new OrderDetailDao();
    }
    public void insertOrdDetail(OrderDetail orderDetail){
        String sql = "INSERT INTO orderdetail(ord_id, pro_id, pro_price, pro_quantity) VALUES(?,?,?,?)";
        try {
            PreparedStatement ptm = con.prepareStatement(sql);
            ptm.setString(1, orderDetail.getOrder().getId());
            ptm.setString(2, orderDetail.getProduct().getId());
            ptm.setDouble(3, orderDetail.getPrice());
            ptm.setInt(4, orderDetail.getQuantity());
            ptm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ConnectDatabase.getInstance().closeConnect(con);
    }

    public ObservableList<OrderDetail> findByOrderId(Order order){
        ObservableList<OrderDetail> orderDetails = FXCollections.observableArrayList();
        String sql = "SELECT * FROM orderdetail WHERE ord_id = ?";
        try {
            PreparedStatement ptm = con.prepareStatement(sql);
            ptm.setString(1, order.getId());
            ResultSet rs = ptm.executeQuery();
            while (rs.next()){
                int id = rs.getInt("id");
                String proId = rs.getString("pro_id");
                Product product = ProductDAO.getInstance().findById(proId);
                double price = rs.getDouble("pro_price");
                int quantity = rs.getInt("pro_quantity");
                OrderDetail orderDetail = new OrderDetail(id, order, product, quantity, price);
                orderDetails.add(orderDetail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderDetails;
    }

}
