package edu.uestc.diaryinuestc.ui.me;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;


import edu.uestc.diaryinuestc.R;
import edu.uestc.diaryinuestc.databinding.FragmentMeBinding;

public class MeFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "MeFragment";
    private FragmentMeBinding binding;
    private PopupWindow popupPhotoSelectorWindow;

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
            popAvatarSelectorWindow();
        } else if (v.getId() == binding.userName.getId()) {

        } else if (v.getId() == binding.myInfo.getId()) {

        } else if (v.getId() == binding.mineHelp.getId()) {

        } else if (v.getId() == binding.mineSetting.getId()) {

        } else if (v.getId() == binding.mineAbout.getId()) {

        } else if (v.getId() == R.id.pop_camera) {
            popupPhotoSelectorWindow.dismiss();
            getPicFromCamera();

        } else if (v.getId() == R.id.pop_album) {
            popupPhotoSelectorWindow.dismiss();

        } else {
            Log.e(TAG, "Unsettled onClick view:" + v.toString());
        }
    }

    private void popAvatarSelectorWindow() {
        //创建popupWindow
        View popView = getLayoutInflater().inflate(R.layout.avatar_select_pop, null);
        popupPhotoSelectorWindow = new PopupWindow(popView, (int) (binding.getRoot().getWidth() * 0.8),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupPhotoSelectorWindow.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.round_outline, null));
        popupPhotoSelectorWindow.setOutsideTouchable(true);
        popupPhotoSelectorWindow.setFocusable(true);
        popupPhotoSelectorWindow.setAnimationStyle(R.style.popupWindow_anim_style);

        //设置弹出内容监听
        TextView popCameraBtn = popView.findViewById(R.id.pop_camera);
        TextView popAlbumBtn = popView.findViewById(R.id.pop_album);
        popCameraBtn.setOnClickListener(this);
        popAlbumBtn.setOnClickListener(this);

        //弹出窗口
        popupPhotoSelectorWindow.showAtLocation(binding.getRoot(), Gravity.CENTER, 0, 0);

        //淡化背景
        backgroundAlpha(0.5f);
        popupPhotoSelectorWindow.setOnDismissListener(() -> backgroundAlpha(1.0f));

    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bg_alpha 1.0f 全透明没有阴影
     */
    public void backgroundAlpha(float bg_alpha) {
        Window window = requireActivity().getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = bg_alpha; //0.0-1.0
        window.setAttributes(lp);
    }

    public void getPicFromCamera() {

    }
}