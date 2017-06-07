package com.realizer.schoolgenie.managment.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Bhagyashri on 9/12/2016.
 */


/*Then create a class for saving and retrieving the files*/

public class ImageStorage {

    public static String saveToSdCard(Bitmap bitmap, String filename) {

        String stored = null;

        File sdcard = Environment.getExternalStorageDirectory() ;

        File folder = new File(sdcard.getAbsoluteFile(), ".SchoolGenie");//the dot makes this directory hidden to the user
        folder.mkdir();
        File file = new File(folder.getAbsoluteFile(), filename) ;
        if (file.exists())
            return stored ;

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            stored = "success";
            Log.d("SavedPath1",file.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stored;
    }

    public static File getImage(String imagename) {

        File mediaImage = null;
        try {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root);
            if (!myDir.exists())
                return null;

            mediaImage = new File(myDir.getPath() + "/.SchoolGenie/"+imagename);
            Log.d("SavedPath2",mediaImage.getPath());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mediaImage;
    }

    public static boolean checkifImageExists(String imagename)
    {
        Bitmap b = null ;
        File file = ImageStorage.getImage(imagename);
        String path = file.getAbsolutePath();

        if (path != null)
            b = BitmapFactory.decodeFile(path);

        if(b == null ||  b.equals(""))
        {
            return false ;
        }
        return true ;
    }

    public static String getURLImage(String url)
    {
        StringBuilder sb=new StringBuilder();
        for(int k=0;k<url.length();k++)
        {

            if (url.charAt(k) =='\\')
            {
                url.replace("\"","");
                sb.append("/");
            }
            else
            {
                sb.append(url.charAt(k));
            }
        }
        return sb.toString();
    }

    public static void setThumbnail(ImageView userimg, String profilePic) {
        if (profilePic.equals("") || profilePic.equals("null") || profilePic.equals(null)) {

        } else {
            String newURL = getURLImage(profilePic);
            if (!ImageStorage.checkifImageExists(newURL.split("/")[newURL.split("/").length - 1]))
                new GetImages(newURL, userimg, newURL.split("/")[newURL.split("/").length - 1]).execute(newURL);
            else {
                File image = ImageStorage.getImage(newURL.split("/")[newURL.split("/").length - 1]);
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
                userimg.setImageBitmap(bitmap);
            }

        }
    }

}
