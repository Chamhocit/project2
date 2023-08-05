package com.aptech.project2.DAO;

import com.aptech.project2.Generic.IGeneric;
import com.aptech.project2.Model.Category;
import com.aptech.project2.Model.ConnectDatabase;
import com.aptech.project2.Model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ProductDAO implements IGeneric<Product> {
    public static ProductDAO getInstance(){
        return new ProductDAO();
    }
    public static String pathImage;
    private Connection con = ConnectDatabase.getInstance().getConnect();
    @Override
    public void insert(Product product) {
        String sql = "INSERT product SET id = ?, pro_name = ?," +
                " cat_id = ?, pro_quantity = ?, pro_price = ?, create_at = ?, pro_image = ?";
        try {
            PreparedStatement ptm = con.prepareStatement(sql);
            ptm.setString(1, product.getId());
            ptm.setString(2, product.getName());
            ptm.setString(3, product.getCategory().getId());
            ptm.setInt(4, product.getQuantity());
            ptm.setDouble(5, product.getPrice());
            ptm.setDate(6, java.sql.Date.valueOf(product.getCreateDate()));
            ptm.setString(7, product.getImage());
            ptm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ConnectDatabase.getInstance().closeConnect(con);
    }

    @Override
    public void delete(Product product) {
        String sql = "DELETE FROM product WHERE id = ?";
        try {
            PreparedStatement ptm = con.prepareStatement(sql);
            ptm.setString(1, product.getId());
            ptm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ObservableList<Product> getAll() {
        ObservableList<Product> products = FXCollections.observableArrayList();
        String sql = "SELECT * FROM product";
        try {
            PreparedStatement ptm = con.prepareStatement(sql);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()){
                String id = rs.getString("id");
                String name = rs.getString("pro_name");
                String cat_id = rs.getString("cat_id");
                Category category = CategoryDAO.getInstance().findById(cat_id);
                int quantity = rs.getInt("pro_quantity");
                double price = rs.getDouble("pro_price");
                LocalDate create_at = rs.getDate("create_at").toLocalDate();
                String image = rs.getString("pro_image");
                Product product = new Product(id, name, category, quantity, price, create_at, image);
                products.add(0, product);
            }
            ConnectDatabase.getInstance().closeConnect(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public Product findById(String id) {
        Product product = null;
        String sql = "SELECT * FROM product WHERE id = ?";
        try {
            PreparedStatement ptm = con.prepareStatement(sql);
            ptm.setString(1, id);
            ResultSet rs = ptm.executeQuery();
            if(rs.next()){
                String pro_id = rs.getString("id");
                String name = rs.getString("pro_name");
                String cat_id = rs.getString("cat_id");
                Category category = CategoryDAO.getInstance().findById(cat_id);
                int quantity = rs.getInt("pro_quantity");
                double price = rs.getDouble("pro_price");
                LocalDate create_at = rs.getDate("create_at").toLocalDate();
                String image = rs.getString("pro_image");
                product = new Product(pro_id, name, category, quantity, price, create_at, image);
            }
            ConnectDatabase.getInstance().closeConnect(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    public void update(Product product, String id) {
        String sql = "UPDATE product SET id = ?, pro_name = ?," +
                " cat_id = ?, pro_quantity = ?, pro_price = ?, create_at = ?, pro_image = ? WHERE id = ?";
        try {
            PreparedStatement ptm = con.prepareStatement(sql);
            ptm.setString(1, product.getId());
            ptm.setString(2, product.getName());
            ptm.setString(3, product.getCategory().getId());
            ptm.setInt(4, product.getQuantity());
            ptm.setDouble(5, product.getPrice());
            ptm.setDate(6, java.sql.Date.valueOf(product.getCreateDate()));
            ptm.setString(7, product.getImage());
            ptm.setString(8, id);
            ptm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ConnectDatabase.getInstance().closeConnect(con);
    }

    public int findByCatId(Category category){
        int count = 0;
        String sql = "SELECT COUNT(*) FROM product WHERE cat_id = ?";
        try {
            PreparedStatement ptm = con.prepareStatement(sql);
            ptm.setString(1, category.getId());
            ResultSet rs = ptm.executeQuery();
            if(rs.next()){
                count = rs.getInt("COUNT(*)");
            };
            ConnectDatabase.getInstance().closeConnect(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public int countProducts(){
        int count = 0;
        String sql = "SELECT COUNT(*) FROM product";
        try {
            PreparedStatement ptm = con.prepareStatement(sql);
            ResultSet rs = ptm.executeQuery();
            if(rs.next()){
                count = rs.getInt("COUNT(*)");
            };
            ConnectDatabase.getInstance().closeConnect(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ConnectDatabase.getInstance().closeConnect(con);
        return count;
    }
}
