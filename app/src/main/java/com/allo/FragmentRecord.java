package com.allo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by baek_uncheon on 2015. 3. 26..
 */
public class FragmentRecord extends Fragment {
    public ViewPager mPager;

    Context mContext;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, null);

        return view;
    }
}
