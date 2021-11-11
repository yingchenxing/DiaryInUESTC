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
     * @param prefix  文件前缀
     * @param suffix  文件后缀
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

    /**
     * 获取Diary资源文件(图片)
     *
     * @param context 上下文获取Files目录
     * @param uid 根据uid得到相应diary的文件
     * @param name 在对应diary目录下的内容
     * @return (未检查是否存在的)File文件
     */
    public static File getDiaryFile(@NonNull Context context, Long uid, String name) {
        StringBuilder sb = new StringBuilder(context.getFilesDir().getPath());
        sb.append('/').append(DIARY_PATH).append('/').append(uid).append('/');
        if (name == null || name.length() == 0)
            return new File(sb.toString());
        return new File(sb.append(name).toString());
    }

    /**
     * 删除文件，可以是文件或文件夹
     *
     * @param fileName：要删除的文件名
     * @return 删除成功返回true，否则返回false
     */
    public static boolean delete(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            Log.e(TAG, "删除文件失败:" + fileName + "不存在！");
            return false;
        } else {
            if (file.isFile())
                return deleteFile(fileName);
            else
                return deleteDirectory(fileName);
        }
    }

    /**
     * 删除单个文件
     *
     * @param fileName：要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                Log.d(TAG, "删除单个文件" + fileName + "成功！");
                return true;
            } else {
                Log.e(TAG, "删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            Log.e(TAG, "删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param dir：要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator;
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            Log.e(TAG, "删除目录失败：" + dir + "不存在！");
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            Log.e(TAG, "删除目录失败！");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            Log.d(TAG, "删除目录" + dir + "成功！");
            return true;
        } else {
            return false;
        }
    }

}
