package com.allo;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by baek_uncheon on 2015. 3. 26..
 */
public class FragmentStore extends Fragment{
    public ViewPager mPager;
    FrameLayout frameLayout;
    Context mContext;

    ListView rankLv;
    ListView newLv;

    ArrayList<Allo> allo_rank_array;
    ArrayList<Allo> allo_new_array;
//    ListView searchLv;
    LinearLayout searchLayout;

    TextView popularBtn;
    TextView newBtn;
    TextView searchBtn;

    TextView currentBtn;

    int pIndex;
    ImageView pImageView;

    public FragmentStore(Context context){
        this.mContext = context;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store, null);
        setLayout(view);
        setListener();
        setListView();

        return view;
    }

    private void setLayout(View view){
        frameLayout = (FrameLayout)view.findViewById(R.id.frameLayout);
        rankLv = (ListView)view.findViewById(R.id.rankLv);
        newLv = (ListView)view.findViewById(R.id.newLv);
        searchLayout = (LinearLayout)view.findViewById(R.id.searchLayout);

        popularBtn = (TextView)view.findViewById(R.id.popularBtn);
        newBtn = (TextView)view.findViewById(R.id.newBtn);
        searchBtn = (TextView)view.findViewById(R.id.searchBtn);

        currentBtn = popularBtn;
        popularBtn();
    }

    private void setListener(){
        popularBtn.setOnClickListener(categoryListener);
        newBtn.setOnClickListener(categoryListener);
        searchBtn.setOnClickListener(categoryListener);
    }


    View.OnClickListener categoryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.popularBtn:
                    if (currentBtn != popularBtn)
                        popularBtn();
                    break;
                case R.id.newBtn:
                    if (currentBtn != newBtn)
                        newBtn();
                    break;
                case R.id.searchBtn:
                    if (currentBtn != searchBtn)
                        searchBtn();
                    break;
            }

        }
    };

    private void initRankList(){
        currentBtn = popularBtn;
        popularBtn.setBackgroundResource(R.drawable.border_bottom_red);
        popularBtn.setTextColor(Color.parseColor("#f91e2f"));
        rankLv.setVisibility(TextView.VISIBLE);

        newBtn.setBackgroundResource(R.drawable.white_btn);
        newBtn.setTextColor(Color.parseColor("#000000"));
        newLv.setVisibility(TextView.GONE);

        searchBtn.setBackgroundResource(R.drawable.white_btn);
        searchBtn.setTextColor(Color.parseColor("#000000"));
        searchLayout.setVisibility(TextView.GONE);
    }

    private void popularBtn(){
        currentBtn = popularBtn;
        popularBtn.setBackgroundResource(R.drawable.border_bottom_red);
        popularBtn.setTextColor(Color.parseColor("#f91e2f"));
        rankLv.setVisibility(TextView.VISIBLE);

        newBtn.setBackgroundResource(R.drawable.white_btn);
        newBtn.setTextColor(Color.parseColor("#000000"));
        newLv.setVisibility(TextView.GONE);

        searchBtn.setBackgroundResource(R.drawable.white_btn);
        searchBtn.setTextColor(Color.parseColor("#000000"));
        searchLayout.setVisibility(TextView.GONE);
    }

    private void newBtn(){
        currentBtn = newBtn;
        popularBtn.setBackgroundResource(R.drawable.white_btn);
        popularBtn.setTextColor(Color.parseColor("#000000"));
        rankLv.setVisibility(TextView.GONE);

        newBtn.setBackgroundResource(R.drawable.border_bottom_red);
        newBtn.setTextColor(Color.parseColor("#f91e2f"));
        newLv.setVisibility(TextView.VISIBLE);

        searchBtn.setBackgroundResource(R.drawable.white_btn);
        searchBtn.setTextColor(Color.parseColor("#000000"));
        searchLayout.setVisibility(TextView.GONE);
    }

    private void searchBtn(){
        currentBtn = searchBtn;
        popularBtn.setBackgroundResource(R.drawable.white_btn);
        popularBtn.setTextColor(Color.parseColor("#000000"));
        rankLv.setVisibility(TextView.GONE);

        newBtn.setBackgroundResource(R.drawable.white_btn);
        newBtn.setTextColor(Color.parseColor("#000000"));
        newLv.setVisibility(TextView.GONE);

        searchBtn.setBackgroundResource(R.drawable.border_bottom_red);
        searchBtn.setTextColor(Color.parseColor("#f91e2f"));
        searchLayout.setVisibility(TextView.VISIBLE);
    }

    private void setListView(){
        AlloHttpUtils alloHttpUtils = new AlloHttpUtils(mContext, this);
        alloHttpUtils.getNewRing();
        alloHttpUtils.getRankRing();

    }


// call back
    public void setRankAdapter(ArrayList<Allo> allo_array){
        allo_rank_array = allo_array;
        StoreAdapter adapter = new StoreAdapter(mContext, R.layout.layout_store_item, allo_rank_array);
        rankLv.setAdapter(adapter);
        rankLv.setOnItemClickListener(storeItemClickListener);

    }

    public void setNewAdapter(ArrayList<Allo> allo_array){
        allo_new_array = allo_array;
        StoreAdapter adapter = new StoreAdapter(mContext, R.layout.layout_store_item, allo_new_array);
        newLv.setAdapter(adapter);
        newLv.setOnItemClickListener(storeItemClickListener);

    }

    AdapterView.OnItemClickListener storeItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.i("click event", "clickclick");




            ImageView playBtn = (ImageView)view.findViewById(R.id.imageBtn);
            if (pImageView != null){
                if (pImageView == playBtn){
                    stopRingbackTone();
                }
                pImageView.setBackgroundResource(R.drawable.damienrice);
            }

            playBtn.setBackgroundResource(R.drawable.stop_btn_1);
            playRingbackTone(allo_rank_array.get(position));
            pImageView = playBtn;
        }
    };


    //    Ringbacktone play & pause
    private void playRingbackTone(Allo allo){
        RingbackTone mRingbackTone = RingbackTone.getInstance();

        if (mRingbackTone.isPlayingNow())
            mRingbackTone.stopRingbackTone();

        String url = mContext.getString(R.string.base_url)+allo.getRingURL();
        mRingbackTone.playRingbackTone(url);
    }

    private void stopRingbackTone() {
        RingbackTone mRingbackTone = RingbackTone.getInstance();
        mRingbackTone.stopRingbackTone();


    }

}
