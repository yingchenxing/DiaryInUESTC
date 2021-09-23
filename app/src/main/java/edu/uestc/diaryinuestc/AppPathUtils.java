package edu.uestc.diaryinuestc;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class AppPathUtils {

    public static final String APP_PATH = "DiaryInUESTC";

    public static final String Bill_PATH = "Bill";
    public static final String DIARY_PATH = "Diary";
    public static final String ME_PATH = "Me";
    public static final String TODO_PATH = "TODO";

    public static final String AVATAR_PATH = "user_avatar.png";

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
        if (path == null || bitmap == null || !path.isFile()) return false;

        if (!Objects.requireNonNull(path.getParentFile()).exists())
            if (!path.getParentFile().mkdirs())
                return false;
        try {
            FileOutputStream fos = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean saveImage(String path, Bitmap bitmap) {
        if (path == null || bitmap == null)
            return false;
        return saveImage(new File(path), bitmap);

    }

    public static boolean saveImage(ArrayList<String> paths, Bitmap bitmap) {
        if (paths == null || bitmap == null) return false;
        return saveImage(connectPaths(paths), bitmap);
    }


}
