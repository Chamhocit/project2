package com.aptech.project2.Controller;

import com.aptech.project2.DAO.OrderDao;
import com.aptech.project2.DAO.ProductDAO;
import com.aptech.project2.Model.ConnectDatabase;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
public class DashBoardController implements Initializable {
        private Connection con = ConnectDatabase.getInstance().getConnect();
        @FXML
        private AnchorPane anchoiceParent;

        @FXML
        private AreaChart<?, ?> chartIncome;

        @FXML
        private Label txtIncome;

        @FXML
        private Label txtOrder;

        @FXML
        private Label txtProduct;

    public void showDashBoard(){
        int products = ProductDAO.getInstance().countProducts();
        int orders = OrderDao.getInstance().countOrders();
        double todayIncome = OrderDao.getInstance().getTotalToday();
        txtOrder.setText(String.valueOf(orders));
        txtProduct.setText(String.valueOf(products));
        txtIncome.setText(String.valueOf(todayIncome)+"$");
    }

    public void showChart(){
        chartIncome.getData().clear();
        String sql = "SELECT SUM(total), DATE(create_at) FROM orders GROUP BY DATE(create_at)";
        XYChart.Series chart = new XYChart.Series();
        try {
            PreparedStatement ptm = con.prepareStatement(sql);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()){
                chart.getData().add(new XYChart.Data<>(rs.getString(2), rs.getDouble(1)));
            }
            chartIncome.getData().add(chart);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ConnectDatabase.getInstance().closeConnect(con);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showDashBoard();
        showChart();
    }


}
