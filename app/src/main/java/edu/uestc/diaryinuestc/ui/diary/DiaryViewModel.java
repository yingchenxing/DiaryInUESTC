package edu.uestc.diaryinuestc.ui.diary;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class DiaryViewModel extends AndroidViewModel {

    private String TAG = this.getClass().getSimpleName();
    private DiaryDao diaryDao;
    private DiaryDataBase diaryDB;
    private LiveData<List<Diary>> mAllDiary;

    public DiaryViewModel(@NonNull Application application) {
        super(application);

        diaryDB = DiaryDataBase.getInstance(application);
        diaryDao = diaryDB.diaryDao();
        mAllDiary = diaryDao.getAllDiaries();
    }

    public long insert(Diary diary) {
        try {
            return new InsertAsyncTack(diaryDao).execute(diary).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    LiveData<List<Diary>> getAllDiary() {
        return mAllDiary;
    }

    LiveData<Diary> getDiary(long uid) {
        return diaryDB.diaryDao().getDiary(uid);
    }

    public void update(Diary diary) {
        new UpdateAsyncTack(diaryDao).execute(diary);
    }

    public void delete(Diary diary) {
        new DeleteAsyncTack(diaryDao).execute(diary);
    }


    private class InsertAsyncTack extends AsyncTask<Diary, Void, Long> {
        DiaryDao mAsyncTaskDao;

        public InsertAsyncTack(DiaryDao diaryDao) {
            mAsyncTaskDao = diaryDao;
        }

        @Override
        protected Long doInBackground(Diary... diaries) {
            return mAsyncTaskDao.insert(diaries[0]);
        }
    }

    private class OperationsAsyncTask extends AsyncTask<Diary, Void, Void> {

        DiaryDao mAsyncTaskDao;

        OperationsAsyncTask(DiaryDao dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Diary... diaries) {
            return null;
        }
    }

    private class UpdateAsyncTack extends OperationsAsyncTask {
        public UpdateAsyncTack(DiaryDao diaryDao) {
            super(diaryDao);
        }

        @Override
        protected Void doInBackground(Diary... diaries) {
            mAsyncTaskDao.update(diaries[0]);
            return null;
        }
    }

    private class DeleteAsyncTack extends OperationsAsyncTask {
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
