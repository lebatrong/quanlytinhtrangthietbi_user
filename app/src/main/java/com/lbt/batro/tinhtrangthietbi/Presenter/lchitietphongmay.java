package com.lbt.batro.tinhtrangthietbi.Presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.lbt.batro.tinhtrangthietbi.models.clsApp.objthietbimaytinh_app;
import com.lbt.batro.tinhtrangthietbi.models.clsApp.objthietbiphongmay_app;
import com.lbt.batro.tinhtrangthietbi.models.clsApp.objthongkemaytinh_app;
import com.lbt.batro.tinhtrangthietbi.models.clsFireBase.objchitietthietbimaytinh;
import com.lbt.batro.tinhtrangthietbi.models.clsFireBase.objlichsu_maytinhs;
import com.lbt.batro.tinhtrangthietbi.models.clsFireBase.objlichsu_thietbikhacs;
import com.lbt.batro.tinhtrangthietbi.models.clsFireBase.objmaytinhs;
import com.lbt.batro.tinhtrangthietbi.models.clsFireBase.objthietbikhacs;

import java.util.ArrayList;
import java.util.List;

public class lchitietphongmay {
    static final String TAG = "lchitietphongmay";

    FirebaseDatabase mDatabase;
    ichitietphongmay mChiTiet;


    public lchitietphongmay(ichitietphongmay mChiTiet) {
        this.mDatabase = FirebaseDatabase.getInstance();
        this.mChiTiet = mChiTiet;
    }


    public void getChiTietPhongMay(String maphong){
        DatabaseReference mRef = mDatabase.getReference().child("thietbis").child(maphong);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

//                ================PHÂN TÁCH DATA============
                //PHÂN TÁCH DANH SÁCH MÁY
                GenericTypeIndicator<List<objmaytinhs>> gen = new GenericTypeIndicator<List<objmaytinhs>>(){};
                List<objmaytinhs> mListMayTinh = dataSnapshot.child("maytinhs").getValue(gen);
                //PHÂN TÁCH THIETBIKHACS
                objthietbikhacs mthietbikhac = dataSnapshot.child("thietbikhacs").getValue(objthietbikhacs.class);

//                THỐNG KÊ
                thongkechitietphong(new objthietbiphongmay_app(dataSnapshot.getKey(),mListMayTinh,mthietbikhac));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mChiTiet.loitaidulieu();
                Log.e(TAG,databaseError.toString());
            }
        });
    }

    private void thongkechitietphong(final objthietbiphongmay_app thietbi_default){
        final String maphong = thietbi_default.getMaphong();
        final objthietbikhacs default_values = thietbi_default.getThietbikhacs();
        //KIỂM TRA LỊCH SỬ SỬA CHỮA
        DatabaseReference mRef = mDatabase.getReference().child("lichsusuachuas").child(maphong);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //============PHÂN TÁCH DATA
                if(dataSnapshot.getValue()!=null){
                    //CÓ LỊCH SỬ
                    //========================================
                    //XỬ LÝ NOTE thietbikhacs
                    phantachdata_thietbikhac(dataSnapshot,default_values);

                    //===================================================
                    //XỬ LÝ NODE maytinhs
                    phantachdata_maytinh(dataSnapshot,thietbi_default);



                }else{//TẤT CẢ THIẾT BỊ HOẠT ĐỘNG TỐT (LỊCH SỬ K CÓ)

                    objthietbikhacs chitiettbhu = new objthietbikhacs(0,0,0,0,0,0,0,0,"");
                    mChiTiet.thongkethietbikhac(default_values,chitiettbhu);

                    //MÁY TÍNH

                    long tongmay = thietbi_default.getMaytinh().size();
                    objthongkemaytinh_app thongke = new objthongkemaytinh_app();
                    thongke.setMaytinh(tongmay);
                    thongke.setManhinh(tongmay);
                    thongke.setChuot(tongmay);
                    thongke.setBanphim(tongmay);
                    thongke.setCpu(tongmay);

                    mChiTiet.thongkemaytinh(false,thongke);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mChiTiet.loitaidulieu();
                Log.e(TAG,databaseError.toString());
            }
        });

    }

    private void phantachdata_maytinh(DataSnapshot dataSnapshot, objthietbiphongmay_app thietbi_default){
        if(dataSnapshot.child("maytinhs").getValue()!=null) {
            List<objmaytinhs> listmaytinh = thietbi_default.getMaytinh();

            List<objlichsu_maytinhs> mlistMayTinhHu = new ArrayList<>();
            for (objmaytinhs mamay: listmaytinh) {
                if(dataSnapshot.child("maytinhs").child(mamay.getMamay()).getValue()!=null) //CÓ LỊCH SỬ
                {
                    GenericTypeIndicator<List<objlichsu_maytinhs>> gen = new GenericTypeIndicator<List<objlichsu_maytinhs>>(){};
                    List<objlichsu_maytinhs> listmayhu = dataSnapshot.child("maytinhs").child(mamay.getMamay()).getValue(gen);

                    if(!listmayhu.get(listmayhu.size()-1).isDasuachua()) //MÁY TÍNH CHƯA SỬA CHỬA
                    {
                        mlistMayTinhHu.add(listmayhu.get(listmayhu.size()-1));
                    }
                }
            }

            //**************THỐNG KÊ*********************;


            long chuothu =0;
            long manhinhhu =0;
            long cpuhu = 0;
            long banphimhu = 0;

            for (objlichsu_maytinhs j: mlistMayTinhHu) {
                objchitietthietbimaytinh obj = j.getChitietsuachua();
                if(!obj.isBanphim())
                    banphimhu++;
                if(!obj.isChuot())
                    chuothu++;
                if(!obj.isCpu())
                    cpuhu++;
                if(!obj.isManhinh())
                    manhinhhu++;
            }

            objthongkemaytinh_app thongke = new objthongkemaytinh_app();
            //TỔNG
            thongke.setBanphim(listmaytinh.size());
            thongke.setManhinh(listmaytinh.size());
            thongke.setChuot(listmaytinh.size());
            thongke.setCpu(listmaytinh.size());
            thongke.setMaytinh(listmaytinh.size());

            //HƯ
            thongke.setBanphimhu(banphimhu);
            thongke.setChuothu(chuothu);
            thongke.setCpuhu(cpuhu);
            thongke.setManhinhhu(manhinhhu);
            thongke.setMaytinhhu(mlistMayTinhHu.size());

            mChiTiet.thongkemaytinh(true,thongke);



        }else{//KHÔNG CÓ GIÁ TRỊ (máy tính tốt)
            long tongmay = thietbi_default.getMaytinh().size();
            objthongkemaytinh_app thongke = new objthongkemaytinh_app();
            thongke.setMaytinh(tongmay);
            thongke.setManhinh(tongmay);
            thongke.setChuot(tongmay);
            thongke.setBanphim(tongmay);
            thongke.setCpu(tongmay);

            mChiTiet.thongkemaytinh(false,thongke);
        }
    }

    private void phantachdata_thietbikhac(DataSnapshot dataSnapshot,objthietbikhacs default_values ){
        //XỬ LÝ NOTE thietbikhacs
        if(dataSnapshot.child("thietbikhacs").getValue()!=null){
            //CÓ LỊCH SỬ
            GenericTypeIndicator<List<objlichsu_thietbikhacs>> gen = new GenericTypeIndicator<List<objlichsu_thietbikhacs>>(){};
            List<objlichsu_thietbikhacs> mLS_TBK = dataSnapshot.child("thietbikhacs").getValue(gen);

            //CHƯA SỬA MỚI KIỂM TRA
            if(!mLS_TBK.get(mLS_TBK.size()-1).isDasuachua()) {
                objthietbikhacs chitiettbhu = mLS_TBK.get(mLS_TBK.size() - 1).getChitietsuachua().getThietbihu();
                objthietbikhacs thietbi_defautl = mLS_TBK.get(mLS_TBK.size() - 1).getChitietsuachua().getDefault_thietbi();
                mChiTiet.thongkethietbikhac(thietbi_defautl,chitiettbhu);

            }else{//THIẾT BỊ KHÁC BÌNH THƯỜNG (ĐÃ SỬA CHỮA)
                objthietbikhacs chitiettbhu = new objthietbikhacs(0,0,0,0,0,0,0,0,"");
                mChiTiet.thongkethietbikhac(default_values,chitiettbhu);
            }
        }else{ //THIẾT BỊ KHÁC BÌNH THƯỜNG (KHÔNG CÓ GIÁ TRỊ TRONG NODE)
            objthietbikhacs chitiettbhu = new objthietbikhacs(0,0,0,0,0,0,0,0,"");
            mChiTiet.thongkethietbikhac(default_values,chitiettbhu);
        }
    }

    public void getDanhSachMay(final String maphong){
        DatabaseReference mRef = mDatabase.getReference().child("thietbis")
                .child(maphong)
                .child("maytinhs");

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Lấy tất cả máy tính trong phòng máy
                //Lấy danh sách máy tính có node consudung == true;
                final List<objmaytinhs> mListMayTinh = new ArrayList<>();
                for (DataSnapshot maytinh : dataSnapshot.getChildren()) {
                    if(Boolean.parseBoolean(maytinh.child("consudung").getValue().toString()) == true)
                        mListMayTinh.add(maytinh.getValue(objmaytinhs.class));
                }


                //Child vào lịch sử lấy ra tình trạng
                DatabaseReference mRefLichSu = mDatabase.getReference()
                        .child("lichsusuachuas")
                        .child(maphong)
                        .child("maytinhs");
                mRefLichSu.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //PHÂN TÁCH DATA
                        ArrayList<objthietbimaytinh_app> mlist = new ArrayList<>();
                        for (DataSnapshot i : dataSnapshot.getChildren()) {
                            //Kiểm tra máy còn sử dụng hay không
                            boolean isConSuDung = false;
                            objmaytinhs maytinh = null;
                            for(objmaytinhs mt : mListMayTinh){
                                if(mt.getMamay().matches(i.getKey())){
                                    isConSuDung = true;
                                    maytinh = mt;
                                    break;
                                }
                            }
                            if(isConSuDung){
                                objthietbimaytinh_app mobj = new objthietbimaytinh_app();
                                mobj.setThietbi(maytinh);
                                GenericTypeIndicator<List<objlichsu_maytinhs>> gen = new GenericTypeIndicator<List<objlichsu_maytinhs>>(){};
                                List<objlichsu_maytinhs> mlistLichSu = i.getValue(gen);

                                mobj.setLichsusuachua(mlistLichSu.get(mlistLichSu.size()-1));
                                mlist.add(mobj);
                            }
                        }


                        List<objmaytinhs> maytemp = mListMayTinh;
                        for(objthietbimaytinh_app i : mlist){
                            //KIỂM TRA XEM CÓ MÁY CHƯA
                            for(int j = 0; j< mListMayTinh.size(); j++){
                                if(mListMayTinh.get(j).getMamay().matches(i.getThietbi().getMamay()))
                                    maytemp.remove(j);
                            }
                        }

                        //NHỮNG MÁY TỐT CÓ LỊCH SỬ LÀ ĐÃ SỬA
                        for(objmaytinhs mamay : maytemp){
                            objthietbimaytinh_app obj = new objthietbimaytinh_app();
                            obj.setThietbi(mamay);
                            objlichsu_maytinhs ls = new objlichsu_maytinhs();
                            ls.setDasuachua(true);
                            obj.setLichsusuachua(ls);
                            mlist.add(obj);
                        }

                        mChiTiet.danhsachmaytinh(mlist);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        mChiTiet.loitaidulieu();
                        Log.e(TAG,databaseError.toString());
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mChiTiet.loitaidulieu();
                Log.e(TAG,databaseError.toString());
            }
        });

    }


}
