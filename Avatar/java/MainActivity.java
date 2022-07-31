package com.example.review_material;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    DrawerLayout drawer;

    List<Picture> list = new ArrayList<>();

    private final Picture[] arr = {new Picture("pic_1", R.mipmap.pic_1),
            new Picture("pic_2", R.mipmap.pic_2),
            new Picture("pic_3", R.mipmap.pic_3),
            new Picture("pic_4", R.mipmap.pic_4),
            new Picture("pic_5", R.mipmap.pic_5),
            new Picture("pic_6", R.mipmap.pic_6),
            new Picture("pic_7", R.mipmap.pic_7),
            new Picture("pic_8", R.mipmap.pic_8)};

    private CircleImageView circleImage;

    private View headerView;

    private Uri output_uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_dehaze_24);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        initList();
        RecyclerView recyclerView = findViewById(R.id.recycler);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        ImageAdapter adapter = new ImageAdapter(list);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        NavigationView nav = findViewById(R.id.nav);
        headerView = nav.getHeaderView(0);
        circleImage = headerView.findViewById(R.id.circle_image);
        circleImage.setOnClickListener(this);
        headerView.setOnClickListener(this);
        // 如果已经设置过头像，就显示设置的头像
        File temp = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "avatar.jpg");
        if (temp.exists() && temp.length() > 0) {
            Glide.with(this)
                    .load(temp)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(circleImage);
        }
    }

    private void initList() {
        list.clear();
        for (int i = 0; i < 50; i++) {
            Random random = new Random();
            int index = random.nextInt(arr.length);
            list.add(arr[index]);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            drawer.openDrawer(GravityCompat.START);
        }
        return true;
    }

    // 为头像设置点击事件
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.circle_image) {
            initPopup(view);
        } else if (id == R.id.background) {
            selectPhoto();
        }
    }

    // 初始化PopupWindow
    private void initPopup(View v0) {
        View view = LayoutInflater.from(this).inflate(R.layout.popup_item, null, false);
        Button btn_select = view.findViewById(R.id.popup_btn_select);
        Button btn_take = view.findViewById(R.id.popup_btn_take);
        Button btn_cancel = view.findViewById(R.id.popup_btn_cancel);

        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x0000_0000));

        popupWindow.showAsDropDown(v0, 50, 0);
        btn_select.setOnClickListener(v -> {
            selectPhoto();
        });
        btn_take.setOnClickListener(v -> {
            takePhoto();
        });
        btn_cancel.setOnClickListener(v -> {
            popupWindow.dismiss();
        });
    }

    // 拍照回调
    ActivityResultLauncher<Uri> takePhotoLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if (result) {   // result = (responseCode == RESULT_OK)
                        File tarFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "avatar.jpg");
                        File srcFile = new File(getExternalCacheDir(), "avatar_temp.jpg");
                        copy(tarFile, srcFile);
                        Glide.with(MainActivity.this)
                                .load(output_uri)
                                .skipMemoryCache(true)          // 不读取缓存中的内容
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .into(circleImage);
                    }
                }
            });

    // 选择照片回调
    ActivityResultLauncher<String> selectPhotoLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if (result != null) {
                        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "avatar.jpg");
                        copy(file, result);     // 将result复制到file中
                        Glide.with(MainActivity.this)
                                .load(result)
                                .into(circleImage);
                        /*
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(result));
                            headerView.setBackground(new BitmapDrawable(bitmap));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }   */
                    }
                }
            });

    // 拍照作为头像
    private void takePhoto() {
        // 将拍照结果储存到缓存文件夹
        File image_file = new File(getExternalCacheDir(), "avatar_temp.jpg");
        try {
            if (image_file.exists()) {
                image_file.delete();
            }
            image_file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24) {
            output_uri = FileProvider.getUriForFile(this, "com.example.review_material", image_file);
        } else {
            output_uri = Uri.fromFile(image_file);
        }
        /*
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, output_uri);   */
        takePhotoLauncher.launch(output_uri);
    }

    // 选择照片
    private void selectPhoto() {
        selectPhotoLauncher.launch("image/*");
    }

    // 将uri内的内容写入指定的File对象
    private void copy(File file, Uri uri) {
        if (file.exists()) {
            file.delete();
        }
        try (
                InputStream is = getContentResolver().openInputStream(uri);
                OutputStream os = new FileOutputStream(file);
        )
        {
            file.createNewFile();
            byte[] b = new byte[1024];
            while (is.read(b) != -1) {
                os.write(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 将srcFile内的内容写入tarFile
    private void copy(File tarFile, File srcFile) {
        if (tarFile.exists()) {
            tarFile.delete();
        }
        try (
                InputStream is = new FileInputStream(srcFile);
                OutputStream os = new FileOutputStream(tarFile);
        )
        {
            tarFile.createNewFile();
            byte[] b = new byte[1024];
            while (is.read(b) != -1) {
                os.write(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}