package com.lbt.batro.tinhtrangthietbi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.kongzue.dialog.v2.WaitDialog;
import com.lbt.batro.tinhtrangthietbi.Presenter.idangnhap;
import com.lbt.batro.tinhtrangthietbi.Presenter.ldangnhap;

public class LoginActivity extends AppCompatActivity implements idangnhap {

    Button btnLogin;
    TextInputLayout tiluser,tilpwd;
    ldangnhap mDangNhap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        actionDangNhap();

    }

    private void initView() {
        btnLogin = findViewById(R.id.btnDangNhap);
        tiluser = findViewById(R.id.tilTenDangNhap);
        tilpwd = findViewById(R.id.tilMatKhau);
        mDangNhap = new ldangnhap(this,this);
    }



    private void actionDangNhap(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WaitDialog.show(LoginActivity.this, getText(R.string.dangdangnhap).toString());

                String userName = tiluser.getEditText().getText().toString();
                String pwd = tilpwd.getEditText().getText().toString();
                tilpwd.setErrorEnabled(false);
                tiluser.setErrorEnabled(false);

                if(mDangNhap.kiemtratentaikhoan(userName,pwd)){
                    mDangNhap.login(userName,pwd);
                }
            }
        });
    }


    @Override
    public void dangnhapthanhcong() {
        mDangNhap.laythongtintaikhoan();

    }


    @Override
    public void dangnhapthatbai() {
        WaitDialog.dismiss();
        Toast.makeText(this, getString(R.string.dangnhapthatbai), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void saitendangnhap() {
        WaitDialog.dismiss();
        tiluser.setErrorEnabled(true);
        tiluser.setError(getString(R.string.tendangnhapkhonghople));
    }

    @Override
    public void saimatkhau() {
        WaitDialog.dismiss();
        tilpwd.setErrorEnabled(true);
        tilpwd.setError(getString(R.string.matkhaukhonghople));
    }

    @Override
    public void autoDangNhap(FirebaseUser user, boolean isSuccess) {

    }

    @Override
    public void luuthongtinnguoidungthanhcong(boolean iscoluu) {
        WaitDialog.dismiss();
        startActivity(new Intent(LoginActivity.this,MainActivity.class));
        finish();
    }


    @Override
    public void dangxuat() {

    }

}
