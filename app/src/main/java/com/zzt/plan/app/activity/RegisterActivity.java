package com.zzt.plan.app.activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.zzt.plan.app.R;
import com.zzt.plan.app.net.Register;
import com.zzt.plan.app.tools.MD5Utils;

import java.io.FileNotFoundException;

/**
 * Created by zzt on 15-6-16.
 */
public class RegisterActivity extends ActionBarActivity {

    private EditText etAccount, etNickname, etPassword, etConfirmPassword, etPhoneNum;
    private ImageButton btnAvatar;
    private Bitmap bitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etAccount = (EditText) findViewById(R.id.et_account);
        etNickname = (EditText) findViewById(R.id.et_nickname);
        etPassword = (EditText) findViewById(R.id.et_password);
        etConfirmPassword = (EditText) findViewById(R.id.et_confirm_password);
        etPhoneNum = (EditText) findViewById(R.id.et_phone_num);
        btnAvatar = (ImageButton) findViewById(R.id.btn_avatar);

        TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        etPhoneNum.setText(tm.getLine1Number());

        btnAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });

        findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = etAccount.getText().toString();
                String nickname = etNickname.getText().toString();
                String password = etPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();
                String phoneNum = etPhoneNum.getText().toString();

                if (TextUtils.isEmpty(etAccount.getText()) || TextUtils.isEmpty(etNickname.getText())
                        || TextUtils.isEmpty(etPassword.getText()) || TextUtils.isEmpty(etConfirmPassword.getText())) {
                    Toast.makeText(RegisterActivity.this, R.string.please_complete_above_info, Toast.LENGTH_LONG).show();
                    return;
                }
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, R.string.inconsistent_passwords, Toast.LENGTH_LONG).show();
                    return;
                }

                btnAvatar.setDrawingCacheEnabled(true);
                bitmap = Bitmap.createBitmap(btnAvatar.getDrawingCache());
                btnAvatar.setDrawingCacheEnabled(false);

                final ProgressDialog pd = ProgressDialog.show(RegisterActivity.this, getString(R.string.connecting), getString(R.string.login_now));

                new Register(account, MD5Utils.str2MD5(password), nickname, phoneNum, bitmap, new Register.SuccessCallback() {
                    @Override
                    public void onSuccess() {
                        pd.dismiss();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

                        finish();
                    }
                }, new Register.FailCallback() {
                    @Override
                    public void onFail() {
                        pd.dismiss();
                        Toast.makeText(RegisterActivity.this, R.string.fail_to_register, Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            ContentResolver cr = this.getContentResolver();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                btnAvatar.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
