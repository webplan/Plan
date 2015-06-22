package com.zzt.plan.app.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzt.plan.app.Config;
import com.zzt.plan.app.R;
import com.zzt.plan.app.net.AddFriend;

/**
 * Created by zzt on 15-6-17.
 */
public class UserInfoActivity extends ActionBarActivity {
    private ImageView ivAvatar;
    private TextView tvNickname;
    private TextView tvAccount;
    private Button btnAddFriend;
    private String account;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        ImageLoader loader = ImageLoader.getInstance();

        account = Config.getCachedAccount(this);
        token = Config.getCachedToken(this);

        ivAvatar = (ImageView) findViewById(R.id.iv_avatar);
        tvNickname = (TextView) findViewById(R.id.tv_nickname);
        tvAccount = (TextView) findViewById(R.id.tv_account);
        btnAddFriend = (Button) findViewById(R.id.btn_add_friend);

        final String friendAccount = getIntent().getStringExtra(Config.KEY_ACCOUNT);
        String nickname = getIntent().getStringExtra(Config.KEY_NICKNAME);
        String avatarURL = getIntent().getStringExtra(Config.KEY_AVATAG);

        tvNickname.setText(nickname);
        tvAccount.setText(friendAccount);
        loader.displayImage(Config.SERVER_URL + avatarURL, ivAvatar);

        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog pd = ProgressDialog.show(UserInfoActivity.this, getString(R.string.now_loading), getString(R.string.please_waite));

                new AddFriend(account, token, friendAccount, new AddFriend.SuccessCallback() {
                    @Override
                    public void onSuccess() {
                        pd.dismiss();
                    }
                }, new AddFriend.FailCallback() {
                    @Override
                    public void onFail() {
                        onFail(Config.RESULT_STATUS_FAIL);
                    }

                    @Override
                    public void onFail(int failCode) {
                        pd.dismiss();
                        switch (failCode) {
                            case Config.RESULT_STATUS_FAIL:
                                Toast.makeText(UserInfoActivity.this, R.string.loading_failed, Toast.LENGTH_LONG).show();
                                break;
                            case Config.RESULT_STATUS_INVALID_TOKEN:
                                Toast.makeText(UserInfoActivity.this, R.string.invalid_token_please_login_again, Toast.LENGTH_LONG).show();
                                startActivity(new Intent(UserInfoActivity.this, LoginActivity.class));
                                break;
                        }
                    }
                });
            }
        });
    }
}
