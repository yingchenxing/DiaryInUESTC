package edu.uestc.diaryinuestc.ui.me;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import edu.uestc.diaryinuestc.databinding.FragmentMeBinding;

public class MeFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "MeFragment";
    private FragmentMeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setOnClickListener();



        return root;
    }

    private void setOnClickListener() {
        binding.userAvatar.setOnClickListener(this);
        binding.userName.setOnClickListener(this);
        binding.myInfo.setOnClickListener(this);
        binding.mineSetting.setOnClickListener(this);
        binding.mineAbout.setOnClickListener(this);
        binding.mineSetting.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == binding.userAvatar.getId()) {

        } else if (v.getId() == binding.userName.getId()) {

        } else if (v.getId() == binding.myInfo.getId()){

        } else if (v.getId() == binding.mineHelp.getId()){

        } else if (v.getId() == binding.mineSetting.getId()){

        } else if (v.getId() == binding.mineAbout.getId()){
            
        } else {
            Log.e(TAG, "Unsettled onClick view:"+v.toString());
        }
    }
}