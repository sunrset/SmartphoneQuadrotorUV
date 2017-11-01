package com.survey360.quadcoptercontroluv.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaScannerConnection;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/**
 * Created by AAstudillo on 15/10/2017.
 */

public class SaveFile implements Serializable {

    private final Context ctx;
    private File file, path;
    private int hour, minutes, seconds, year, month, day;

    //private final ArrayList<String> list;
    private String file_name;
    private CharSequence app_name;

    public SaveFile (Context context){
        this.ctx = context;
        //list = new ArrayList<>();
        file_name = "";
    }

    public void saveArrayList(ArrayList<String> arrayList, String filename) {
        file_name = filename;
        final ArrayList<String> list = arrayList;

        Resources appR = ctx.getResources();
        app_name = appR.getText(appR.getIdentifier("app_name",
                "string", ctx.getPackageName()));

        Thread save = new Thread() {
            @Override
            public void run() {
                try {
                    hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                    minutes = Calendar.getInstance().get(Calendar.MINUTE);
                    seconds = Calendar.getInstance().get(Calendar.SECOND);
                    year = Calendar.getInstance().get(Calendar.YEAR);
                    month = Calendar.getInstance().get(Calendar.MONTH)+1;
                    day = Calendar.getInstance().get(Calendar.DATE);

                    path = new File(Environment.getExternalStorageDirectory()+"/"+app_name+"/");
                    if (!path.exists()) {path.mkdir();}

                    file = new File(path, file_name+"-"+year+"-"+month+"-"+day+"-"+hour+":"+minutes+":"+seconds+".quv");
                    FileOutputStream fileOutputStream = new FileOutputStream(file, true);

                    DataOutputStream out = new DataOutputStream(fileOutputStream);
                    out.writeBytes(list.toString());
                    out.close();
                    fileOutputStream.close();

                    file.setReadable(true);
                    file.setWritable(true);
                    MediaScannerConnection.scanFile(ctx, new String[] {file.toString()}, null, null);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        save.start();
    }


}
