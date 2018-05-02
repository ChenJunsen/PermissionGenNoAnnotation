package com.cjs.component.demo.permissiongennoannotation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;

public class DetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ((TextView)findViewById(R.id.text)).setText(getIntent().getStringExtra("msg"));
    }

    public static void startToDetailActivity(Context context,String msg){
        Intent i=new Intent(context,DetailActivity.class);
        i.putExtra("msg",msg);
        ActivityCompat.startActivity(context,i,null);
    }
}
