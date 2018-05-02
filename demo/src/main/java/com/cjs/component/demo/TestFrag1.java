package com.cjs.component.demo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.cjs.component.demo.permissiongennoannotation.DetailActivity;
import com.cjs.component.demo.permissiongennoannotation.R;
import com.cjs.component.demo.permissiongennoannotation.Util.Constant;
import com.cjs.component.demo.permissiongennoannotation.Util.DialogTools;
import com.cjs.component.permissiongennoannotation.PermissionGenNoAnnotation;

/**
 * 描述:Fragment测试类
 * <p>
 * 作者:陈俊森
 * 创建时间:2018年05月02日 20:32
 * 邮箱:chenjunsen@outlook.com
 *
 * @version 1.0
 */
public class TestFrag1 extends Fragment implements PermissionGenNoAnnotation.RequestPermissionCallBack{
    private View mView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.frag_test1,container,false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btn=view.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"ok",Toast.LENGTH_SHORT).show();
                PermissionGenNoAnnotation.with(TestFrag1.this)
                        .permissions(new String[]{Manifest.permission.RECORD_AUDIO})
                        .requestCode(Constant.REQUEST_PERMISSION_RECORD_AUDIO)
                        .requestPermissionCallBack(TestFrag1.this)
                        .request();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionGenNoAnnotation.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }

    @Override
    public void onRequestSuccessful(int requestCode) {
        if(requestCode==Constant.REQUEST_PERMISSION_RECORD_AUDIO){
            DetailActivity.startToDetailActivity(getActivity(),"Fragment跳转录音授权");
        }
    }

    @Override
    public void onRequestFailed(int requestCode) {
        if(requestCode==Constant.REQUEST_PERMISSION_RECORD_AUDIO){
            showPermissionSettingDialog("Fragment跳转录音授权");
        }
    }

    public void showPermissionSettingDialog(String msg){
        DialogTools.create2(getActivity(), "权限管理", "不授权", "去设置", msg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startAppSettings();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }

    // 启动应用的设置
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
        startActivity(intent);
    }
}
