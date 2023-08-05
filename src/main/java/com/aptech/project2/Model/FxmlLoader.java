package com.aptech.project2.Model;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class FxmlLoader {

    public static FxmlLoader getInstance(){
        return new FxmlLoader();
    }
    public Parent getViewPane(String fileNamePane){
        Parent view=new Pane();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation( getClass().getResource(fileNamePane));
            view = loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        return view;
    }
}
