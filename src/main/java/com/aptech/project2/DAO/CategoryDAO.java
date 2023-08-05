package com.aptech.project2.DAO;

import com.aptech.project2.Model.Category;
import com.aptech.project2.Model.ConnectDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;


public class CategoryDAO {
    public static CategoryDAO getInstance(){
        return new CategoryDAO();
    }

    private Connection con = ConnectDatabase.getInstance().getConnect();

    public ObservableList<Category> getList(){
        ObservableList<Category> categories = FXCollections.observableArrayList();
        String sql = "SELECT * FROM category";
        try {
            PreparedStatement ptm = con.prepareStatement(sql);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()){
                Category category = new Category();
                category.setId(rs.getString("id"));
                category.setName(rs.getString("cat_name"));
                String parentID = rs.getString("parent_id");
                Category parentCat = CategoryDAO.getInstance().findById(parentID);
                category.setParentCat(parentCat);
                category.setCreateDate(rs.getDate("create_at").toLocalDate());
                categories.add(0, category);
            }
            ConnectDatabase.getInstance().closeConnect(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public void insertCat(Category category){
        String sql = "INSERT INTO category(id, cat_name, parent_id, create_at ) VALUES(?,?,?,?)";
        try {
            PreparedStatement ptm = con.prepareStatement(sql);
            ptm.setString(1, category.getId());
            ptm.setString(2, category.getName());
            ptm.setString(3, category.getParentCat()!=null ? category.getParentCat().getId():"");
            ptm.setDate(4, java.sql.Date.valueOf(category.getCreateDate()));
            ptm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ConnectDatabase.getInstance().closeConnect(con);
    }

    public void updateCat(String id, Category category){
        String sql = "UPDATE category SET id = ?, cat_name = ?, parent_id = ?, create_at = ? WHERE id =? ";
        try {
            PreparedStatement ptm = con.prepareStatement(sql);
            ptm.setString(1, category.getId());
            ptm.setString(2, category.getName());
            ptm.setString(3, category.getParentCat().getId());
            ptm.setDate(4, java.sql.Date.valueOf(category.getCreateDate()));
            ptm.setString(5, id);
            ptm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ConnectDatabase.getInstance().closeConnect(con);
    }

    public void deleteCat(Category category){
        String sql = "DELETE FROM category WHERE id =? ";
        try {
            PreparedStatement ptm = con.prepareStatement(sql);
            ptm.setString(1, category.getId());
            ptm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ConnectDatabase.getInstance().closeConnect(con);
    }


    public Category findById(String catId) {
        String sql = "SELECT * FROM category WHERE id = ?";
        Category cat = null;
        try {
            PreparedStatement ptm = con.prepareStatement(sql);
            ptm.setString(1, catId);
            ResultSet rs = ptm.executeQuery();
            if(rs.next()){
                String id = rs.getString("id");
                String name = rs.getString("cat_name");
                String parentId = rs.getString("parent_id");
                Category parentCat = CategoryDAO.getInstance().findById(parentId);
                LocalDate createDate = rs.getDate("create_at").toLocalDate();
                cat = new Category(id, name, parentCat, createDate);
            }
            ConnectDatabase.getInstance().closeConnect(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cat;
    }
}
