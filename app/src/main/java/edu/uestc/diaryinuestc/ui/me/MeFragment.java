package edu.uestc.diaryinuestc.ui.me;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;


import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import edu.uestc.diaryinuestc.AppPathUtils;
import edu.uestc.diaryinuestc.BuildConfig;
import edu.uestc.diaryinuestc.R;
import edu.uestc.diaryinuestc.databinding.FragmentMeBinding;

public class MeFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "MeFragment";
    private FragmentMeBinding binding;
    private Activity activity;

    private PopupWindow popupPhotoSelectorWindow;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        activity = requireActivity();

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
            getPicFromAlbum();

        } else {
            Log.e(TAG, "Unsettled onClick view:" + v.toString());
            Toast.makeText(activity, getResources().getString(R.string.toast_unsettled_onclick)
                    + ":" + v.toString(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 创建并弹出photo selector window
     */
    private void popAvatarSelectorWindow() {
        //创建popupWindow
        View popView = getLayoutInflater().inflate(R.layout.avatar_select_pop, null);
        popupPhotoSelectorWindow = new PopupWindow(popView, (int) (binding.getRoot().getWidth() * 0.8),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupPhotoSelectorWindow.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(),
                R.drawable.round_outline, null));
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
        Window window = activity.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = bg_alpha; //0.0-1.0
        window.setAttributes(lp);
    }

    //临时存储图片
    File tempFile;

    //最终存储图片
    File avatarFile;


    ActivityResultLauncher<Intent> picFromCameraIntentLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Uri contentUri = FileProvider.getUriForFile(activity,
                                    BuildConfig.APPLICATION_ID + ".fileprovider", tempFile);
                            cropPicture(contentUri);
                        } else {
                            cropPicture(Uri.fromFile(tempFile));
                        }
                    }
                }
            });

    /**
     * 从相机获取图片
     */
    private void getPicFromCamera() {
        //临时接收图片文件
        File tempFile = new File(Environment.getExternalStorageDirectory().getPath(), "AvatarPhoto_" + System.currentTimeMillis() + ".png");
        //intent连接image_capture 并用EXTRA_OUTPUT传入tempFile的Uri
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {   //如果在Android7.0以上,使用FileProvider获取Uri
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileprovider", tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        } else {    //否则使用Uri.fromFile(file)方法获取Uri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        }
        picFromCameraIntentLauncher.launch(intent);

    }


    ActivityResultLauncher<Intent> picFromAlbumIntentLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        assert result.getData() != null;
                        cropPicture(result.getData().getData());
                    }
                }
            });

    /**
     * 从手机相册获取图片
     */
    private void getPicFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        picFromAlbumIntentLauncher.launch(intent);
    }


    ActivityResultLauncher<Intent> cropPictureIntentLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getData() != null) {
                        Bitmap bitmap = BitmapFactory.decodeFile(avatarFile.getPath());
                        binding.userAvatar.setImageBitmap(bitmap);
                        if (AppPathUtils.saveImage(avatarFile, bitmap))
                            Toast.makeText(activity, getResources().getString(R.string.toast_success_avatar), Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(activity, getResources().getString(R.string.toast_fail_avatar), Toast.LENGTH_LONG).show();

                    } else {
                        Log.e(TAG, "传入图片为空");
                        Toast.makeText(activity, getResources().getString(R.string.toast_null_picture), Toast.LENGTH_LONG).show();
                    }
                }
            });

    /**
     * 裁剪图片
     *
     * @param uri 图片资源
     */
    private void cropPicture(Uri uri) {
        if (uri == null) return;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // 设置宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 设置裁剪图片宽高
        intent.putExtra("outputX", 100);
        intent.putExtra("outputY", 100);
        intent.putExtra("return-data", false);

        //设置输出文件
        ArrayList<String> paths = new ArrayList<String>();
        paths.add(Environment.getExternalStorageDirectory().getPath());
        paths.add(AppPathUtils.APP_PATH);
        paths.add(AppPathUtils.ME_PATH);
        paths.add(AppPathUtils.AVATAR_PATH);
        avatarFile = new File(AppPathUtils.connectPaths(paths));
        if (!Objects.requireNonNull(avatarFile.getParentFile()).exists())
            if (!avatarFile.getParentFile().mkdirs()) {
                Log.e(TAG, "fail to make dirs in MeFragment cropPicture()");
                Toast.makeText(activity, getResources().getString(R.string.toast_fail_create_dir), Toast.LENGTH_LONG).show();
            }
        //连接intent
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(avatarFile));
        cropPictureIntentLauncher.launch(intent);
    }

}