package edu.uestc.diaryinuestc.ui.me;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.Objects;

import edu.uestc.diaryinuestc.ui.PhotoSelectorPopupWindow;
import edu.uestc.diaryinuestc.utils.AppPathUtils;
import edu.uestc.diaryinuestc.BuildConfig;
import edu.uestc.diaryinuestc.R;
import edu.uestc.diaryinuestc.databinding.FragmentMeBinding;

public class MeFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "MeFragment";
    private FragmentMeBinding binding;
    private Activity activity;

    private PopupWindow popupPhotoSelectorWindow;
    private Bitmap userAvatarBitmap;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        activity = requireActivity();

        loadUserInfo();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnClickListener();
    }

    /**
     * ???path?????????????????????????????????
     */
    private void loadUserInfo() {
        //????????????
        File avatarFile = new File(activity.getFilesDir().getPath(), AppPathUtils.AVATAR_PATH);
        if (avatarFile.exists()) {
            userAvatarBitmap = BitmapFactory.decodeFile(avatarFile.getPath());
            Glide.with(activity).load(userAvatarBitmap).into(binding.userAvatar);
        }
        //???????????????
        SharedPreferences defaultPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        binding.userName.setText(defaultPreferences.getString("name", ""));
        //????????????
        String gender = defaultPreferences.getString("gender", "null");
        int genderDraw;
        if (gender.equals("boy"))
            genderDraw = R.drawable.ic_boy;
        else if (gender.equals("girl"))
            genderDraw = R.drawable.ic_girl;
        else
            genderDraw = R.drawable.ic_boygirl;
        binding.genderIc.setImageResource(genderDraw);
        //????????????
        binding.mySign.setText(defaultPreferences.getString("sign", ""));
    }

    private void setOnClickListener() {
        binding.userAvatar.setOnClickListener(this);
        binding.userName.setOnClickListener(this);
        binding.theme.setOnClickListener(this);
        binding.mySign.setOnClickListener(this);
        binding.genderIc.setOnClickListener(this);
        binding.myInfo.setOnClickListener(this);
        binding.mineHelp.setOnClickListener(this);
        binding.mineSetting.setOnClickListener(this);
        binding.mineAbout.setOnClickListener(this);
        binding.mineSetting.setOnClickListener(this);
        binding.mineTheme.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        loadUserInfo();
        super.onResume();
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
            startActivity(new Intent(activity, MyInfoActivity.class).putExtra("open", "name"));
        } else if (v.getId() == binding.theme.getId() || v.getId() == binding.mineTheme.getId()) {
            Intent intent = new Intent(activity, ThemeSelectActivity.class).putExtra(ThemeSelectActivity.FRAGMENT_KEY, 4);
            startActivity(intent);
        } else if (v.getId() == binding.mySign.getId()) {
            startActivity(new Intent(activity, MyInfoActivity.class).putExtra("open", "sign"));
        } else if (v.getId() == binding.genderIc.getId()) {
            startActivity(new Intent(activity, MyInfoActivity.class).putExtra("open", "gender"));
        } else if (v.getId() == binding.myInfo.getId()) {
//            startActivity(new Intent(activity, PhotoSelectorPopupWindow.class).putExtra("isBottom", false));
            startActivity(new Intent(activity, MyInfoActivity.class));
        } else if (v.getId() == binding.mineHelp.getId()) {
            Toast.makeText(getContext(), "????????? ?????? ??????github????????????", Toast.LENGTH_LONG).show();
        } else if (v.getId() == binding.mineSetting.getId()) {
            AlertDialog dialog = new AlertDialog.Builder(activity)
                    .setMessage("??????????????????\n???????????? \"?????????????????????\" ??????\n?????????????????? \"????????????\" ??????")
                    .create();
            dialog.setButton(Dialog.BUTTON_POSITIVE, "??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else if (v.getId() == binding.mineAbout.getId()) {
            Toast.makeText(getContext(), "????????????????????????\n???????????????????????????GitHub", Toast.LENGTH_LONG).show();
            startActivity(new Intent(activity, AboutActivity.class));
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
     * ???????????????photo selector window
     */
    private void popAvatarSelectorWindow() {
        //??????popupWindow
        View popView = getLayoutInflater().inflate(R.layout.photo_select_pop_center, null);

//        Log.e(TAG, String.valueOf(binding.getRoot().getWidth())+"  "+ getResources().getDisplayMetrics().widthPixels);

        popupPhotoSelectorWindow = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupPhotoSelectorWindow.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(),
                R.drawable.round_outline_white, null));
        popupPhotoSelectorWindow.setOutsideTouchable(true);
        popupPhotoSelectorWindow.setFocusable(true);
        popupPhotoSelectorWindow.setAnimationStyle(R.style.popupWindow_anim_style);

        //????????????????????????
        View popCameraBtn = popView.findViewById(R.id.pop_camera);
        View popAlbumBtn = popView.findViewById(R.id.pop_album);
        popCameraBtn.setOnClickListener(this);
        popAlbumBtn.setOnClickListener(this);

        //????????????
        popupPhotoSelectorWindow.showAtLocation(binding.getRoot(), Gravity.CENTER, 0, 0);

        //????????????
        backgroundAlpha(0.5f);
        popupPhotoSelectorWindow.setOnDismissListener(() -> backgroundAlpha(1.0f));

    }

    /**
     * ????????????????????????????????????
     *
     * @param bg_alpha 1.0f ?????????????????????
     */
    public void backgroundAlpha(float bg_alpha) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = bg_alpha; //0.0-1.0
        window.setAttributes(lp);
    }

    //??????????????????
    File tempFile;

    //??????????????????
    File avatarFile;

    ActivityResultLauncher<Intent> picFromCameraIntentLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (tempFile != null && tempFile.exists()) {
                        Log.e(TAG, "????????????????????????");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Uri contentUri = FileProvider.getUriForFile(activity,
                                    BuildConfig.APPLICATION_ID + ".fileprovider", tempFile);
                            cropPicture(contentUri);
                        } else {
                            cropPicture(Uri.fromFile(tempFile));
                        }
                    } else {
                        assert tempFile != null;
                        Log.e(TAG, "????????????????????????" + "file:" + tempFile.toString() + " code:" + result.getResultCode());
                    }
                }
            });

    /**
     * ?????????????????????
     */
    private void getPicFromCamera() {
        //??????????????????AvatarPhoto

        //????????????????????????
        tempFile = new File(activity.getCacheDir() + "/AvatarPhoto_" + System.currentTimeMillis() + ".png");
        //intent??????image_capture ??????EXTRA_OUTPUT??????tempFile???Uri
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {   //?????????Android7.0??????,??????FileProvider??????Uri
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileprovider", tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        } else {    //????????????Uri.fromFile(file)????????????Uri
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
     * ???????????????????????????
     */
    private void getPicFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        picFromAlbumIntentLauncher.launch(intent);
    }

    private final ActivityResultLauncher<CropImageContractOptions> cropImage =
            registerForActivityResult(new CropImageContract(), new ActivityResultCallback<CropImageView.CropResult>() {
                @Override
                public void onActivityResult(CropImageView.CropResult result) {
                    if (result.isSuccessful()) {
                        userAvatarBitmap = BitmapFactory.decodeFile(result.getUriFilePath(activity, false));
//                        userAvatarBitmap = result.getBitmap();
                        if (userAvatarBitmap == null) {
                            Log.e(TAG, "null bitmap! path=" + result.getUriFilePath(activity, false));
                            Toast.makeText(activity, getResources().getString(R.string.toast_null_picture), Toast.LENGTH_LONG).show();
                        } else {
                            if (AppPathUtils.saveImage(avatarFile, userAvatarBitmap)) {
                                Toast.makeText(activity, getResources().getString(R.string.toast_success_save_avatar), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(activity, getResources().getString(R.string.toast_fail_save_avatar), Toast.LENGTH_LONG).show();
                            }
                            Glide.with(activity).load(userAvatarBitmap).into(binding.userAvatar);
                        }
                    } else {
                        Log.e(TAG, "Error in CropImage" + Objects.requireNonNull(result.getError()).toString());
                        Toast.makeText(activity, "Error in CropImage" + Objects.requireNonNull(result.getError()).toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });

    /**
     * ????????????<br><br/>
     *
     * @param uri ????????????
     */
    private void cropPicture(Uri uri) {
        avatarFile = new File(activity.getFilesDir().getPath(), AppPathUtils.AVATAR_PATH);
        CropImageContractOptions options = new CropImageContractOptions(uri, new CropImageOptions())
                .setScaleType(CropImageView.ScaleType.CENTER)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .setMaxZoom(8)
                .setAutoZoomEnabled(false)
                .setMultiTouchEnabled(true)
                .setCenterMoveEnabled(true)
                .setShowCropOverlay(true)
                .setAllowFlipping(true)
                .setSnapRadius(30f)
                .setTouchRadius(48f)
                .setInitialCropWindowPaddingRatio(0.1f)
                .setBorderLineThickness(3f)
                .setBorderLineColor(R.color.black)
                .setBorderCornerThickness(2f)
                .setBorderCornerOffset(2f)
                .setBorderCornerLength(20f)
//                .setBorderCornerColor(R.color.RED)
                .setGuidelinesThickness(5f)
//                .setGuidelinesColor(RED)
//                .setBackgroundColor(Color.argb(119, 30, 60, 90))
                .setMinCropWindowSize(42, 42)
                .setMinCropResultSize(40, 40)
                .setMaxCropResultSize(999, 999)
                .setActivityTitle(getResources().getString(R.string.crop_image_activity_title))
//                .setActivityMenuIconColor(RED)
                /*
                 * set out put settings
                 * NONE uri will create a temp file
                 * ?????????????????????fileprovider??????uri
                 * !!!
                 */
//                .setOutputUri()
                .setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                .setOutputCompressQuality(100)
//                .setRequestedSize(100, 100)
//                .setRequestedSize(100, 100, CropImageView.RequestSizeOptions.RESIZE_FIT)
//                .setInitialCropWindowRectangle(null)
                .setInitialRotation(180)
                .setAllowCounterRotation(true)
                .setFlipHorizontally(true)
                .setFlipVertically(true)
                .setCropMenuCropButtonTitle(getResources().getString(R.string.crop_image_button_title))
                .setCropMenuCropButtonIcon(R.drawable.ic_baseline_aspect_ratio_24)
                .setAllowRotation(true)
                .setNoOutputImage(false)
                .setFixAspectRatio(true);

        cropImage.launch(options);
    }

}