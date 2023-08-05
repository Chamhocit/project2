package com.aptech.project2.Generic;

import javafx.collections.ObservableList;

public interface IGeneric <T>{
    public void insert(T t);

    public void delete(T t);
    public ObservableList<T> getAll();
}
