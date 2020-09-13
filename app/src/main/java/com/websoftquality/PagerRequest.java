package com.websoftquality;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.websoftquality.agaphey.GroupMessageFragment;
import com.websoftquality.agaphey.MessageFragment;
import com.websoftquality.agaphey.views.main.ActionRequestFragment;
import com.websoftquality.agaphey.views.main.SendRequestFragment;

import org.json.JSONObject;

/**
 * Created by Belal on 2/3/2016.
 */
//Extending FragmentStatePagerAdapter
public class PagerRequest extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;
    JSONObject jsonObject;

    //Constructor to the class
    public PagerRequest(FragmentManager fm, int tabCount, JSONObject jsonObject) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
        this.jsonObject= jsonObject;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                SendRequestFragment tab1 = new SendRequestFragment(jsonObject);
                return tab1;
            case 1:
                ActionRequestFragment tab2 = new ActionRequestFragment();
                return tab2;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}
