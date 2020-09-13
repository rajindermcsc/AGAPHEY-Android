package com.websoftquality.agaphey.views.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.websoftquality.agaphey.R;
import com.websoftquality.agaphey.adapters.main.RequestAdapter;

import org.json.JSONObject;


public class SendRequestFragment extends Fragment {
    RecyclerView rv_profiles;
    LinearLayoutManager linearLayoutManager;
    RequestAdapter dashboardAdapter;
    JSONObject jsonObject;


    public SendRequestFragment(JSONObject jsonObject) {
        // Required empty public constructor
        Log.e("TAG", "SendRequestFragment: ");
        this.jsonObject=jsonObject;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_send_request, container, false);
        rv_profiles=view.findViewById(R.id.rv_profiles);
        linearLayoutManager=new LinearLayoutManager(getContext());

        Log.e("TAG", "SendRequestFragment: ");
        dashboardAdapter=new RequestAdapter(getContext(),jsonObject);
        rv_profiles.setLayoutManager(linearLayoutManager);
        rv_profiles.setAdapter(dashboardAdapter);
        return view;
    }
}