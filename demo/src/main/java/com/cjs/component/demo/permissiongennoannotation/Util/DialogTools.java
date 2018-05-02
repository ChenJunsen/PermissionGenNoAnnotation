package com.cjs.component.demo.permissiongennoannotation.Util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * 描述:
 * <p>
 * 作者:陈俊森
 * 创建时间:2018年05月02日 16:00
 * 邮箱:chenjunsen@outlook.com
 *
 * @version 1.0
 */
public class DialogTools {
    static final String DEFAULT_TITLE="提示";
    static final String DEFAULT_SUBMIT="确定";
    static final String DEFAULT_CANCEL="取消";

    public static AlertDialog create2(Context context, String title, String cancelText, String submitText, String msg, DialogInterface.OnClickListener onSubmitClickListener, DialogInterface.OnClickListener onCancelClickListener){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setNegativeButton(cancelText, onCancelClickListener);
        builder.setPositiveButton(submitText, onSubmitClickListener);
        return builder.create();
    }

    public static AlertDialog create2(Context context, String msg, DialogInterface.OnClickListener onSubmitClickListener, DialogInterface.OnClickListener onCancelClickListener){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle(DEFAULT_TITLE);
        builder.setMessage(msg);
        builder.setNegativeButton(DEFAULT_CANCEL, onCancelClickListener);
        builder.setPositiveButton(DEFAULT_SUBMIT, onSubmitClickListener);
        return builder.create();
    }

    public static AlertDialog create1(Context context, String title, String submitText, String msg, DialogInterface.OnClickListener onSubmitClickListener){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(submitText, onSubmitClickListener);
        return builder.create();
    }

    public static AlertDialog create1(Context context, String msg, DialogInterface.OnClickListener onSubmitClickListener){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle(DEFAULT_TITLE);
        builder.setMessage(msg);
        builder.setPositiveButton(DEFAULT_SUBMIT, onSubmitClickListener);
        return builder.create();
    }

    public static AlertDialog create1(Context context, String msg){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle(DEFAULT_TITLE);
        builder.setMessage(msg);
        builder.setPositiveButton(DEFAULT_SUBMIT, null);
        return builder.create();
    }
}
