package com.websoftquality.agaphey;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.websoftquality.agaphey.adapters.TopicAdapter;
import com.websoftquality.agaphey.views.main.AddForumActivity;

import org.json.JSONObject;


public class ForumFragment extends Fragment implements View.OnClickListener {
    FloatingActionButton fab;
    RecyclerView rv_topics;
    TopicAdapter topicAdapter;
    LinearLayoutManager linearLayoutManager;
    JSONObject jsonObject;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_forum, container, false);
        jsonObject=new JSONObject();
        fab=view.findViewById(R.id.fab);
        rv_topics=view.findViewById(R.id.rv_topics);
        linearLayoutManager=new LinearLayoutManager(getContext());
        topicAdapter=new TopicAdapter(getContext(),jsonObject);
        rv_topics.setLayoutManager(linearLayoutManager);
        rv_topics.setAdapter(topicAdapter);

        fab.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.fab){
            Intent intent=new Intent(getContext(), AddForumActivity.class);
            startActivity(intent);
        }
    }
}