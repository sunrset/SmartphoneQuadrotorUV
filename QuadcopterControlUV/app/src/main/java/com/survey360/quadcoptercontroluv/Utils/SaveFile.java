package com.survey360.quadcoptercontroluv.Utils;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;

import org.ejml.dense.row.decompose.hessenberg.HessenbergSimilarDecomposition_CDRM;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by AAstudillo on 15/10/2017.
 */

public class SaveFile {

    private final Context ctx;
    private File file, path;
    private int hour, minutes, seconds, year, month, day;

    public SaveFile (Context context){
        this.ctx = context;
    }

    public void saveArrayList(ArrayList<String> arrayList, String filename) {
        try {
            hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            minutes = Calendar.getInstance().get(Calendar.MINUTE);
            seconds = Calendar.getInstance().get(Calendar.SECOND);
            year = Calendar.getInstance().get(Calendar.YEAR);
            month = Calendar.getInstance().get(Calendar.MONTH)+1;
            day = Calendar.getInstance().get(Calendar.DATE);

            path = new File(Environment.getExternalStorageDirectory()+"/SmartphoneQuadrotorUV/");
            if (!path.exists()) {path.mkdir();}

            file = new File(path, filename+"-"+year+"-"+month+"-"+day+"-"+hour+":"+minutes+":"+seconds+".quv");
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);

            DataOutputStream out = new DataOutputStream(fileOutputStream);
            out.writeBytes(arrayList.toString());
            out.close();
            fileOutputStream.close();

            file.setReadable(true);
            file.setWritable(true);
            MediaScannerConnection.scanFile(this.ctx, new String[] {file.toString()}, null, null);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
