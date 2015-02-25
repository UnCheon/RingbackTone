package com.example.uncheon.ringbacktone;

/**
 * Created by baek_uncheon on 2015. 2. 25..
 */

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

class JavaFilter implements FilenameFilter{

    @Override
    public boolean accept(File dir, String filename) {
        // TODO Auto-generated method stub
//                   return false;
        return (filename.endsWith(".java")); // 확장자가 java인지 확인
    }

}

public class FileList extends Activity {

    //         private static final String FILE_PATH = new String("/sdcard/javaeditor/");
    private List<String> mFileNames = new ArrayList<String>();
    ListView mFlieListView;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.file_list);

            mFlieListView = (ListView) findViewById(R.id.file_listview);
            this.updateFileList();
        }
        catch(NullPointerException e)
        {
            Log.v(getString(R.string.app_name), e.getMessage());
        }
    }

    public void updateFileList()
    {
        String ext = Environment.getExternalStorageState();
        String path = null;
        if(ext.equals(Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/javaeditor/";
        }
        else
        {
            path = Environment.MEDIA_UNMOUNTED;
        }

        File files = new File(path);
//                   File files = new File(FILE_PATH);
        ArrayAdapter<String> fileList = new ArrayAdapter<String>(this, R.layout.file_list_item, mFileNames);

        if(files.listFiles(new JavaFilter()).length > 0)
        {
            for(File file : files.listFiles(new JavaFilter()))
            {
                mFileNames.add(file.getName());
            }
        }
        mFlieListView.setAdapter(fileList);
    }
}
