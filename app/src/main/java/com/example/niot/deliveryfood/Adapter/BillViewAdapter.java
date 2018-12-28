package com.example.niot.deliveryfood.Adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.niot.deliveryfood.R;
import com.example.niot.deliveryfood.model.Bill;

import java.util.List;

public class BillViewAdapter extends RecyclerView.Adapter<BillViewAdapter.BillHolder>  {
    List<Bill> bills;
    BillViewOnClickListener listener;

    @NonNull
    @Override
    public BillHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.bill_info_view,viewGroup,false);
        return new BillHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillHolder billHolder, final int i) {
        final Bill bill = bills.get(i);

        // ID hoá đơn
        String idHoaDon = "Đơn #" + String.valueOf(bill.getIdHoaDon());
        billHolder.bill_id.setText(idHoaDon);

        // Địa chỉ của hoá đơn
        billHolder.bill_addr.setText(bill.getDiaChiGiao());

        // Giá tiền hoá đơn
        String gia = "Tổng tiền: " + String.valueOf(bill.getGiaHoaDon() + bill.getGiaVanCHuyen());
        billHolder.bill_price.setText(gia);

        // Trạng thái hoá đơn
        final TextView status = billHolder.bill_status;
        String str;
        switch (bill.getTrangThai()){
            case 0: str = "Chờ quán xác nhận."; break;
            case 1: str = "Quán đã xác nhận"; break;
            case 2: str = "Shipper đã xác nhận"; break;
            case 3: str = "Đang giao"; break;
            case 4: str = "Giao thành công"; break;
            default: str = "Đã huỷ"; break;
        }

        final String str1 = str;
        status.post(new Runnable() {
            @Override
            public void run() {
                status.setText(str1);
                if(bill.getTrangThai() == -1)
                    status.setTextColor(Color.RED);
                else if(bill.getTrangThai() < 4)
                    status.setTextColor(Color.BLUE);
                else
                    status.setTextColor(0xFF005500);
            }
        });

        // Bấm vào để hiện chi tiết -> mở intent activity khác, gọi về fragment để fragment xử lý
        billHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null)
                    listener.onClickBillView(bills.get(i));
            }
        });
    }


    @Override
    public int getItemCount() {
        return bills.size();
    }

    public BillViewAdapter(List<Bill> bills, BillViewOnClickListener listener){
        this.listener = listener;
        this.bills = bills;
    }

    public interface BillViewOnClickListener{
        void onClickBillView(Bill r);
    }

    public class BillHolder extends RecyclerView.ViewHolder {
        TextView bill_id;
        TextView bill_addr;
        TextView bill_price;
        TextView bill_status;

        public BillHolder(@NonNull View itemView) {
            super(itemView);
            bill_id = itemView.findViewById(R.id.bill_id_text_view);
            bill_addr = itemView.findViewById(R.id.bill_address_text_view);
            bill_price = itemView.findViewById(R.id.bill_price_text_view);
            bill_status = itemView.findViewById(R.id.bill_info_status);
        }
    }
}
