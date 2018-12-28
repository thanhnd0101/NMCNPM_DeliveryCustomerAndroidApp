package com.example.niot.deliveryfood;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.niot.deliveryfood.Adapter.FoodOnlyTextAdapter;
import com.example.niot.deliveryfood.model.Bill;
import com.example.niot.deliveryfood.model.BillDetail;
import com.example.niot.deliveryfood.retrofit.CvlApi;
import com.example.niot.deliveryfood.retrofit.RetrofitObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BillDetailActivity extends AppCompatActivity {
    FoodOnlyTextAdapter adapter;
    RecyclerView recyclerView;
    Bill bill;
    List<BillDetail> details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_detail);
        Bundle extras = getIntent().getExtras();
        details = new ArrayList<>();
        if(extras != null)
            bill = (Bill)extras.get("bill");
        if(bill == null)
            finish();
        setUpActivityViews();
        setUpBillDetails();
        getBillDetail();
        initRefreshThread();
    }

    private void setUpActivityViews() {
        TextView bill_id = findViewById(R.id.bill_detail_bill_id);
        String bill_id_str = "Hoá đơn #" + bill.getIdHoaDon();
        bill_id.setText(bill_id_str);

        TextView customer_address = findViewById(R.id.bill_detail_customer_addr);
        customer_address.setText(bill.getDiaChiGiao());

        TextView restaurant_address = findViewById(R.id.bill_detail_restaurant_addr);
        restaurant_address.setText(bill.getDiaChiQuan());

        TextView total = findViewById(R.id.bill_detail_total);
        total.setText(String.valueOf(bill.getGiaHoaDon()));

        TextView ship = findViewById(R.id.bill_detail_ship);
        ship.setText(String.valueOf(bill.getGiaVanCHuyen()));

        TextView totalTotal = findViewById(R.id.bill_detail_total_total);
        totalTotal.setText(String.valueOf(bill.getGiaHoaDon() + bill.getGiaVanCHuyen()));

        updateBillStatus();
    }

    private void updateBillStatus(){
        final TextView status = findViewById(R.id.bill_detail_status);
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
    }

    private void initRefreshThread(){
        final Runnable loadData = new Runnable() {
            @Override
            public void run() {
                getBill();
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try{
                        recyclerView.postDelayed(loadData, 2000);
                        try{
                            Thread.sleep(2000);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void getBill() {
        Retrofit retrofit = RetrofitObject.getInstance();
        retrofit.create(CvlApi.class).getBillById(bill.getIdHoaDon()).enqueue(new Callback<Bill>() {
            @Override
            public void onResponse(Call<Bill> call, Response<Bill> response) {
                if(response.body() != null){
                    int oldStatus = bill.getTrangThai();
                    bill = response.body();
                    if(oldStatus != bill.getTrangThai())
                        updateBillStatus();
                }
                else{
                    Toast.makeText(BillDetailActivity.this, "Null Response", Toast.LENGTH_SHORT).show();
                    getBillDetail();
                }
            }

            @Override
            public void onFailure(Call<Bill> call, Throwable t) {
                Toast.makeText(BillDetailActivity.this, "Get data failed!", Toast.LENGTH_SHORT).show();
                Log.e("BILL_DETAIL: ", t.getMessage());
                BillDetailActivity.this.finish();
            }
        });
    }

    private void getBillDetail() {
        Retrofit retrofit = RetrofitObject.getInstance();
        retrofit.create(CvlApi.class).getBillDetailByBill(bill.getIdHoaDon()).enqueue(new Callback<List<BillDetail>>() {
            @Override
            public void onResponse(Call<List<BillDetail>> call, Response<List<BillDetail>> response) {
                if(response.body() != null){
                    if(response.body().size() > 0){
                        BillDetailActivity.this.details.clear();
                        BillDetailActivity.this.details.addAll(response.body());
                        BillDetailActivity.this.adapter.notifyDataSetChanged();
                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(BillDetailActivity.this);
                        builder.setMessage("Không tìm thấy hoá đơn này!!").setTitle("Lỗi").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        BillDetailActivity.this.finish();
                                    }
                                });
                        builder.create().show();
                    }
                }
                else{
                    Toast.makeText(BillDetailActivity.this, "Null Response", Toast.LENGTH_SHORT).show();
                    getBillDetail();
                }
            }

            @Override
            public void onFailure(Call<List<BillDetail>> call, Throwable t) {
                Toast.makeText(BillDetailActivity.this, "Get data failed!", Toast.LENGTH_SHORT).show();
                Log.e("BILL_DETAIL: ", t.getMessage());
                BillDetailActivity.this.finish();
            }
        });
    }

    private void setUpBillDetails() {
        adapter = new FoodOnlyTextAdapter(details);
        recyclerView = findViewById(R.id.bill_detail_recycler_view);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(0);
        finish();
    }
}
