package edu.uestc.diaryinuestc.ui.diary;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.transition.Explode;
import android.transition.Visibility;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.SharedElementCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.uestc.diaryinuestc.R;
import edu.uestc.diaryinuestc.databinding.FragmentDiaryBinding;

public class DiaryFragment extends Fragment {

    private final String TAG = "DiaryFragment";
    private FragmentDiaryBinding binding;
    private DiaryAdapter adapter;
    private List<Diary> diaryList;
    private Activity activity;
    private DiaryViewModel diaryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDiaryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        activity = requireActivity();

        //load view model
        diaryViewModel = ViewModelProviders.of(this).get(DiaryViewModel.class);
        //加载日记的recyclerView
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);//
        binding.diaryRecyclerView.setLayoutManager(layoutManager);
        adapter = new DiaryAdapter(Objects.requireNonNull(getContext()), diaryViewModel);
        binding.diaryRecyclerView.setAdapter(adapter);

        DiaryItemTouchHelper callback = new DiaryItemTouchHelper(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(binding.diaryRecyclerView);

        //load list
        LiveData<List<Diary>> liveDiaryList = diaryViewModel.getAllDiary();
        liveDiaryList.observe(this, new Observer<List<Diary>>() {
            @Override
            public void onChanged(List<Diary> diaryList) {
                adapter.setDiaryList(diaryList);
                //all change are rely on this observer
//                liveDiaryList.removeObserver(this);
            }
        });

        //刷新页面
        binding.diaryRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                adapter.notifyDataSetChanged();
                refreshLayout.finishRefresh(500);
                Toast.makeText(getContext(), "已加载 " + adapter.getItemCount() + "条日记", Toast.LENGTH_SHORT).show();
            }
        });
        //添加
        binding.diaryFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, EditActivity.class);
                intent.putExtra(EditActivity.NEW_TAG, true);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) getActivity(), binding.diaryFab, binding.diaryFab.getTransitionName());

                startActivity(intent, options.toBundle());
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}