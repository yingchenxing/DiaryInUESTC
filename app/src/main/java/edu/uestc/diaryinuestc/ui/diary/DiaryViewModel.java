package edu.uestc.diaryinuestc.ui.diary;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

import edu.uestc.diaryinuestc.utils.AppPathUtils;

public class DiaryViewModel extends AndroidViewModel {

    private final String TAG = this.getClass().getSimpleName();
    private DiaryDao diaryDao;
    private DiaryDataBase diaryDB;
    private LiveData<List<Diary>> mAllDiary;

    public DiaryViewModel(@NonNull Application application) {
        super(application);

        diaryDB = DiaryDataBase.getInstance(application);
        diaryDao = diaryDB.diaryDao();
        mAllDiary = diaryDao.getOrderDiaries();
    }

    public Long insert(Diary diary) {
        try {
            return new InsertAsyncTack(diaryDao).execute(diary).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "fail to insert a instance diary:"+diary.toString());
        return null;
    }

    public LiveData<List<Diary>> getAllDiary() {
        return mAllDiary;
    }

    public Diary getDiary(Long uid) {
        if (uid == null) {
            Log.e(TAG, "获取失败 getDiary from a null uid");
            return null;
        }
        return diaryDB.diaryDao().getDiary(uid);
    }

    public void update(Diary diary) {
        if (diary.getUid() == null){
            Log.e(TAG, "更新失败 update from a null uid" + diary.toString());
            return;
        }
        new UpdateAsyncTack(diaryDao).execute(diary);
    }

    public void delete(Diary diary) {
        if (diary.getUid() == null){
            Log.e(TAG, "删除失败 delete from a null uid" + diary.toString());
            return;
        }
        new DeleteAsyncTack(diaryDao).execute(diary);
        //delete all res data]
        File coverFile = AppPathUtils.getDiaryFile(getApplication(), diary.getUid(), "");
        AppPathUtils.delete(coverFile.getPath());
    }

    private static class InsertAsyncTack extends AsyncTask<Diary, Void, Long> {
        DiaryDao mAsyncTaskDao;

        public InsertAsyncTack(DiaryDao diaryDao) {
            mAsyncTaskDao = diaryDao;
        }

        @Override
        protected Long doInBackground(Diary... diaries) {
            return mAsyncTaskDao.insert(diaries[0]);
        }
    }

    private static class OperationsAsyncTask extends AsyncTask<Diary, Void, Void> {

        DiaryDao mAsyncTaskDao;

        OperationsAsyncTask(DiaryDao dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Diary... diaries) {
            return null;
        }
    }

    private static class UpdateAsyncTack extends OperationsAsyncTask {
        public UpdateAsyncTack(DiaryDao diaryDao) {
            super(diaryDao);
        }

        @Override
        protected Void doInBackground(Diary... diaries) {
            mAsyncTaskDao.update(diaries[0]);
            return null;
        }
    }

    private static class DeleteAsyncTack extends OperationsAsyncTask {
        public DeleteAsyncTack(DiaryDao diaryDao) {
            super(diaryDao);
        }

        @Override
        protected Void doInBackground(Diary... diaries) {
            mAsyncTaskDao.delete(diaries[0]);
            return null;
        }
    }
}
