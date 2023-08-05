package com.aptech.project2.Validation;

public class Validate {
    public static boolean checkIdCustomer(String id){
        boolean f = true;
        return f;
    }

    public static boolean checkProQuantity(String quantity){
        String regex = "^[1-9]\\d*$";
        return quantity.matches(regex);
    }

    public static boolean checkPriceProduct(String price){
        String regex = "^?\\d+(\\.\\d+)?$";
        return price.matches(regex);
    }

    public static boolean checkCatId(String catId){
        String regex = "^C\\d{3}$";
        return catId.matches(regex);
    }

    public static boolean checkProId(String proId){
        String regex = "P\\d{4}$";
        return proId.matches(regex);
    }


    public static boolean checkFullName(String fullName){
        String regex = "[^!@#$%^&*()]*$";
        return fullName.matches(regex);
    }

    public static boolean checkPass(String password){
        String regex = "(?=(.*[0-9]))(?=.*[a-z])(?=(.*)).{8,}";
        return password.matches(regex);
    }

    public static boolean checkPhone(String phone){
        String regex = "^[0-9]{10,15}$";
        return phone.matches(regex);
    }
}
