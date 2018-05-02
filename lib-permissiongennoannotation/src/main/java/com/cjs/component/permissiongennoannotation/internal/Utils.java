package com.cjs.component.permissiongennoannotation.internal;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

final public class Utils {
  private Utils(){}

  public static boolean isOverMarshmallow() {
    return Build.VERSION.SDK_INT >= 23;
  }

  public static List<String> findDeniedPermissions(Activity activity, String... permission){
    List<String> denyPermissions = new ArrayList<>();
    for(String value : permission){
      if(ActivityCompat.checkSelfPermission(activity,value) != PackageManager.PERMISSION_GRANTED){
        denyPermissions.add(value);
      }
    }
    return denyPermissions;
  }


  public static Activity getActivity(Object object){
    if(object instanceof Fragment){
      return ((Fragment)object).getActivity();
    } else if(object instanceof Activity){
      return (Activity) object;
    }
    return null;
  }
}
