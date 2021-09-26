package edu.uestc.diaryinuestc;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class AppPathUtils {

    private static final String TAG = "AppPathUtils";

    public static final String APP_PATH = "DiaryInUESTC";

    public static final String Bill_PATH = "Bill";
    public static final String DIARY_PATH = "Diary";
    public static final String ME_PATH = "Me";
    public static final String TODO_PATH = "TODO";

    public static final String AVATAR_NAME = "user_avatar.png";
    public static final String AVATAR_PATH = "Me/user_avatar.png";

    public static String connectPaths(ArrayList<String> paths) {
        if (paths == null || paths.size() == 0)
            return null;
        StringBuilder sb = new StringBuilder();
        for (String path : paths)
            sb.append(path + "/");
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static boolean saveImage(File path, Bitmap bitmap) {
        if (path == null) {
            Log.e(TAG + "saveImage", "null path");
            return false;
        }
        if (bitmap == null) {
            Log.e(TAG + "saveImage", "null bitmap");
            return false;
        }
//        if (!path.isFile()) {
//            Log.e(TAG + "saveImage", "path is not file" + path.getPath());
//            return false;
//        }

        if (!Objects.requireNonNull(path.getParentFile()).exists())
            if (!path.getParentFile().mkdirs()) {
                Log.e(TAG + "saveImage", "fail to make dirs");
                return false;
            }
        try {
            FileOutputStream fos = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            return true;
        } catch (IOException e) {
            Log.e(TAG + "saveImage", e.toString());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean saveImage(String path, Bitmap bitmap) {
        if (path == null) {
            Log.e(TAG + "saveImage", "null path String");
            return false;
        }
        return saveImage(new File(path), bitmap);

    }

    public static boolean saveImage(ArrayList<String> paths, Bitmap bitmap) {
        return saveImage(connectPaths(paths), bitmap);
    }


}
