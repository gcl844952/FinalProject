package com.example.student.pedometerx;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBclass extends SQLiteOpenHelper {

    public DBclass(Context context){
        super(context, "pedometer.db",null,1);
    }
    //基本数据
    public void adddailyrecord(String date, long steps,double calburned, double distance, double speed, String status,long time){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues p = new ContentValues();
        p.put("DATE", date);
        p.put("STEPS", steps);
        p.put("CALBURNED", calburned);
        p.put("DISTANCE", distance);
        p.put("SPEED", speed);
        p.put("STATUS", status);
        p.put("TIME", time);
        db.insert("DAILYRECORD",null,p);
    }


    public void updatestatus(String status,String date){
        SQLiteDatabase db = this.getReadableDatabase();
        String strSQL = "UPDATE DAILYRECORD SET STATUS = '"+status+"' WHERE DATE ='"+ date +"'";
        db.execSQL(strSQL);
        db.close();
    }

    public void updatesteps(long steps, String date){
        SQLiteDatabase db = this.getReadableDatabase();
        String strSQL = "UPDATE DAILYRECORD SET STEPS = '"+steps+"'WHERE DATE ='"+ date +"'";
        db.execSQL(strSQL);
        db.close();
    }


    public void updateReset(long steps, double distance, double speed, double calburned, long time, String status, String Date){
        SQLiteDatabase db = this.getReadableDatabase();
        String strSQL = "UPDATE DAILYRECORD SET STEPS='"+steps +"', DISTANCE = '"+distance+"', SPEED = '"+speed+"', STATUS ='"+status+"',CALBURNED = '"+calburned+"',TIME='"+time+"' WHERE DATE ='"+Date+"'";
        db.execSQL(strSQL);
        db.close();
    }


    public void updatedistance(double distance, String date){
        SQLiteDatabase db = this.getReadableDatabase();
        String strSQL = "UPDATE DAILYRECORD SET DISTANCE = '"+distance+"' WHERE DATE ='"+ date +"'";
        db.execSQL(strSQL);
        db.close();
    }


    public void updatedspeed(double speed, String date){
        SQLiteDatabase db = this.getReadableDatabase();
        String strSQL = "UPDATE DAILYRECORD SET SPEED = '"+speed+"'WHERE DATE ='"+ date +"'";
        db.execSQL(strSQL);
        db.close();
    }


    public void updatetime(long time, String date){
        SQLiteDatabase db = this.getReadableDatabase();
        String strSQL = "UPDATE DAILYRECORD SET TIME ='"+time+"'WHERE DATE ='"+ date +"'";
        db.execSQL(strSQL);
        db.close();
    }


    public void updatecalburned(double calburned, String date){
        SQLiteDatabase db = this.getReadableDatabase();
        String strSQL = "UPDATE DAILYRECORD SET CALBURNED ='"+calburned+"' WHERE DATE ='"+ date +"'";
        db.execSQL(strSQL);
        db.close();
    }


    public ArrayList<dailyrecord> selectDailyrecords() {
        ArrayList<dailyrecord> result = new ArrayList<dailyrecord>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM DAILYRECORD",null);
        c.moveToFirst();
        while (c.isAfterLast() == false){
            String date = c.getString(c.getColumnIndex("DATE"));
            String status = c.getString(c.getColumnIndex("STATUS"));
            long step = c.getLong(c.getColumnIndex("STEPS"));
            double speed = c.getDouble(c.getColumnIndex("SPEED"));
            double calburned = c.getDouble(c.getColumnIndex("CALBURNED"));
            double distance = c.getDouble(c.getColumnIndex("DISTANCE"));
            long time = c.getLong(c.getColumnIndex("TIME"));

            dailyrecord dr = new dailyrecord(date,step,speed,calburned,distance,status,time);
            result.add(dr);
            c.moveToNext();
        }
        return result;
    }

    @Override
    public String getDatabaseName() {
        return super.getDatabaseName();
    }

    @Override
    public void setWriteAheadLoggingEnabled(boolean enabled) {
        super.setWriteAheadLoggingEnabled(enabled);
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        return super.getReadableDatabase();
    }

    @Override
    public synchronized void close() {
        super.close();
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE DAILYRECORD (DATE TEXT,STEPS LONG, CALBURNED DOUBLE, DISTANCE DOUBLE, SPEED DOUBLE, STATUS TEXT, TIME LONG)");
        sqLiteDatabase.execSQL("CREATE TABLE ACHIEVEMENT (ID INT , TYPE TEXT, TOTAL DOUBLE, SETS INT, SETSACHIEVE INT, STATUS TEXT, STATTODAY TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}
