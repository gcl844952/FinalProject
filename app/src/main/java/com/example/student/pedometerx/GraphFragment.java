package com.example.student.pedometerx;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.ChartData;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class GraphFragment extends Fragment {
    ListView lvtimline;
    static ConstraintLayout constraintLayout;
    AlertDialog.Builder builder;
    Button btnreset ;
    public DBclass db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_graphs, container, false);
        lvtimline = (ListView)v.findViewById(R.id.lvtimeline);
        GraphFragment.TimeLineAdapter timeLineAdapter = new GraphFragment.TimeLineAdapter();
        lvtimline.setAdapter(timeLineAdapter);
        db = new DBclass(getActivity());
        btnreset = (Button)v.findViewById(R.id.btnreset);
        ArrayList<dailyrecord> dr = db.selectDailyrecords();
        builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Title");
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

        //重置应用
        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirm");
                builder.setMessage("确定重置今日数据吗?");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        db.updateReset(0,0.0,0.0,0.0,0,"pause",MainActivity.getdatetod());
                        if(HomeFragment.isMyServiceRunning(MyService.class,getActivity())){
                            getActivity().stopService(new Intent(getActivity(), MyService.class));
                        }
                        Toast.makeText(getActivity(),"今日数据已重置",Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            }
        });
        return v;
    }

    class TimeLineAdapter extends BaseAdapter{
        DBclass db = new DBclass(getActivity());
        ArrayList<dailyrecord> dr = db.selectDailyrecords();
        @Override
        public int getCount() {
            return dr.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v1 = getLayoutInflater().inflate(R.layout.timeline_list,null);
            TextView txtdate =(TextView)v1.findViewById(R.id.txtdatel);
            TextView txtstep =(TextView)v1.findViewById(R.id.txtstep);
            TextView txtcb = (TextView)v1.findViewById(R.id.tvcalburnedl);
            TextView txtspeed= (TextView)v1.findViewById(R.id.tvspeedl);
            TextView txtdis=(TextView)v1.findViewById(R.id.tvdistancel);
            TextView txttime = (TextView)v1.findViewById(R.id.tvttimel);

            txtdate.setText(dr.get(i).dates);
            txtstep.setText(dr.get(i).steps+" steps");
            txtcb.setText(dr.get(i).calburned+" kcal");
            txtspeed.setText(dr.get(i).speeds+" km/h");
            txtdis.setText(dr.get(i).distances+" km");
            txttime.setText(dr.get(i).time+"");
            return v1;
        }
    }

    public static Boolean chkifvisible(){
        Boolean b=false;
        if(constraintLayout.getVisibility() == View.VISIBLE){
            b=true;
        }
        return b;
    }
}
