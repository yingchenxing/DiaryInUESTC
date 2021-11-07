package edu.uestc.diaryinuestc.ui.diary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import edu.uestc.diaryinuestc.R;
import edu.uestc.diaryinuestc.databinding.FragmentDiaryBinding;

public class DiaryFragment extends Fragment {

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


        //加载日记的recyclerView
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);//
        binding.diaryRecyclerView.setLayoutManager(layoutManager);
        adapter = new DiaryAdapter(getContext());
        binding.diaryRecyclerView.setAdapter(adapter);

        DiaryItemTouchHelper callback = new DiaryItemTouchHelper(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(binding.diaryRecyclerView);

        //load viewModel
        diaryViewModel = ViewModelProviders.of(this).get(DiaryViewModel.class);
        diaryViewModel.getAllDiary().observe(this, new Observer<List<Diary>>() {
            @Override
            public void onChanged(List<Diary> diaryList) {
                adapter.setDiaries(diaryList);
            }
        });

        //刷新页面
        binding.diaryRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                adapter.notifyDataSetChanged();
                refreshLayout.finishRefresh(2000);
                Toast.makeText(getContext(), "刷新成功", Toast.LENGTH_SHORT).show();
            }
        });
        //添加
        binding.diaryFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, EditActivity.class);
                intent.putExtra(EditActivity.NEW_TAG, true);
                startActivity(intent);
            }
        });
        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}