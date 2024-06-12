package com.example.bookingflight.model;

public class Airport {
    private String maSanBay , tenSanBay, diaDiem;

    public String getMaSanBay() {
        return maSanBay;
    }

    public void setMaSanBay(String maSanBay) {
        this.maSanBay = maSanBay;
    }

    public String getTenSanBay() {
        return tenSanBay;
    }

    public void setTenSanBay(String tenSanBay) {
        this.tenSanBay = tenSanBay;
    }

    public String getDiaDiem() {
        return diaDiem;
    }

    public void setDiaDiem(String diaDiem) {
        this.diaDiem = diaDiem;
    }

    public Airport(String maSanBay, String tenSanBay, String diaDiem) {
        this.maSanBay = maSanBay;
        this.tenSanBay = tenSanBay;
        this.diaDiem = diaDiem;
    }

    @Override
    public String toString() {
        return "Airport{" +
                "maSanBay='" + maSanBay + '\'' +
                ", tenSanBay='" + tenSanBay + '\'' +
                ", diaDiem='" + diaDiem + '\'' +
                '}';
    }


}
