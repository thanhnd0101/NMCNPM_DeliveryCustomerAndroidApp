package com.example.niot.deliveryfood;

import android.widget.Toast;

import com.example.niot.deliveryfood.model.Bill;
import com.example.niot.deliveryfood.retrofit.CvlApi;
import com.example.niot.deliveryfood.retrofit.RetrofitObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CurrentBillsViewFragment extends BillsViewFragment {
    boolean firstRun = true;

    @Override
    protected void getBills() {
        final Runnable getData = new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        };

        Runnable autoRefreshFunc = new Runnable() {
            @Override
            public void run() {
                while(true){
                    try{
                        if(firstRun)
                        {
                            recyclerView.post(getData);
                            firstRun = false;
                        }
                        recyclerView.postDelayed(getData, 2000);
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
        };

        new Thread(autoRefreshFunc).start();
    }

    private void loadData(){
        Retrofit retrofit = RetrofitObject.getInstance();
        retrofit.create(CvlApi.class).getCurrentBillsList(userId).enqueue(new Callback<List<Bill>>() {
            @Override
            public void onResponse(Call<List<Bill>> call, Response<List<Bill>> response) {
                List<Bill> billsList = response.body();
                if(billsList != null) {
                    CurrentBillsViewFragment.this.bills.clear();
                    CurrentBillsViewFragment.this.bills.addAll(billsList);
                    CurrentBillsViewFragment.this.adapter.notifyDataSetChanged();
                }
                else{
                    if(CurrentBillsViewFragment.this.getActivity() == null)
                        return;
                    Toast.makeText(CurrentBillsViewFragment.this.getActivity(), "Failed1, Refresh please", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Bill>> call, Throwable t) {
                if(CurrentBillsViewFragment.this.getActivity() == null)
                    return;
                Toast.makeText(CurrentBillsViewFragment.this.getActivity(), "Failed3!", Toast.LENGTH_SHORT).show();

                if (t instanceof IOException) {
                    Toast.makeText(CurrentBillsViewFragment.this.getActivity(), "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    //Toast.makeText(SignUpActivity.this, "conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                    Toast.makeText(CurrentBillsViewFragment.this.getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        firstRun = true;
    }
}
