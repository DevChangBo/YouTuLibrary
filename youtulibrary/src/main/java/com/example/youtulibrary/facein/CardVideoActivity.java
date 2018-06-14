package com.example.youtulibrary.facein;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.youtulibrary.R;
import com.example.youtulibrary.YouTuConfig;
import com.example.youtulibrary.common.BitmapUtil;
import com.example.youtulibrary.common.FileUtil;
import com.example.youtulibrary.common.YTServerAPI;
import com.jess.arms.utils.DialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import timber.log.Timber;

/**
 * ================================================================
 * 创建时间：2018-6-8 16:52:07
 * 创 建 人：Mr.常
 * 文件描述：相机页面
 * 码    农：你不是我记忆中Bug、但致命的程度没两样！
 * ================================================================
 */
public class CardVideoActivity extends AppCompatActivity implements DialogInterface.OnCancelListener{

    private static final String LOG_TAG = YTServerAPI.class.getName();
    private CameraPreview mCameraPreview;
    private Button mPicButton;
    private TextView mWarningText;
    private TextView mErrorText;

    private Bitmap mFaceBitmap;
    private YTServerAPI mServer;
    public Dialog mLoading;//进度条
    private Handler mHandler;
    private int rawArgs=-1;

    @Override
    protected void onStart() {
        super.onStart();
        //全屏显示
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE  //保持View Layout不变，隐藏状态栏或者导航栏后，View不会拉伸。
                            |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION  //让View全屏显示，Layout会被拉伸到StatusBar和NavigationBar下面。
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN  //让View全屏显示，Layout会被拉伸到StatusBar下面，不包含NavigationBar。
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION    //隐藏虚拟按键(导航栏)。有些手机会用虚拟按键来代替物理按键
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_video);
        setView();
        CameraPreviewInit();
        mPicButton.setEnabled(true);
        initUI();
        mServer = new YTServerAPI();
        mHandler = new Handler();

        //优图识别结果回调
        mServer.setRequestListener(new YTServerAPI.OnRequestListener() {
            @Override
            public void onSuccess(int statusCode, final String responseBody) {
                Log.d(LOG_TAG, responseBody);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            hideLoading();
                            mPicButton.setEnabled(true);
                            JSONObject jsonObject = new JSONObject(responseBody.toString());
                            int errorCode = jsonObject.getInt("errorcode");
                            if (errorCode == 0) {
                                Intent mIntent = new Intent();
                                mIntent.putExtra("responseBody",responseBody);
                                finishPage(mIntent);
                            }else {
                                    frontFail();

                            }

                        }catch (JSONException e){

                        }

                    }
                });
            }

            @Override
            public void onFailure(int statusCode) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mLoading.dismiss();
                        mPicButton.setEnabled(true);
                            frontFail();
                    }
                });


            }
        });
    }


    private void setView(){
        mCameraPreview = (CameraPreview) findViewById(R.id.camPreview);
        mPicButton = (Button) findViewById(R.id.startPic);
        mWarningText = (TextView) findViewById(R.id.warningText);
        mErrorText = (TextView) findViewById(R.id.errorText);
        String raw=getIntent().getStringExtra("rawArgs").toString();
        rawArgs=Integer.parseInt(raw);

        //拍照按钮点击事件
        mPicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraPreview.takePicture();
            }
        });
    }


    /**
     * 相机初始化
     */
    private void CameraPreviewInit(){
        mCameraPreview.setCameraFrontBack(CameraPreview.CameraBack);
        //拍照返回结果回调
        mCameraPreview.setOnTakePicCallBack(new CameraPreview.OnTakePicCallBack() {
            @Override
            public void onPictureTaken(byte[] data) {
                mFaceBitmap = BitmapUtil.byteToBitmap(data);
                uploadImage(FileUtil.saveBitmap(mFaceBitmap));
                //save pics to SDCard

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mCameraPreview.onResume();
    }

    @Override
    public void onPause() {
        mCameraPreview.onPause();

        super.onPause();
    }


    @Override
    public void onDestroy(){
        mCameraPreview.releaseRes();
        if (mFaceBitmap != null && !mFaceBitmap.isRecycled()){
            mFaceBitmap.recycle();
        }

        super.onDestroy();
    }



    private void initUI(){
        mWarningText.setText("将证件放入框内");
    }

    private void frontFail(){
        mErrorText.setText("OCR识别失败, 请重试");

    }


    private void uploadImage(final String imageUrl){
        //上传的图片大小不能超过2M，请开发者自行处理
//        mLoading.setText("身份证识别中");
//        mLoading.show();
        showLoading();
        mPicButton.setEnabled(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Timber.e(""+rawArgs);
                    switch (rawArgs) {
                        case 0://身份证识别
                            mServer.idCardOcr(BitmapUtil.getImage(imageUrl), 0);
                            break;
                        case 1:
                            mServer.driverlicenseocr(BitmapUtil.getImage(imageUrl), 1);//0表示行驶证识别，1表示驾驶证识别
                            break;
                        case 2:
                            mServer.driverlicenseocr(BitmapUtil.getImage(imageUrl), 0);//0表示行驶证识别，1表示驾驶证识别
                            break;
                        case 3://车牌号识别
                            mServer.plateocrCar(BitmapUtil.getImage(imageUrl));
                            break;
                    }
                    System.out.println(mFaceBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }



    private void finishPage(Intent mIntent) {
        setResult(YouTuConfig.REQUEST_CODE_HOME_TAKECAMERA, mIntent);
        finish();
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
    }

    public void showLoading() {
        mLoading = DialogUtils.getInstance().getLoadingDialog(this, this);
    }
    public void hideLoading() {
        if (mLoading != null && mLoading.isShowing()) {
            mLoading.dismiss();
        }
    }
}
