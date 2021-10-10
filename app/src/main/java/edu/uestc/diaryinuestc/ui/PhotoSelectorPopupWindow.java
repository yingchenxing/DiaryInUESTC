package edu.uestc.diaryinuestc.ui;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;

import java.io.File;
import java.util.Objects;

import edu.uestc.diaryinuestc.BuildConfig;
import edu.uestc.diaryinuestc.R;
import edu.uestc.diaryinuestc.databinding.PhotoSelectPopBottomBinding;
import edu.uestc.diaryinuestc.databinding.PhotoSelectPopCenterBinding;
import edu.uestc.diaryinuestc.ui.me.ThemeSelectActivity;
import edu.uestc.diaryinuestc.utils.AppPathUtils;

public class PhotoSelectorPopupWindow extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "PhotoSelectorWindow";
    private boolean isBottom;
    private PhotoSelectPopBottomBinding bindingBottom;
    private PhotoSelectPopCenterBinding bindingCenter;
    private RelativeLayout layout;
    private TextView camera;
    private TextView album;
    private CardView cancel;

    private File tempFile;
    private File target;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        //获取目的文件
        String path = getIntent().getStringExtra("Path");
        if (path == null)
            target = AppPathUtils.createTempFile(this, null, ".png");
        else
            target = new File(path);
        if (!Objects.requireNonNull(target.getParentFile()).exists())
            target.getParentFile().mkdirs();
        //选择在底部还是中间显示
        isBottom = getIntent().getBooleanExtra("isBottom", true);
        if (isBottom) {
            overridePendingTransition(R.anim.push_bottom_in, R.anim.fadeout);
            bindingBottom = PhotoSelectPopBottomBinding.inflate(getLayoutInflater());
            setContentView(bindingBottom.getRoot());
            layout = bindingBottom.photoSelectPopLayout;
            camera = bindingBottom.popCamera;
            album = bindingBottom.popAlbum;
            cancel = bindingBottom.popPhotoCancel;
            cancel.setOnClickListener(this);
        } else {
            overridePendingTransition(R.anim.fadein, 0);
            bindingCenter = PhotoSelectPopCenterBinding.inflate(getLayoutInflater());
            setContentView(bindingCenter.getRoot());
            layout = bindingCenter.photoSelectPopLayout;
            camera = bindingCenter.popCamera;
            album = bindingCenter.popAlbum;
        }
        layout.setOnClickListener(this);
        camera.setOnClickListener(this);
        album.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.photo_select_pop_layout){
            this.finish();
        } else if (v.getId() == R.id.pop_camera) {
            getPicFromCamera();
        } else if (v.getId() == R.id.pop_album) {
            getPicFromAlbum();
        } else if (v.getId() == R.id.pop_photo_cancel) {
            this.finish();
        } else {
            Log.e(TAG, "Unsettled onClick view:" + v.toString());
            Toast.makeText(this, getResources().getString(R.string.toast_unsettled_onclick)
                    + ":" + v.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    ActivityResultLauncher<Intent> picFromCameraIntentLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (tempFile != null && tempFile.exists()) {
                        Log.e(TAG, "成功获取拍照图片");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Uri contentUri = FileProvider.getUriForFile(PhotoSelectorPopupWindow.this,
                                    BuildConfig.APPLICATION_ID + ".fileprovider", tempFile);
                            cropPicture(contentUri);
                        } else {
                            cropPicture(Uri.fromFile(tempFile));
                        }
                    } else {
                        assert tempFile != null;
                        Log.e(TAG, "获取拍照图片失败" + "file:" + tempFile.toString() + " code:" + result.getResultCode());
                    }
                }
            });

    /**
     * 从相机获取图片
     */
    private void getPicFromCamera() {
        //顺便删除其他临时文件

        //临时接收图片文件
        tempFile = AppPathUtils.createTempFile(this, "Photo", ".png");
        //intent连接image_capture 并用EXTRA_OUTPUT传入tempFile的Uri
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {   //如果在Android7.0以上,使用FileProvider获取Uri
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(this, this.getPackageName() + ".fileprovider", tempFile);
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


    private final ActivityResultLauncher<CropImageContractOptions> cropImage =
            registerForActivityResult(new CropImageContract(), new ActivityResultCallback<CropImageView.CropResult>() {
                @Override
                public void onActivityResult(CropImageView.CropResult result) {
                    if (result.isSuccessful()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(result.getUriFilePath(PhotoSelectorPopupWindow.this, false));
                        if (bitmap == null) {
                            Log.e(TAG, "null bitmap! path=" + result.getUriFilePath(PhotoSelectorPopupWindow.this, false));
                            Toast.makeText(PhotoSelectorPopupWindow.this, getResources().getString(R.string.toast_null_picture), Toast.LENGTH_LONG).show();
                        } else {
                            AppPathUtils.saveImage(target, bitmap);
                            PhotoSelectorPopupWindow.this.setResult(Activity.RESULT_OK, new Intent().putExtra("path", target.getPath()));
                        }
                        PhotoSelectorPopupWindow.this.finish();
                    } else {
                        Log.e(TAG, "Error in CropImage" + Objects.requireNonNull(result.getError()).toString());
                        Toast.makeText(PhotoSelectorPopupWindow.this, "Error in CropImage" + Objects.requireNonNull(result.getError()).toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });

    /**
     * 裁剪图片<br><br/>
     *
     * @param uri 图片资源
     */
    private void cropPicture(Uri uri) {
//        avatarFile = new File(this.getFilesDir().getPath(), AppPathUtils.AVATAR_PATH);
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
                 * 不设置是避免从fileprovider获取uri
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