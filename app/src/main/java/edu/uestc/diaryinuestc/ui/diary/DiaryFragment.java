package edu.uestc.diaryinuestc.ui.diary;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.List;
import java.util.Objects;

import edu.uestc.diaryinuestc.databinding.FragmentDiaryBinding;

public class DiaryFragment extends Fragment {

    private final String TAG = "DiaryFragment";
    private FragmentDiaryBinding binding;
    private DiaryAdapter adapter;
    LiveData<List<Diary>> liveDiaryList;
    private Activity activity;
    private DiaryViewModel diaryViewModel;
    List<Diary> diaryListCache;

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
        liveDiaryList = diaryViewModel.getAllDiary();
        liveDiaryList.observe(this, new Observer<List<Diary>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<Diary> diaryList) {
                adapter.setDiaryList(diaryList);
                diaryListCache = diaryList;
                //all change are rely on this observer
//                liveDiaryList.removeObserver(this);
                adapter.notifyDataSetChanged();
            }
        });

        //刷新页面
        binding.diaryRefreshLayout.setOnRefreshListener(refreshLayout -> {
            List<Diary> diaryList = liveDiaryList.getValue();
            if (diaryList == null)
                return;
            if (!diaryList.equals(diaryListCache)) {
                diaryListCache = diaryList;
                adapter.setDiaryList(diaryList);
            }
            ObjectAnimator animator = ObjectAnimator.ofFloat(binding.diaryRecyclerView, "alpha", 1, 0.5F);
            animator.setDuration(250);
            animator.setRepeatCount(0);
            animator.start();
            ObjectAnimator animator1 = ObjectAnimator.ofFloat(binding.diaryRecyclerView, "alpha", 0.5F, 1);
            animator1.setDuration(250);
            animator1.setRepeatCount(0);
            animator1.setStartDelay(250);
            animator1.start();
//                adapter.notifyDataSetChanged();
            refreshLayout.finishRefresh(500);
            Toast.makeText(getContext(), "已加载 " + adapter.getItemCount() + "条日记", Toast.LENGTH_SHORT).show();
        });
        //添加
        binding.diaryFab.setOnClickListener(v -> {
            Intent intent = new Intent(activity, EditActivity.class);
            intent.putExtra(EditActivity.NEW_TAG, true);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(requireActivity(), binding.diaryFab, binding.diaryFab.getTransitionName());

            startActivity(intent, options.toBundle());
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void setSharedElementReturnTransition(@Nullable Object transition) {
        super.setSharedElementReturnTransition(transition);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}