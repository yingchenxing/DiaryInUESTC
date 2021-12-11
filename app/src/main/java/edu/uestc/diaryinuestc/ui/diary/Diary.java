package edu.uestc.diaryinuestc.ui.diary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.File;

import edu.uestc.diaryinuestc.utils.AppPathUtils;

@Entity
public class Diary {
    public static final String COVER_PNG = "cover.png";

    @ColumnInfo(name = "uid")
    @PrimaryKey(autoGenerate = true)
    private Long uid;
    @ColumnInfo(name = "date")
    private String date;
    @ColumnInfo(name = "location")
    private String location;
    @ColumnInfo(name = "millis")
    private Long millis;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "content")
    private String content;

    public Diary() {
    }

    @Ignore
    public Diary(Long uid) {
        this.uid = uid;
    }

    public String[] requirePreview() {
        String[] preview = {"", ""};
        if (!checkText()) {
            preview[1] = "--万事皆\"空\"~";
            return preview;
        }
        int preview_len = 80;
        int title_len = 14;

        //简化content
        StringBuilder sc = new StringBuilder();
        if (content != null && content.length() != 0) {
            for (String str : content.split("\n")) {
                str = str.trim();
                if (str.length() > 0)
                    sc.append(str);
                if (sc.length() >= preview_len) {
                    sc.delete(preview_len, sc.length() - 1);
                    break;
                }
                sc.append('\n');
            }
            if (sc.charAt(sc.length() - 1) == '\n')
                sc.deleteCharAt(sc.length() - 1);
        }

        if (getTitle() != null && getTitle().length() != 0) {
            //不需填充
            preview[0] = getTitle();
            preview[1] = sc.toString();
            return preview;
        }

        //sc填充到title,切开
        if (sc.length() <= title_len) {
            preview[0] = sc.toString();
        } else {
            int i;
            for (i = 0; i < title_len; ++i) {
                if (sc.charAt(i) == '\n') {
                    i--;
                    break;
                }
            }
            preview[0] = sc.substring(0, i);
            if (sc.charAt(i + 1) == '\n') i++;
            preview[1] = sc.substring(i + 1);
        }

        return preview;
    }

    public Bitmap requireCover(@NonNull Context context) {
        File coverFile = requireCoverFile(context);
        Bitmap cover = null;
        if (coverFile.exists())
            cover = BitmapFactory.decodeFile(coverFile.getPath());
        return cover;
    }

    public File requireCoverFile(@NonNull Context context) {
        return AppPathUtils.getDiaryFile(context, uid, COVER_PNG);
    }

    public boolean isEmpty(@NonNull Context context) {
        return !checkText() && !checkRes(context);
    }

    /**
     * 检查资源是否存在文件(图片)
     *
     * @param context context获取文件
     * @return true 存在 false 不存在
     */
    public boolean checkRes(@NonNull Context context) {
        File diaryFile = AppPathUtils.getDiaryFile(context, uid, null);
        File[] files = diaryFile.listFiles();
        if (files == null || files.length == 0)
            return false;
        for (File file : files) {
            if (file.isFile() && file.exists())
                return true;
        }
        return false;
    }

    /**
     * 检查diary是否存在文字内容
     *
     * @return true 存在 false 不存在
     */
    public boolean checkText() {

        return !((content == null || content.length() == 0) && (title == null || title.length() == 0) && (location == null || location.length() == 0));
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getMillis() {
        return millis;
    }

    public void setMillis(Long millis) {
        this.millis = millis;
    }

    @Override
    public String toString() {
        return "Diary{" +
                "uid=" + uid +
                ", date='" + date + '\'' +
                ", millis=" + millis +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
