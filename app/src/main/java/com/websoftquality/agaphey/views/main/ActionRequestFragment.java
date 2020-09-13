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
import com.websoftquality.agaphey.adapters.main.DashboardAdapter;
import com.websoftquality.agaphey.adapters.main.RequestAdapter;

import org.json.JSONObject;


public class ActionRequestFragment extends Fragment {
    RecyclerView rv_profiles_request;
    LinearLayoutManager linearLayoutManager;
    DashboardAdapter requestAdapter;
    JSONObject jsonObject;



    public ActionRequestFragment() {
        Log.e("TAG", "ActionRequestFragment: ");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_action_request, container, false);
        rv_profiles_request=view.findViewById(R.id.rv_profiles_request);
        linearLayoutManager=new LinearLayoutManager(getContext());
        Log.e("TAG", "ActionRequestFragment: ");
        requestAdapter=new DashboardAdapter(getContext(),jsonObject);
        Log.e("TAG", "onCreateView: ");
        rv_profiles_request.setLayoutManager(linearLayoutManager);
        rv_profiles_request.setAdapter(requestAdapter);
        return view;
    }
}