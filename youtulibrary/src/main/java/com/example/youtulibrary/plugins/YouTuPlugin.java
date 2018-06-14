package com.example.youtulibrary.plugins;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.example.youtulibrary.YouTuConfig;
import com.example.youtulibrary.facein.CardVideoActivity;
import com.jess.arms.utils.DialogUtils;
import com.jess.arms.utils.PermissionUtil;
import com.jess.arms.widget.dialog.SweetAlertDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import timber.log.Timber;

/**
 * ================================================================
 * 创建时间：2018/6/6 15:41
 * 创 建 人：Mr.常
 * 文件描述：腾讯优图插件入口类
 * 码    农：你不是我记忆中Bug、但致命的程度没两样！
 * ================================================================
 */
public class YouTuPlugin extends CordovaPlugin{
    public SweetAlertDialog mDialog;//对话框
//    private static YouTuPlugin mPlugin; // TODO: 2017/12/18 测试、正式版本需要注释/反注释的代码  ----------  1
    private  Activity mActivity;//super.cordova.getActivity();
    private CallbackContext callbackContext;

//    // TODO: 2017/12/18 测试、正式版本需要注释/反注释的代码  ----------  2
//    public static YouTuPlugin getInstance(Activity mActivity1){
//                mActivity = mActivity1;
//        if (mPlugin == null) mPlugin = new YouTuPlugin();
//        return mPlugin;
//    }

    @Override
    public boolean execute(String action, String rawArgs, CallbackContext callbackContext) throws JSONException {
        if (TextUtils.isEmpty(rawArgs)) {
            Log.e("YouTu","传入参数不能为空");
            callbackContext.error("传入参数不能为空");
            return false;
        }
        this.callbackContext=callbackContext;
        mActivity = super.cordova.getActivity();// TODO: 2017/12/18 测试、正式版本需要注释/反注释的代码  ----------  3
        //获取相关权限
        Observable.create(e -> e.onNext("")).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(mO -> requestPermissions(rawArgs));//请求权限
        return true;
    }

    /**
     * 业务操作在这里
     *
     * @param rawArgs
     */
    private void doSomething(String rawArgs) {
        Intent intent = new Intent(mActivity, CardVideoActivity.class);
        intent.putExtra("rawArgs", rawArgs);
        startActivityForResult(intent, YouTuConfig.REQUEST_CODE_HOME_TAKECAMERA);
    }


    /**
     * 返回页面所获得值
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (intent == null) return;
        if (requestCode == YouTuConfig.REQUEST_CODE_HOME_TAKECAMERA
                && resultCode == YouTuConfig.REQUEST_CODE_HOME_TAKECAMERA) {
            String responseBody=intent.getStringExtra("responseBody");
            if (callbackContext!=null)
            callbackContext.success(responseBody);
        }
        Timber.d("responseBody=:");
    }

    /**
     * 内部使用方法
     *
     * @param mIntent
     * @param requestCode
     */
    private void startActivityForResult(Intent mIntent, int requestCode) {
        cordova.startActivityForResult(this,mIntent, requestCode);
    }




    /*未使用的构造*/
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        return false;
    }

    /*未使用的构造*/
    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {
        return false;
    }



    /**
     * 权限申请
     *
     */
    private void requestPermissions(String rawArgs) {
        PermissionUtil.requestPermission(new PermissionUtil.RequestPermission() {
                                             @Override
                                             public void onRequestPermissionSuccess() {
                                                 doSomething(rawArgs);
                                             }

                                             @Override
                                             public void onRequestPermissionFailure() {
                                                 showDialog("提示", "您有未授予的权限，可能导致部分功能闪退，请点击\"设置\"授权相关权限", SweetAlertDialog.ERROR_TYPE);
                                             }
                                         }, new RxPermissions(mActivity), RxErrorHandler
                        .builder()
                        .with(mActivity)
                        .responseErrorListener((context, t) -> Timber.e("异常"))
                        .build(),
                /**
                 * 注释后面有 1 的为显示确认访问、无 1 的为隐藏默认访问
                 */
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE);
    }



    public void showDialog(String title, String content, int dialogType) {
        mDialog= DialogUtils.getInstance().getDialog(mActivity,title,content,dialogType,false, new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }
}
