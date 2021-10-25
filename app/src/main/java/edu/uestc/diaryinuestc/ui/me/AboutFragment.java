package edu.uestc.diaryinuestc.ui.me;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import edu.uestc.diaryinuestc.R;
import edu.uestc.diaryinuestc.databinding.FragmentAboutBinding;

public class AboutFragment extends Fragment {

    private FragmentAboutBinding binding;
    private List<TextView> writers;
    private View.OnClickListener school;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentAboutBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    private void setWriters() {
        TextView mark = writers.get(0);
        while (mark == writers.get(0))
            Collections.shuffle(writers);
        writers.get(0).setText(R.string.jako);
        writers.get(1).setText(R.string.chen_xi);
        writers.get(2).setText(R.string.xin_xuan);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        writers = new ArrayList<TextView>();
        writers.add(binding.writer1);
        writers.add(binding.writer2);
        writers.add(binding.writer3);
        setWriters();

        binding.refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                setWriters();
                binding.title.setTextSize(24);
                refreshLayout.finishRefresh(1000);
            }
        });

        binding.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.title.setTextSize(new Random().nextInt() % 5 + 30);
            }
        });

        school = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "正在访问：电子科技大学主页", Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse("https://www.uestc.edu.cn/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        };

        binding.schoolBadge.setOnClickListener(school);
        binding.uestcTitle.setOnClickListener(school);

        binding.icLauncher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "正在访问：成电微记github", Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse("https://github.com/yingchenxing/DiaryInUESTC");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}