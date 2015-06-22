package com.zzt.plan.app.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import com.zzt.plan.app.Config;
import com.zzt.plan.app.R;
import com.zzt.plan.app.adapter.UsersAdapter;
import com.zzt.plan.app.entity.UserEntity;
import com.zzt.plan.app.net.SearchUser;

import java.util.List;

/**
 * Created by zzt on 15-6-17.
 */
public class SearchUserActivity extends ActionBarActivity {
    private ListView lvSearchResult;
    private EditText etNickname;
    private ImageButton btnSearch;
    private UsersAdapter adapter;
    private String account;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        account = Config.getCachedAccount(this);
        token = Config.getCachedToken(this);

        lvSearchResult = (ListView) findViewById(R.id.lv_search_result);
        etNickname = (EditText) findViewById(R.id.et_nickname);
        btnSearch = (ImageButton) findViewById(R.id.btn_search);
        adapter = new UsersAdapter(this);
        lvSearchResult.setAdapter(adapter);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etNickname.getText())) {
                    Toast.makeText(SearchUserActivity.this, R.string.please_enter_nickname, Toast.LENGTH_LONG).show();
                    return;
                }

                final ProgressDialog pd = ProgressDialog.show(SearchUserActivity.this, getString(R.string.now_loading), getString(R.string.please_waite));

                new SearchUser(account, token, etNickname.getText().toString(), new SearchUser.SuccessCallback() {
                    @Override
                    public void onSuccess(List<UserEntity> userList) {
                        pd.dismiss();
                        adapter.clear();
                        adapter.addAll(userList);
                    }
                }, new SearchUser.FailCallback() {
                    @Override
                    public void onFail() {
                        onFail(Config.RESULT_STATUS_FAIL);
                    }

                    @Override
                    public void onFail(int failCode) {
                        pd.dismiss();
                        switch (failCode) {
                            case Config.RESULT_STATUS_FAIL:
                                Toast.makeText(SearchUserActivity.this, R.string.loading_failed, Toast.LENGTH_LONG).show();
                                break;
                            case Config.RESULT_STATUS_INVALID_TOKEN:
                                Toast.makeText(SearchUserActivity.this, R.string.invalid_token_please_login_again, Toast.LENGTH_LONG).show();
                                startActivity(new Intent(SearchUserActivity.this, LoginActivity.class));
                                break;
                        }
                    }
                });
            }
        });
    }


}
