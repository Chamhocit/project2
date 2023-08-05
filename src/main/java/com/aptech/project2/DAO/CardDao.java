package com.aptech.project2.DAO;

import com.aptech.project2.Model.Card;
import com.aptech.project2.Model.ConnectDatabase;
import com.aptech.project2.Model.Product;
import com.aptech.project2.Model.Staff;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CardDao {
    public static CardDao getInstance(){
        return new CardDao();
    }
    private Connection con = ConnectDatabase.getInstance().getConnect();

    public void insertCard(Card card){
        String sql = "INSERT INTO card(staff_id, pro_id, pro_quantity, pro_price) VALUES(?,?,?,?)";
        try {
            PreparedStatement ptm = con.prepareStatement(sql);
            ptm.setInt(1, card.getStaff().getId());
            ptm.setString(2, card.getProduct().getId());
            ptm.setInt(3, card.getProQuantity());
            ptm.setDouble(4, card.getProPrice());
            ptm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ConnectDatabase.getInstance().closeConnect(con);
    }

    public ObservableList<Card> getAllByStaff(Staff staff){
        ObservableList<Card> cards = FXCollections.observableArrayList();
        String sql = "SELECT * FROM card WHERE staff_id = ?";
        try {
            PreparedStatement ptm = con.prepareStatement(sql);
            ptm.setInt(1, staff.getId());
            ResultSet rs = ptm.executeQuery();
            while (rs.next()){
                int id = rs.getInt("id");
                String proId = rs.getString("pro_id");
                Product product = ProductDAO.getInstance().findById(proId);
                double proPrice = rs.getDouble("pro_price");
                int proQuantity = rs.getInt("pro_quantity");
                Card card = new Card(id, staff, product, proQuantity, proPrice);
                cards.add(card);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ConnectDatabase.getInstance().closeConnect(con);
        return cards;
    }

    public void deleteOneCardByProId(Card card){
        String sql = "DELETE FROM card WHERE pro_id = ?";
        try {
            PreparedStatement ptm = con.prepareStatement(sql);
            ptm.setString(1, card.getProduct().getId());
            ptm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ConnectDatabase.getInstance().closeConnect(con);
    }

    public void deleteByStaff(Staff staff){
        String sql = "DELETE FROM card WHERE staff_id = ?";
        try {
            PreparedStatement ptm = con.prepareStatement(sql);
            ptm.setInt(1, staff.getId());
            ptm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ConnectDatabase.getInstance().closeConnect(con);
    }

    public int findByProId(Product product){
        int count = 0;
        String sql = "SELECT COUNT(*) FROM card WHERE pro_id=?";
        try {
            PreparedStatement ptm = con.prepareStatement(sql);
            ptm.setString(1,product.getId());
            ResultSet rs = ptm.executeQuery();
            if(rs.next()){
                count = rs.getInt("COUNT(*)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ConnectDatabase.getInstance().closeConnect(con);
        return count;
    }

    public void updateByProID(Product product, int quantity){
        String sql = "UPDATE card SET pro_quantity =pro_quantity+? WHERE pro_id = ?";
        try {
            PreparedStatement ptm = con.prepareStatement(sql);
            ptm.setInt(1, quantity);
            ptm.setString(2, product.getId());
            ptm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
