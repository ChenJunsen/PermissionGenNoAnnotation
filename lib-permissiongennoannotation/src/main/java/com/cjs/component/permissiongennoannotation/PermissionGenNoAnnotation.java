package com.cjs.component.permissiongennoannotation;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;

import com.cjs.component.permissiongennoannotation.internal.Utils;

import java.util.ArrayList;
import java.util.List;


/**
 * 描述:安卓6运行时获取权限的工具类，支持链式及静态方式调用，修改自<a href="https://github.com/lovedise/PermissionGen">PermissionGen</>的一个第三方库。
 * 原文件是以注解反射的方式进行界结果的解析。但是考虑到项目的扩展性，以及代码的重用性，本来想在BaseActivity里面写一个公共的
 * 获取结果的处理方法，结果发现父类注解不能被子类继承，导致无法解析获取权限的结果，因此才重写了这个库的,删掉了原库文件的所有注解部分，
 * 针对获取结果添加了一个结果回调函数{@link RequestPermissionCallBack}
 * <br>作者:陈俊森
 * <br>创建时间:2017年05月31日 15:58
 * <br>邮箱:chenjunsen@outlook.com
 */
public class PermissionGenNoAnnotation {

    private String[] mPermissions;
    private int mRequestCode;
    private Object object;
    private RequestPermissionCallBack mRequestPermissionCallBack;

    private PermissionGenNoAnnotation(Object object) {
        this.object = object;
    }

    /**
     * 设置获取权限的载体
     * @param activity
     * @return
     */
    public static PermissionGenNoAnnotation with(Activity activity) {
        return new PermissionGenNoAnnotation(activity);
    }

    /**
     * 设置获取权限的载体
     * @param fragment
     * @return
     */
    public static PermissionGenNoAnnotation with(Fragment fragment) {
        return new PermissionGenNoAnnotation(fragment);
    }

    /**
     * 设置需要获取的权限
     * @param permissions 参考{@link android.Manifest.permission}
     * @return
     */
    public PermissionGenNoAnnotation permissions(String... permissions) {
        this.mPermissions = permissions;
        return this;
    }

    /**
     * 设置获取权限操作的请求码，后续的回调结果会根据此请求码进行结果的回调解析
     * @param requestCode
     * @return
     */
    public PermissionGenNoAnnotation requestCode(int requestCode) {
        this.mRequestCode = requestCode;
        return this;
    }

    /**
     * 设置请求权限的回调接口
     * @param requestPermissionCallBack
     * @return
     */
    public PermissionGenNoAnnotation requestPermissionCallBack(RequestPermissionCallBack requestPermissionCallBack) {
        mRequestPermissionCallBack = requestPermissionCallBack;
        return this;
    }

    /**
     * 请求权限
     */
    public void request() {
        if(Utils.isOverMarshmallow()){
            requestPermissions(object, mRequestCode, mPermissions, mRequestPermissionCallBack);
        }
    }

    public static void needPermission(Activity activity, int requestCode, String[] permissions, RequestPermissionCallBack requestPermissionCallBack) {
        requestPermissions(activity, requestCode, permissions, requestPermissionCallBack);
    }

    public static void needPermission(Fragment fragment, int requestCode, String[] permissions, RequestPermissionCallBack requestPermissionCallBack) {
        requestPermissions(fragment, requestCode, permissions, requestPermissionCallBack);
    }

    public static void needPermission(Activity activity, int requestCode, String permission, RequestPermissionCallBack requestPermissionCallBack) {
        needPermission(activity, requestCode, new String[]{permission}, requestPermissionCallBack);
    }

    public static void needPermission(Fragment fragment, int requestCode, String permission, RequestPermissionCallBack requestPermissionCallBack) {
        needPermission(fragment, requestCode, new String[]{permission}, requestPermissionCallBack);
    }

    /**
     *
     * @param object 当前的载体，可以是Fragment或者Activity
     * @param requestCode 权限的请求码,回调函数会返回该请求码，然后根据请求码来进行操作
     * @param permissions 权限的请求集合，参考{@link android.Manifest.permission}
     * @param requestPermissionCallBack 权限请求回调
     */
    @TargetApi(23)
    private static void requestPermissions(Object object, int requestCode, String[] permissions, RequestPermissionCallBack requestPermissionCallBack) {
        if (!Utils.isOverMarshmallow()) {
            doExecuteSuccess(requestCode, requestPermissionCallBack);
            return;
        }
        List<String> deniedPermissions = Utils.findDeniedPermissions(Utils.getActivity(object), permissions);

        if (deniedPermissions.size() > 0) {
            if (object instanceof Activity) {
                ((Activity) object).requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
            } else if (object instanceof Fragment) {
                ((Fragment) object).requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
            } else {
                throw new IllegalArgumentException(object.getClass().getName() + " is not supported,only supports Activity or Fragment");
            }
        } else {
            doExecuteSuccess(requestCode, requestPermissionCallBack);
        }
    }


    private static void doExecuteSuccess(int requestCode, RequestPermissionCallBack requestPermissionCallBack) {
        if (requestPermissionCallBack != null) {
            requestPermissionCallBack.onRequestSuccessful(requestCode);
        }
    }

    private static void doExecuteFail(int requestCode, RequestPermissionCallBack requestPermissionCallBack) {
        if (requestPermissionCallBack != null) {
            requestPermissionCallBack.onRequestFailed(requestCode);
        }
    }


    public static void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                  int[] grantResults, RequestPermissionCallBack requestPermissionCallBack) {
        requestResult(requestCode, permissions, grantResults, requestPermissionCallBack);
    }


    public static void requestResult(int requestCode, String[] permissions,
                                     int[] grantResults, RequestPermissionCallBack permissionCallBack) {
        List<String> deniedPermissions = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permissions[i]);
            }
        }

        if (deniedPermissions.size() > 0) {
            doExecuteFail(requestCode, permissionCallBack);
        } else {
            doExecuteSuccess(requestCode, permissionCallBack);
        }
    }

    /**
     * 安卓6运行时权限请求结果回调接口
     */
    public interface RequestPermissionCallBack {
        /**
         * 权限请求成功
         * @param requestCode 当前请求操作的请求码
         */
        void onRequestSuccessful(int requestCode);

        /**
         * 权限请求失败
         * @param requestCode 当前请求操作的请求码
         */
        void onRequestFailed(int requestCode);
    }
}
