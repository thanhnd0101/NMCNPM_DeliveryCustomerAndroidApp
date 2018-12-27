package com.example.niot.deliveryfood;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.niot.deliveryfood.Adapter.BillViewAdapter;
import com.example.niot.deliveryfood.R;
import com.example.niot.deliveryfood.model.Bill;
import com.example.niot.deliveryfood.retrofit.CvlApi;
import com.example.niot.deliveryfood.retrofit.RetrofitObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BillsViewFragment extends Fragment implements BillViewAdapter.BillViewOnClickListener {
    HasUserId hasUserId;
    RecyclerView recyclerView;
    List<Bill> bills;
    BillViewAdapter adapter;
    int userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bills_view, container, false);
        recyclerView = view.findViewById(R.id.bill_view_recycler_view);
        bills = new ArrayList<>();
        adapter = new BillViewAdapter(bills, this);
        recyclerView.setAdapter(adapter);
        getBills();

        return view;
    }

    private void getBills() {
        Retrofit retrofit = RetrofitObject.getInstance();
        retrofit.create(CvlApi.class).getBillsList(userId).enqueue(new Callback<List<Bill>>() {
            @Override
            public void onResponse(Call<List<Bill>> call, Response<List<Bill>> response) {
                List<Bill> billsList = response.body();
                if(billsList != null) {
                    if (billsList.size() > 0) {
                        bills.clear();
                        bills.addAll(billsList);
                        adapter.notifyDataSetChanged();
                    }
                }
                else
                    Toast.makeText(BillsViewFragment.this.getActivity(), "Failed1, Refresh please", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<List<Bill>> call, Throwable t) {
                Toast.makeText(BillsViewFragment.this.getActivity(), "Failed3!", Toast.LENGTH_SHORT).show();

                if (t instanceof IOException) {
                    Toast.makeText(BillsViewFragment.this.getActivity(), "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    //Toast.makeText(SignUpActivity.this, "conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                    Toast.makeText(BillsViewFragment.this.getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HasUserId) {
            hasUserId = (HasUserId) context;
            userId = hasUserId.getUserId();
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        hasUserId = null;
    }

    @Override
    public void onClickBillView(Bill r) {
        // Call the bill detail
        //Intent i = new Intent();
        //getActivity().startActivity(i);
    }

    public interface HasUserId {
        // TODO: Update argument type and name
        int getUserId();
    }
}
