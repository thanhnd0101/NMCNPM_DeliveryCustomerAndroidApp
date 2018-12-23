package com.example.niot.deliveryfood.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Bill implements Serializable {
    @SerializedName("id")
    private int idHoaDon;
    @SerializedName("gia")
    private int giaHoaDon;
    @SerializedName("dchi_giao")
    private String DiaChiaGiao;
    @SerializedName("tg_nd_dat")
    private String TGKhachHangDat;
    @SerializedName("tg_qa_xac_nhan")
    private String TGQuanAnXacNhan;
    @SerializedName("tg_sh_xac_nhan_giao")
    private String TGShipperXacNhanDon;
    @SerializedName("tg_sh_xac_nhan_nhan_hang")
    private String TGShipperXacNhanNhanHang;
    @SerializedName("tg_sh_giao_thanhcong")
    private String TGShipperXacNhanDaGiaoHang;
    @SerializedName("trangthai")
    private int trangThai;
    @SerializedName("gia_vanchuyen")
    private int giaVanChuyen;

    public int getIdHoaDon() {
        return idHoaDon;
    }

    public int getGiaHoaDon() {
        return giaHoaDon;
    }

    public String getDiaChiaGiao() {
        return DiaChiaGiao;
    }

    public String getTGKhachHangDat() {
        return TGKhachHangDat;
    }

    public String getTGQuanAnXacNhan() {
        return TGQuanAnXacNhan;
    }

    public String getTGShipperXacNhanDon() {
        return TGShipperXacNhanDon;
    }

    public String getTGShipperXacNhanNhanHang() {
        return TGShipperXacNhanNhanHang;
    }

    public String getTGShipperXacNhanDaGiaoHang() {
        return TGShipperXacNhanDaGiaoHang;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public int getGiaVanCHuyen() {
        return giaVanChuyen;
    }
}
