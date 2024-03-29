package com.example.simulatepositioning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BgImage extends AppCompatActivity implements View.OnClickListener {

    private ImageButton bgimage1,bgimage2,bgimage3,bgimage4,bgimage5,bgimage6,bgimage7;
    private Button btn,add;
    private final int IMAGE_RESULT_CODE = 2;
    private final int PICK = 1;
    private String imgString = "";
    Bitmap bitmap=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bg_image);

        bgimage1=findViewById(R.id.bgimage1);
        bgimage2=findViewById(R.id.bgimage2);
        bgimage3=findViewById(R.id.bgimage3);
        bgimage4=findViewById(R.id.bgimage4);
        bgimage5=findViewById(R.id.bgimage5);
        bgimage6=findViewById(R.id.bgimage6);
        bgimage7=findViewById(R.id.bgimage7);
        btn=findViewById(R.id.btn);
        add=findViewById(R.id.add);

        bgimage1.setOnClickListener(this);
        bgimage2.setOnClickListener(this);
        bgimage3.setOnClickListener(this);
        bgimage4.setOnClickListener(this);
        bgimage5.setOnClickListener(this);
        bgimage6.setOnClickListener(this);
        bgimage7.setOnClickListener(this);
        add.setOnClickListener(this);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.bgimage1:
                intent=new Intent(BgImage.this,MainActivity.class);
                intent.putExtra("bgimage",R.drawable.bgimage1+"");
                startActivityForResult(intent,1002);
                break;
            case R.id.bgimage2:
                intent=new Intent(BgImage.this,MainActivity.class);
                intent.putExtra("bgimage",R.drawable.bgimage2+"");
                startActivityForResult(intent,1002);
                break;
            case R.id.bgimage3:
                intent=new Intent(BgImage.this,MainActivity.class);
                intent.putExtra("bgimage",R.drawable.bgimage3+"");
                startActivityForResult(intent,1002);
                break;
            case R.id.bgimage4:
                intent=new Intent(BgImage.this,MainActivity.class);
                intent.putExtra("bgimage",R.drawable.bgimage4+"");
                startActivityForResult(intent,1002);
                break;
            case R.id.bgimage5:
                intent=new Intent(BgImage.this,MainActivity.class);
                intent.putExtra("bgimage",R.drawable.bgimage5+"");
                startActivityForResult(intent,1002);
                break;
            case R.id.bgimage6:
                intent=new Intent(BgImage.this,MainActivity.class);
                intent.putExtra("bgimage",R.drawable.bgimage6+"");
                startActivityForResult(intent,1002);
                break;
            case R.id.bgimage7:
                intent=new Intent(this,MainActivity.class);
                if (bitmap!=null){
//                    Bundle b = new Bundle();
//                    b.putParcelable("bgimageBitmap", bitmap);
                    intent.putExtra("bgimageBitmap",bitmap);
                    intent.putExtra("bgimage","");
//                    System.out.println(bitmap);

                    startActivityForResult(intent,1003);
                }else {
                    intent.putExtra("bgimage",R.drawable.bgimage7+"");
                    startActivityForResult(intent,1002);
                }

                break;
            case R.id.add:
                intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE_RESULT_CODE);
                break;
            case R.id.btn:
                intent=new Intent(BgImage.this,MainActivity.class);
                intent.putExtra("bgimage","");
                startActivityForResult(intent,1002);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // 表示 调用照相机拍照
            case PICK:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    imgString = bitmapToBase64(bitmap);
//                    uploadImg();
                }
                break;
            // 选择图片库的图片
            case IMAGE_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    Bitmap bitmap2 = PhotoUtils.getBitmapFromUri(uri, this);
//                    imgString = bitmapToBase64(bitmap2);
                    bitmap=bitmap2;

//                    byte[]bitmapArray;
//                    bitmapArray=Base64.decode(imgString, Base64.DEFAULT);
//                    bitmap= BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
                    bgimage7.setImageBitmap(bitmap2);//显示图片
                }
                break;
        }
    }

//    //上传图片文件的操作
//    public void uploadImg() {
//
//    }

    //如上参需要64位编码可调用此方法，不需要可以忽略
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
