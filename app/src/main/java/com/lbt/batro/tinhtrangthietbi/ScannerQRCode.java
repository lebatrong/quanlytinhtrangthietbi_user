package com.lbt.batro.tinhtrangthietbi;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.zxing.Result;
import com.kongzue.dialog.v2.DialogSettings;
import com.kongzue.dialog.v2.SelectDialog;
import com.kongzue.dialog.v2.WaitDialog;
import com.lbt.batro.tinhtrangthietbi.Presenter.ibaocaotinhtrang;
import com.lbt.batro.tinhtrangthietbi.Presenter.lbaocaotinhtrang;
import com.lbt.batro.tinhtrangthietbi.models.clsFireBase.objlichsu_maytinhs;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerQRCode extends AppCompatActivity implements ZXingScannerView.ResultHandler, ibaocaotinhtrang {

    private ZXingScannerView mScannerView;
    private lbaocaotinhtrang mTinhTrang;
    private String mQRcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);// Set the scanner view as the content view

        mTinhTrang = new lbaocaotinhtrang(this);
        mQRcode = "";


        //SetTing Dialog;
        DialogSettings.use_blur = true;
        DialogSettings.blur_alpha = 200;
        DialogSettings.type = DialogSettings.TYPE_IOS;

        ActivityCompat.requestPermissions(ScannerQRCode.this,new String[] {Manifest.permission.CAMERA},1);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DialogSettings.unloadAllDialog();
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }
    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }
    @Override
    public void handleResult(Result rawResult) {
        mQRcode = rawResult.getText();
        mTinhTrang.kiemtratinhtrangmay(rawResult.getText());
        WaitDialog.show(ScannerQRCode.this, getText(R.string.dangtai).toString());

        //PHÁT ÂM BÁO KHI QUÉT THÀNH CÔNG
        MediaPlayer mediaPlayer = MediaPlayer.create(this,R.raw.beep);
        mediaPlayer.start();

    }


    @Override
    public void laythongtinmay(boolean isSuccess) {
        WaitDialog.dismiss();
        if(isSuccess){
            Intent intent = new Intent(this,baocaotinhtrangActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("qrcode",mQRcode);
            intent.putExtra("data",bundle);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this, getText(R.string.loi), Toast.LENGTH_SHORT).show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScannerView.resumeCameraPreview(ScannerQRCode.this);
                }
            },1500);
        }

    }

    @Override
    public void capnhatthanhcong(boolean isthanhcong) {

    }

    @Override
    public void giatrikhonghople() {

    }

    @Override
    public void baocaothietbikhacthanhcong(boolean isCapNhat) {

    }

    @Override
    public void maycontot(boolean isTot, objlichsu_maytinhs mls, String mamay) {
        //Nếu máy còn tốt thì mới báo cáo
        if(isTot){
            mTinhTrang.laythongtinthietbi(mamay);
        }else{
            WaitDialog.dismiss();
            showaler(mls,mamay);
        }
    }

    @Override
    public void maqrkhonghopke() {
        Toast.makeText(this, getText(R.string.maqrkhonghople), Toast.LENGTH_SHORT).show();
    }

    public void showaler(final objlichsu_maytinhs mls, final String mamay){

        SelectDialog.show(ScannerQRCode.this,
                getText(R.string.thongbao).toString(),
                getText(R.string.maydangsuachuamuonxemtinhtrang).toString(),
                getText(R.string.dongy).toString(),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showtinhtrangthietbi(mls,mamay);
                        dialog.dismiss();
                    }
                }, getText(R.string.huy).toString(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        dialog.dismiss();
                    }
                });

    }

    private void showtinhtrangthietbi(objlichsu_maytinhs mls, String mamay){
        Bundle bundle = new Bundle();
        bundle.putSerializable("mls",  mls);
        bundle.putString("mamay",mamay);
        Intent intent = new Intent(this,tinhtrangthietbi.class);
        intent.putExtra("data",bundle);
        startActivity(intent);

    }
}
