package com.allo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

/**
 * Created by baek_uncheon on 2015. 3. 26..
 */
public class AddAlloActivity extends Activity {
    ImageButton storeBtn;
    ImageButton uploadBtn;
    ImageButton recordBtn;

    LinearLayout storeLayout;
    LinearLayout uploadLayout;
    LinearLayout recordLayout;

    int status = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        setLayout();
        setListener();
    }

    private void setLayout(){
        storeBtn = (ImageButton)findViewById(R.id.storeBtn);
        uploadBtn = (ImageButton)findViewById(R.id.uploadBtn);
        recordBtn = (ImageButton)findViewById(R.id.recordBtn);

        storeLayout = (LinearLayout)findViewById(R.id.storeLayout);
        uploadLayout = (LinearLayout)findViewById(R.id.uploadLayout);
        recordLayout= (LinearLayout)findViewById(R.id.recordLayout);

        storeLayout.setVisibility(View.VISIBLE);
        uploadLayout.setVisibility(View.GONE);
        recordLayout.setVisibility(View.GONE);

    }

    private void setListener(){
        storeBtn.setOnClickListener(addOnClickListener);
        uploadBtn.setOnClickListener(addOnClickListener);
        recordBtn.setOnClickListener(addOnClickListener);
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
                        clickStoreBtn();

                    }
                    break;
                case R.id.uploadBtn:
                    if (status == 1){
                        break;
                    }
                    else{
                        status =1;
                        clickUploadBtn();
                    }
                    break;
                case R.id.recordBtn:
                    if (status == 2){
                        break;
                    }else{
                        status = 2;
                        clickRecordBtn();
                    }
                    break;
            }
        }
    };

    private void clickStoreBtn(){
        storeBtn.setBackgroundResource(R.drawable.store_btn_1);
        uploadBtn.setBackgroundResource(R.drawable.upload_btn_2);
        recordBtn.setBackgroundResource(R.drawable.record_btn_2);

        storeLayout.setVisibility(View.VISIBLE);
        uploadLayout.setVisibility(View.GONE);
        recordLayout.setVisibility(View.GONE);

    }
    private void clickUploadBtn(){
        storeBtn.setBackgroundResource(R.drawable.store_btn_2);
        uploadBtn.setBackgroundResource(R.drawable.upload_btn_1);
        recordBtn.setBackgroundResource(R.drawable.record_btn_2);

        storeLayout.setVisibility(View.GONE);
        uploadLayout.setVisibility(View.VISIBLE);
        recordLayout.setVisibility(View.GONE);
    }
    private void clickRecordBtn(){
        storeBtn.setBackgroundResource(R.drawable.store_btn_2);
        uploadBtn.setBackgroundResource(R.drawable.upload_btn_2);
        recordBtn.setBackgroundResource(R.drawable.record_btn_1);

        storeLayout.setVisibility(View.GONE);
        uploadLayout.setVisibility(View.GONE);
        recordLayout.setVisibility(View.VISIBLE);
    }
}
