package com.lbt.batro.tinhtrangthietbi.models.clsApp;

import com.lbt.batro.tinhtrangthietbi.models.clsFireBase.objlichsu_maytinhs;
import com.lbt.batro.tinhtrangthietbi.models.clsFireBase.objmaytinhs;

import java.io.Serializable;

public class objthietbimaytinh_app implements Serializable {

    objmaytinhs thietbi;
    objlichsu_maytinhs lichsusuachua;



    public objthietbimaytinh_app() {
    }

    public objthietbimaytinh_app(objmaytinhs thietbi, objlichsu_maytinhs lichsusuachua) {
        this.thietbi = thietbi;
        this.lichsusuachua = lichsusuachua;
    }

    public objmaytinhs getThietbi() {
        return thietbi;
    }

    public void setThietbi(objmaytinhs thietbi) {
        this.thietbi = thietbi;
    }

    public objlichsu_maytinhs getLichsusuachua() {
        return lichsusuachua;
    }

    public void setLichsusuachua(objlichsu_maytinhs lichsusuachua) {
        this.lichsusuachua = lichsusuachua;
    }
}
