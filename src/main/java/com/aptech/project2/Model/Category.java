package com.aptech.project2.Model;

import java.sql.Date;
import java.time.LocalDate;

public class Category {
    private String id;
    private String name;
    private Category parentCat;
    private LocalDate createDate;

    public Category() {
    }

    public Category(String id, String name, Category parentCat, LocalDate createDate) {
        this.id = id;
        this.name = name;
        this.parentCat = parentCat;
        this.createDate = createDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getParentCat() {
        return parentCat;
    }

    public void setParentCat(Category parentCat) {
        this.parentCat = parentCat;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }
}
