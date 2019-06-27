package com.example.student.pedometerx;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.components.Description;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class HomeFragment extends Fragment {

    static TextView  TvSteps;
    Button btnplaypause;
    static PieChart mPieChart;
    public static DBclass db;
    public static String curstatus = "";
    static TextView tvspeed,tvdistance, tvcalburned, tvtimer;

    @Nullable

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        db = new DBclass(getActivity());
        TvSteps = (TextView) v.findViewById(R.id.tv_steps);
        btnplaypause = (Button) v.findViewById(R.id.btnplaypause);
        TvSteps.setText("" + MainActivity.numSteps);
        mPieChart = (PieChart) v.findViewById(R.id.piechart);
        mPieChart.setDrawValueInPie(false);
        tvspeed = (TextView) v.findViewById(R.id.txtspeed);
        tvdistance = (TextView) v.findViewById(R.id.txtdistance);
        tvcalburned = (TextView) v.findViewById(R.id.txtcalburned);
        tvtimer = (TextView) v.findViewById(R.id.timer);


        ArrayList<dailyrecord> dr = db.selectDailyrecords();
        if (dr.size() == 0) {
            db.adddailyrecord(MainActivity.getdatetod(), 0, 0.0, 0.0, 0.0, "pause", 0);
        }

        MainActivity.newday();

        if (curstatus.equals("pause")) {
            btnplaypause.setBackground(getActivity().getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp));
        } else {
            btnplaypause.setBackground(getActivity().getResources().getDrawable(R.drawable.ic_pause_black_24dp));
            if (isMyServiceRunning(MyService.class, getActivity())) {

            } else {
                getActivity().startService(new Intent(getActivity(), MyService.class));
            }
        }


        TvSteps.setText(dr.get(dr.size() - 1).steps + "");
        tvdistance.setText(dr.get(dr.size() - 1).distances + " km");
        tvspeed.setText(dr.get(dr.size() - 1).speeds + " km/h");
        tvtimer.setText(String.format("%02d:%02d:%02d", dr.get(dr.size() - 1).time / 3600,
                (dr.get(dr.size() - 1).time % 3600) / 60, (dr.get(dr.size() - 1).time % 60)));
        tvcalburned.setText(dr.get(dr.size() - 1).calburned + " kcal");

        updatechart();
        mPieChart.startAnimation();
        btnplaypause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<dailyrecord> dr = db.selectDailyrecords();
                curstatus = dr.get(dr.size() - 1).status;
                if (curstatus.equals("play")) {
                    btnplaypause.setBackground(getActivity().getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp));
                    db.updatestatus("pause", MainActivity.getdatetod());
                    getActivity().stopService(new Intent(getActivity(), MyService.class));

                } else {
                    btnplaypause.setBackground(getActivity().getResources().getDrawable(R.drawable.ic_pause_black_24dp));
                    db.updatestatus("play", MainActivity.getdatetod());
                    getActivity().startService(new Intent(getActivity(), MyService.class));


                }


            }
        });
        return v;
    }

    public static boolean isMyServiceRunning(Class<?> serviceClass, Context c) {
        ActivityManager manager = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    public static void setText(String text) {
        if (TvSteps != null) {
            TvSteps.setText(text);
            mPieChart.clearChart();
            updatechart();
        }
    }

    public static void updatechart() {
        if (TvSteps != null) {
            ArrayList<dailyrecord> dr = db.selectDailyrecords();
            if (dr.get(dr.size() - 1).steps < 7500)
                mPieChart.addPieSlice(new PieModel("Empty", 7500 - dr.get(dr.size() - 1).steps, Color.parseColor("#424242")));
            mPieChart.addPieSlice(new PieModel("Achieved", dr.get(dr.size() - 1).steps, Color.parseColor("#f29312")));
        }
    }

    //距离计算
    public static void calcldistance(double distance, Context context) {
        DBclass dbl = new DBclass(context);
        ArrayList<dailyrecord> dr = dbl.selectDailyrecords();
        double convert = 0.6 * 0.001;               //默认每步长0.6m
        distance = dr.get(dr.size() - 1).steps * convert;
        String rounded = String.format("%.3f", distance).replaceAll("0*$", "");
        dbl.updatedistance(Double.parseDouble(rounded), MainActivity.getdatetod());
        if (tvdistance != null) {
            tvdistance.setText(rounded + " km");
        }
    }
    //速度计算
    public static void calcspeed(double getdistance, Context context) {
        DBclass dbl = new DBclass(context);
        ArrayList<dailyrecord> dr = dbl.selectDailyrecords();
        Double speed = getdistance / dr.get(dr.size() - 1).time * (3600);
        String rounded = String.format("%.3f", speed).replaceAll("0*$", "");
        dbl.updatedspeed(Double.parseDouble(rounded), MainActivity.getdatetod());
        if (tvspeed != null) {
            tvspeed.setText(rounded + " km/h");
        }
    }
    //计时
    public static void calculatetime(String time) {
        if (tvtimer != null) {
            tvtimer.setText(time);
        }
    }

        //计算卡路里：公式：已知体重、距离 ：跑步热量（kcal）＝体重（kg）×距离（公里）×1.036
    public static void calculatecalburned(Context context) {
        DBclass dbl = new DBclass(context);
        ArrayList<dailyrecord> dr = dbl.selectDailyrecords();
        double kgtopounds = 60 * 1.036;            //默认体重为60kg
        double calburned = dr.get(dr.size() - 1).distances * kgtopounds;
        String rounded = String.format("%.2f", calburned).replaceAll("0*$", "");
        dbl.updatecalburned(Double.parseDouble(rounded), MainActivity.getdatetod());
        if (tvcalburned != null) {
            tvcalburned.setText(rounded + " kcal");
        }
    }

}
