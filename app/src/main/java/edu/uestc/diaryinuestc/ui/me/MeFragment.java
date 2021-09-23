package edu.uestc.diaryinuestc.ui.me;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import edu.uestc.diaryinuestc.databinding.FragmentMeBinding;

public class MeFragment extends Fragment implements View.OnClickListener {

    private FragmentMeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
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
            
        }
    }
}