package com.lbt.batro.tinhtrangthietbi.models.clsApp;

import com.lbt.batro.tinhtrangthietbi.models.clsFireBase.objmaytinhs;
import com.lbt.batro.tinhtrangthietbi.models.clsFireBase.objthietbikhacs;

import java.util.List;

public class objthietbiphongmay_app {

    String maphong;
    List<objmaytinhs> maytinh;
    objthietbikhacs thietbikhacs;

    public objthietbiphongmay_app(String maphong, List<objmaytinhs> maytinh, objthietbikhacs thietbikhacs) {
        this.maphong = maphong;
        this.maytinh = maytinh;
        this.thietbikhacs = thietbikhacs;
    }

    public objthietbiphongmay_app() {
    }

    public String getMaphong() {
        return maphong;
    }

    public void setMaphong(String maphong) {
        this.maphong = maphong;
    }

    public List<objmaytinhs> getMaytinh() {
        return maytinh;
    }

    public void setMaytinh(List<objmaytinhs> maytinh) {
        this.maytinh = maytinh;
    }

    public objthietbikhacs getThietbikhacs() {
        return thietbikhacs;
    }

    public void setThietbikhacs(objthietbikhacs thietbikhacs) {
        this.thietbikhacs = thietbikhacs;
    }
}
