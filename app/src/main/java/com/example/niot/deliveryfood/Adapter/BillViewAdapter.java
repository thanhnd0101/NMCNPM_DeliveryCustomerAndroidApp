package com.example.niot.deliveryfood.Adapter;

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
        // ID hoá đơn
        String idHoaDon = "Đơn #" + String.valueOf(bills.get(i).getIdHoaDon());
        billHolder.bill_id.setText(idHoaDon);

        // Địa chỉ của hoá đơn
        billHolder.bill_addr.setText(bills.get(i).getDiaChiaGiao());

        // Giá tiền hoá đơn
        String gia = "Tổng tiền: " + String.valueOf(bills.get(i).getGiaHoaDon() + bills.get(i).getGiaVanCHuyen());
        billHolder.bill_price.setText(gia);

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

        public BillHolder(@NonNull View itemView) {
            super(itemView);
            bill_id = itemView.findViewById(R.id.bill_id_text_view);
            bill_addr = itemView.findViewById(R.id.bill_address_text_view);
            bill_price = itemView.findViewById(R.id.bill_price_text_view);
        }
    }
}
