package com.allo;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by baek_uncheon on 2015. 3. 30..
 */
public class AlloHttpUtils {
    Context mContext;
    FragmentStore mFragmentStore;


    public AlloHttpUtils(Context mContext){
        this.mContext = mContext;
    }

    public AlloHttpUtils(FragmentStore mFragmentStore){
        this.mFragmentStore = mFragmentStore;
    }

    public AlloHttpUtils(Context mContext, FragmentStore mFragmentStore){
        this.mContext = mContext;
        this.mFragmentStore = mFragmentStore;
    }



    public void getRankRing(){
        AsyncHttpClient myClient;

        myClient = new AsyncHttpClient();
        myClient.setTimeout(30000);
        PersistentCookieStore myCookieStore = new PersistentCookieStore(mContext);
        myClient.setCookieStore(myCookieStore);
        String url = mContext.getString(R.string.base_url)+ "/ringback/tone/store/popular/";

        myClient.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("HTTP RESPONSE......", new String(responseBody));
                parseRankJson(new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println(new String(responseBody));
            }
        });
    }

    private void parseRankJson(String resultString){
        ArrayList<Allo> allo_array = new ArrayList<>();

        try{
            JSONObject result_object = new JSONObject(resultString);
            JSONArray allo_json_array = result_object.getJSONArray("response");
            String status = result_object.getString("status");
            if (status.equals("success")){

                for (int i = 0; i < allo_json_array.length(); i++) {
                    JSONObject allo_object = allo_json_array.getJSONObject(i);

                    String rank = allo_object.getString("rank");
                    String title= allo_object.getString("title");
                    String singer = allo_object.getString("singer");
                    String url = allo_object.getString("url");

                    Allo mAllo = new Allo();
                    mAllo.setRingRank(rank);
                    mAllo.setRingTitle(title);
                    mAllo.setRingSinger(singer);
                    mAllo.setRingURL(url);

                    allo_array.add(mAllo);
                }

            }else{
                Log.i("HttpRequest", "fail fail fail fail");
            }

        }catch (Exception e){
            System.out.println(e);
        }
        mFragmentStore.setRankAdapter(allo_array);
    }



    public void getNewRing(){
        AsyncHttpClient myClient;

        myClient = new AsyncHttpClient();
        myClient.setTimeout(30000);
        PersistentCookieStore myCookieStore = new PersistentCookieStore(mContext);
        myClient.setCookieStore(myCookieStore);
        String url = mContext.getString(R.string.base_url)+ "/ringback/tone/store/new/";

        myClient.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("HTTP RESPONSE......", new String(responseBody));
                parseNewJson(new String(responseBody));


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println(new String(responseBody));
            }
        });
    }

    private void parseNewJson(String resultString){
        ArrayList<Allo> allo_array = new ArrayList<>();

        try{
            JSONObject result_object = new JSONObject(resultString);
            JSONArray allo_json_array = result_object.getJSONArray("response");
            String status = result_object.getString("status");
            if (status.equals("success")){

                for (int i = 0; i < allo_json_array.length(); i++) {
                    JSONObject allo_object = allo_json_array.getJSONObject(i);

                    String rank = allo_object.getString("rank");
                    String title= allo_object.getString("title");
                    String singer = allo_object.getString("singer");
                    String url = allo_object.getString("url");

                    Allo mAllo = new Allo();
                    mAllo.setRingRank(rank);
                    mAllo.setRingTitle(title);
                    mAllo.setRingSinger(singer);
                    mAllo.setRingURL(url);

                    allo_array.add(mAllo);
                }

            }else{
                Log.i("HttpRequest", "fail fail fail fail");
            }

        }catch (Exception e){
            System.out.println(e);
        }
        mFragmentStore.setNewAdapter(allo_array);
    }

}
