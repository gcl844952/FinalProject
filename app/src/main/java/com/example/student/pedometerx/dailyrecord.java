package com.example.student.pedometerx;

public class dailyrecord {
    public dailyrecord(String da, long st, double s, double c, double d, String sta, long t){
        dates = da;         //日期
        steps = st;         //步数
        calburned = c;      //卡路里
        distances = d;      //距离
        speeds = s;         //速度
        status = sta;       //按钮操作
        time = t;           //时间
    }
    public String dates, status;
    public long steps,time;
    public double calburned,distances,speeds;
}


