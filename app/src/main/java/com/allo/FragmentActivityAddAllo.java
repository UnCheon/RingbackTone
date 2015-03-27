package com.allo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by baek_uncheon on 2015. 3. 26..
 */
public class FragmentActivityAddAllo extends FragmentActivity {
    static final int NUM_ITEMS = 3;

    Context mContext = this;

    ViewPager mPager;
    SlidePagerAdapter mPagerAdapter;


    ImageButton storeBtn;
    ImageButton uploadBtn;
    ImageButton recordBtn;

    int status = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_activity_add_allo);

        mPager = (ViewPager)findViewById(R.id.pager);
        mPagerAdapter = new SlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        setLayout();
        setListener();
    }

    private void setLayout(){
        storeBtn = (ImageButton)findViewById(R.id.storeBtn);
        uploadBtn = (ImageButton)findViewById(R.id.uploadBtn);
        recordBtn = (ImageButton)findViewById(R.id.recordBtn);
    }
    private void setListener(){
        storeBtn.setOnClickListener(addOnClickListener);
        uploadBtn.setOnClickListener(addOnClickListener);
        recordBtn.setOnClickListener(addOnClickListener);
        mPager.setOnPageChangeListener(pageChangeListener);
    }

    private View.OnClickListener addOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.storeBtn:
                    if (status == 0){
                        break;
                    }
                    else{
                        status = 0;
                        clickStore();
                        mPager.setCurrentItem(0, true);
                    }
                    break;
                case R.id.uploadBtn:
                    if (status == 1){
                        break;
                    }
                    else{
                        status =1;
                        clickUpload();
                        mPager.setCurrentItem(1, true);
                    }
                    break;
                case R.id.recordBtn:
                    if (status == 2){
                        break;
                    }else{
                        status = 2;
                        clickRecord();
                        mPager.setCurrentItem(2, true);
                    }
                    break;
            }
        }
    };

    private void clickStore(){
        storeBtn.setBackgroundResource(R.drawable.store_btn_1);
        uploadBtn.setBackgroundResource(R.drawable.upload_btn_2);
        recordBtn.setBackgroundResource(R.drawable.record_btn_2);
    }
    private void clickUpload(){
        storeBtn.setBackgroundResource(R.drawable.store_btn_2);
        uploadBtn.setBackgroundResource(R.drawable.upload_btn_1);
        recordBtn.setBackgroundResource(R.drawable.record_btn_2);
    }
    private void clickRecord(){
        storeBtn.setBackgroundResource(R.drawable.store_btn_2);
        uploadBtn.setBackgroundResource(R.drawable.upload_btn_2);
        recordBtn.setBackgroundResource(R.drawable.record_btn_1);
    }

    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position){
                case 0:
                    clickStore();
                    break;
                case 1:
                    clickUpload();
                    break;
                case 2:
                    clickRecord();
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public class SlidePagerAdapter extends FragmentPagerAdapter {
        public SlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0){
                FragmentStore mFragmentStore= new FragmentStore();
                mFragmentStore.mPager = mPager;
                return mFragmentStore ;
            }else if (position == 1){
                FragmentUpload mFragmentUpload= new FragmentUpload(mContext);
                mFragmentUpload.mPager = mPager;
                return mFragmentUpload;
            }else{
                FragmentRecord mFragmentRecord = new FragmentRecord();
                mFragmentRecord.mPager = mPager;
                return mFragmentRecord;
            }
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }
    }

    public void backBtn(View v){
        finish();
    }
}
