package edu.uestc.diaryinuestc.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

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

    /**
     * 将ArrayList中各个路径连接成路径字符串
     *
     * @param paths 路径ArrayList
     * @return pathString
     */
    public static String connectPaths(ArrayList<String> paths) {
        if (paths == null || paths.size() == 0)
            return null;
        StringBuilder sb = new StringBuilder();
        for (String path : paths)
            sb.append(path).append("/");
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * 把bitmap存入file中
     *
     * @param file   目标文件
     * @param bitmap 图片Bitmap
     * @return isSuccess
     */
    public static boolean saveImage(File file, Bitmap bitmap) {
        if (file == null) {
            Log.e(TAG + "saveImage", "null path");
            return false;
        }
        if (bitmap == null) {
            Log.e(TAG + "saveImage", "null bitmap");
            return false;
        }

        if (!Objects.requireNonNull(file.getParentFile()).exists())
            if (!file.getParentFile().mkdirs()) {
                Log.e(TAG + "saveImage", "fail to make dirs");
                return false;
            }
        try {
            FileOutputStream fos = new FileOutputStream(file);
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

    /**
     * 创建临时文件
     *
     * @param context 上下文获取cache目录
     * @param prefix 文件前缀
     * @param suffix 文件后缀
     * @return File对象
     */
    public static File createTempFile(@NonNull Context context, String prefix, @NonNull String suffix) {
        StringBuilder sb = new StringBuilder();
        sb.append(context.getCacheDir()).append('/').append(prefix).append('_').append(System.currentTimeMillis());
        if (suffix.charAt(0) != '.')
            sb.append('.');
        sb.append(suffix);
        return new File(sb.toString());
    }

}
