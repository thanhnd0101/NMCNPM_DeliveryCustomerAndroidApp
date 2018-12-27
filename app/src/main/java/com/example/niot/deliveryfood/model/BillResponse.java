package com.example.niot.deliveryfood.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BillResponse implements Serializable {
    @SerializedName("hoa_don")
    @Expose
    private Bill hoadon;
    @SerializedName("nguoi_dung")
    @Expose
    private User nguoidung;
    @SerializedName("quan_an")
    @Expose
    private Restaurant quanan;
    @SerializedName("chi_tiet")
    @Expose
    private List<BillDetail> chi_tiet;

    public Bill getHoadon() {
        return hoadon;
    }

    public User getNguoidung() {
        return nguoidung;
    }

    public Restaurant getQuanan() {
        return quanan;
    }

    public List<BillDetail> getChi_tiet() {
        return chi_tiet;
    }


    @Override
    public String toString() {
        String ans = "Bill: ";
        ans += "Bill id : " + String.valueOf(hoadon.getIdHoaDon()) + "\n";
        ans += "Bill addr : " + hoadon.getDiaChiaGiao() + "\n";
        for(BillDetail d : chi_tiet){
            ans += "id mon : " + String.valueOf(d.getId_mon_an()) + "\n";
        }
        return ans;
    }
}
