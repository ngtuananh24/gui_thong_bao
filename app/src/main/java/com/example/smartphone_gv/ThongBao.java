package com.example.smartphone_gv;

public class ThongBao {
    private String tieude;
    private String noidung;
    private String pick;
    private String time;

    // Constructor không tham số cần thiết cho Firebase
    public ThongBao() {
    }

    public ThongBao(String pick, String tieude, String noidung, String time) {
        this.pick = pick;
        this.tieude = tieude;
        this.noidung = noidung;
        this.time = time;
    }

    public String getTieude() {
        return tieude;
    }

    public void setTieude(String tieude) {
        this.tieude = tieude;
    }

    public String getNoidung() {
        return noidung;
    }

    public void setNoidung(String noidung) {
        this.noidung = noidung;
    }

    public String getPick() {
        return pick;
    }

    public void setPick(String pick) {
        this.pick = pick;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public String toString() {
        return "Người nhận: " + pick + "\nTiêu đề: " + tieude + "\nNội dung: " + noidung + "\nThời gian: " + time;
    }
}
