package com.survey360.quadcoptercontroluv.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by AAstudillo on 15/10/2017.
 */

public class SaveFile implements Serializable {

    private static Context ctx;
    private final Activity act;
    private File file, path;
    private int hour, minutes, seconds, year, month, day;

    private String file_name;
    private CharSequence app_name;
    public static MediaPlayer mp = new MediaPlayer();

    FileOutputStream fileOutputStream;
    DataOutputStream out;

    private int minTimeOfData = 10; // in seconds

    public SaveFile (Context context, Activity activity){
        this.ctx = context;
        this.act= activity;
        file_name = "";
    }

    public void saveArrayList(final ArrayList<String> arrayList, String filename) {

        file_name = filename;

        final List<String> list = Collections.synchronizedList(arrayList);

        Resources appR = ctx.getResources();
        app_name = appR.getText(appR.getIdentifier("app_name", "string", ctx.getPackageName()));

        final int list_size = list.size();

        if(list_size >= minTimeOfData*100) { // data acquisition has a rate of approximately 100 times per second

            hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            minutes = Calendar.getInstance().get(Calendar.MINUTE);
            seconds = Calendar.getInstance().get(Calendar.SECOND);
            year = Calendar.getInstance().get(Calendar.YEAR);
            month = Calendar.getInstance().get(Calendar.MONTH) + 1;
            day = Calendar.getInstance().get(Calendar.DATE);

            path = new File(Environment.getExternalStorageDirectory() + "/" + app_name + "/");
            if (!path.exists()) {
                path.mkdir();
            }

            file = new File(path, file_name + "-" + year + "-" + month + "-" + day + "-" + hour + ":" + minutes + ":" + seconds + ".quv");
            fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(file, true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            out = new DataOutputStream(fileOutputStream);

            Thread save = new Thread() {
                @Override
                public void run() {
                    try {
                        StringBuilder sb = new StringBuilder();

                        synchronized (list) {

                            for (int j = 2; j <= list_size-2; j++) {
                                sb.append(list.get(j));
                            }
                            out.writeBytes(sb.toString());

                        }
                        out.close();
                        fileOutputStream.close();

                        file.setReadable(true);
                        file.setWritable(true);

                        Uri contentUri = Uri.fromFile(file);
                        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        mediaScanIntent.setData(contentUri);
                        act.sendBroadcast(mediaScanIntent);

                        playSound("donesaving");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            save.start();
        }
    }

    private static void playSound(String sound){

        if(mp.isPlaying()) {
            mp.stop();
        }
        try {
            mp.reset();
            AssetFileDescriptor afd;
            afd = ctx.getAssets().openFd(sound+".mp3");
            mp.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            mp.prepare();
            mp.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
