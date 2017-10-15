package com.survey360.quadcoptercontroluv.Utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.ejml.dense.row.decompose.hessenberg.HessenbergSimilarDecomposition_CDRM;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by AAstudillo on 15/10/2017.
 */

public class SaveFile {

    public void saveArrayList(ArrayList<String> arrayList, String filename) {
        try {
            int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            int minutes = Calendar.getInstance().get(Calendar.MINUTE);
            int seconds = Calendar.getInstance().get(Calendar.SECOND);
            int year = Calendar.getInstance().get(Calendar.YEAR);
            int month = Calendar.getInstance().get(Calendar.MONTH)+1;
            int day = Calendar.getInstance().get(Calendar.DATE);
            //Log.e("DATE: ", "###############"+year+"/"+month+"/"+day+"-"+hour+":"+minutes+":"+seconds+"############");
            //File path = Environment.getExternalStoragePublicDirectory(Environment.getExternalStorageDirectory().getAbsolutePath()+"/SmartphoneQuadrotorUV");
            File path = new File(Environment.getExternalStorageDirectory()+"/SmartphoneQuadrotorUV");
            if (!path.exists()) {
                path.mkdir();
            }
            File file = new File(path, filename+"-"+year+"-"+month+"-"+day+"-"+hour+":"+minutes+":"+seconds+".quv");
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);

            ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
            out.writeObject(arrayList);
            out.close();
            fileOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
