package edu.uestc.diaryinuestc.ui.diary;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import edu.uestc.diaryinuestc.R;
import edu.uestc.diaryinuestc.databinding.FragmentDiaryBinding;

public class DiaryFragment extends Fragment {

    private DiaryViewModel diaryViewModel;
    private FragmentDiaryBinding binding;
    private DiaryAdapter adapter;
    private List<Diary> diaryList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefresh;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        diaryViewModel =
                new ViewModelProvider(this).get(DiaryViewModel.class);

        binding = FragmentDiaryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textDiary;
        diaryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });

        //加载日记的recyclerView
        initDiary();
        RecyclerView recyclerView = binding.diaryRecyclerView;
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);//
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DiaryAdapter(diaryList);
        recyclerView.setAdapter(adapter);

        DiaryItemTouchHelper callback = new DiaryItemTouchHelper(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        //刷新页面
        swipeRefresh = binding.diarySwipeRefresh;
        swipeRefresh.setColorSchemeResources(R.color.design_default_color_on_primary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(binding.getRoot().getContext(), "成功刷新！",Toast.LENGTH_SHORT).show();
                refreshDiary();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void initDiary(){
        for(int i =0;i<10;i++){
            Diary diary = new Diary(i+"",R.drawable.cover);
            diaryList.add(diary);
        }
    }

    private void refreshDiary() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(200);
                }catch (InterruptedException e){
                    e.printStackTrace();
                };
            }
        }).start();

        initDiary();
        adapter.notifyDataSetChanged();
        swipeRefresh.setRefreshing(false);
    }
}