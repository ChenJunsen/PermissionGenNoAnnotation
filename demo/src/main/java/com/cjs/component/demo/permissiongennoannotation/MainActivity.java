package com.cjs.component.demo.permissiongennoannotation;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import com.cjs.component.demo.permissiongennoannotation.Util.Constant;
import com.cjs.component.demo.permissiongennoannotation.Util.DialogTools;
import com.cjs.component.permissiongennoannotation.PermissionGenNoAnnotation;

/**
 * 描述:安卓运行时权限解决方案demo,支持链式及静态方式调用<br>
 * 关于6.0权限的描述，可以参考这篇文章<a href="https://blog.csdn.net/u011150924/article/details/53116105">android6.0后权限概谈<a/>
 * <br>使用之前请在AndroidManifest.xml中配置好需要的权限
 * <p>
 * <br>作者: 陈俊森
 * <br>创建时间: 2018/5/2 0002 11:13
 * <br>邮箱: chenjunsen@outlook.com
 *
 * @version 1.0
 */
public class MainActivity extends Activity implements View.OnClickListener, PermissionGenNoAnnotation.RequestPermissionCallBack {
    private Button btn_audio, btn_storage_read, btn_storage_write, btn_camera;
    private Button btn_go2Frag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_audio = findViewById(R.id.btn_audio);
        btn_camera = findViewById(R.id.btn_camera);
        btn_storage_read = findViewById(R.id.btn_storage_read);
        btn_storage_write = findViewById(R.id.btn_storage_write);
        btn_go2Frag=findViewById(R.id.btn_go2Frag);

        btn_audio.setOnClickListener(this);
        btn_storage_read.setOnClickListener(this);
        btn_storage_write.setOnClickListener(this);
        btn_camera.setOnClickListener(this);
        btn_go2Frag.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btn_audio) {
            //链式写法
            PermissionGenNoAnnotation
                    .with(this)
                    .requestCode(Constant.REQUEST_PERMISSION_RECORD_AUDIO)
                    .permissions(new String[]{Manifest.permission.RECORD_AUDIO})
                    .requestPermissionCallBack(this)
                    .request();
            /*//静态函数写法
            PermissionGenNoAnnotation.needPermission(
                    this,
                    Constant.REQUEST_PERMISSION_RECORD_AUDIO,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    this);*/
        } else if (v == btn_camera) {
            //链式写法
            PermissionGenNoAnnotation
                    .with(this)
                    .requestCode(Constant.REQUEST_PERMISSION_CAMERA)
                    .permissions(new String[]{Manifest.permission.CAMERA})
                    .requestPermissionCallBack(this)
                    .request();
        } else if (v == btn_storage_read) {
            //这里需要注意，权限有permission_group的说法，同一组的权限只要有一个被授权，另外一个就自动被授权
            PermissionGenNoAnnotation
                    .with(this)
                    .requestCode(Constant.REQUEST_PERMISSION_READ_EXTERNAL_STORAGE)
                    .permissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE})
                    .requestPermissionCallBack(this)
                    .request();
        } else if (v == btn_storage_write) {
            PermissionGenNoAnnotation
                    .with(this)
                    .requestCode(Constant.REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE)
                    .permissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE})
                    .requestPermissionCallBack(this)
                    .request();
        }else if(v==btn_go2Frag){
            Intent i=new Intent(this,FragmentDemoActivity.class);
            startActivity(i);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionGenNoAnnotation.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onRequestSuccessful(int requestCode) {
        switch (requestCode) {
            case Constant.REQUEST_PERMISSION_RECORD_AUDIO:
                DetailActivity.startToDetailActivity(this,"录音");
                break;
            case Constant.REQUEST_PERMISSION_READ_EXTERNAL_STORAGE:
                DetailActivity.startToDetailActivity(this,"存储读取");
                break;
            case Constant.REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE:
                DetailActivity.startToDetailActivity(this,"存储写入");
                break;
            case Constant.REQUEST_PERMISSION_CAMERA:
                DetailActivity.startToDetailActivity(this,"相机");
                break;
        }
    }

    @Override
    public void onRequestFailed(int requestCode) {
        switch (requestCode) {
            case Constant.REQUEST_PERMISSION_RECORD_AUDIO:
                showPermissionSettingDialog("录音授权失败!");
                break;
            case Constant.REQUEST_PERMISSION_READ_EXTERNAL_STORAGE:
                showPermissionSettingDialog("存储读取权限获取失败!");
                break;
            case Constant.REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE:
                showPermissionSettingDialog("存储写入权限获取失败!");
                break;
            case Constant.REQUEST_PERMISSION_CAMERA:
                showPermissionSettingDialog("存储相机权限获取失败!");
                break;
        }
    }

    public void showPermissionSettingDialog(String msg){
        DialogTools.create2(this, "权限管理", "不授权", "去设置", msg, new DialogInterface.OnClickListener() {
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
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }
}
