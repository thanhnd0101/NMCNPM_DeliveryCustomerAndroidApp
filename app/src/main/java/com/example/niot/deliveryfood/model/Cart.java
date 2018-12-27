package com.example.niot.deliveryfood.model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cart implements Serializable {
    @SerializedName("id_khach")
    @Expose
    private int id_customer;

    @SerializedName("id_quan")
    @Expose
    private int id_restaurant;

    @SerializedName("dchi_giao")
    @Expose
    private String address;

    @SerializedName("chi_tiet")
    @Expose
    private List<Detail> detail;

    public int getId_customer() {
        return id_customer;
    }

    public int getId_restaurant() {
        return id_restaurant;
    }

    public List<Detail> getDetail() {
        return detail;
    }

    public Cart(int customer, int restaurant, String address){
        id_customer = customer;
        id_restaurant = restaurant;
        this.address = address;
        detail = new ArrayList<>();
    }

    public Cart(User customer, Restaurant restaurant, @Nullable String address){
        id_customer = customer.getId();
        id_restaurant = restaurant.getId();
        if(address == null)
            this.address = customer.getAddress();
        else
            this.address = address;
        detail = new ArrayList<>();
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void addDetail(Food food, int quantity){
        int id = food.getId();
        Detail d = new Detail(food, quantity);
        if(detail.size() > 0)
            for(Detail dd : detail){
                if(dd.food_id == id)
                    detail.remove(dd);
            }
        detail.add(d);
    }

    public int getQuantity(Food food){
        for(Detail d : detail){
            if(d.food_id == food.getId())
                return d.quantity;
        }
        return -1;
    }

    public class Detail implements Serializable{
        @SerializedName("mon")
        @Expose
        Food food;

        @SerializedName("id_mon")
        @Expose
        int food_id;

        @SerializedName("sl")
        @Expose
        int quantity;

        private Detail(Food f, int q){
            food = f;
            food_id = f.getId();
            quantity = q;
        }

        public Food getFood() {
            return food;
        }

        public int getQuantity() {
            return quantity;
        }
    }

    @Override
    public String toString() {
        String ans = "";
        for(Detail d : detail){
            ans += "id : " + String.valueOf(d.food_id) + " sl : " + String.valueOf(d.quantity) + "\n";
        }
        return ans;
    }
}
