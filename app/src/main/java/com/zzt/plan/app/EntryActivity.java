package com.zzt.plan.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zzt.plan.app.activity.LoginActivity;
import com.zzt.plan.app.activity.MainActivity;

/**
 * Created by zzt on 15-6-16.
 */
public class EntryActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

        String account = Config.getCachedAccount(this);
        String token = Config.getCachedToken(this);

        if (account == null || token == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

    }
}
