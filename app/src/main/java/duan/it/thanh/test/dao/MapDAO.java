package duan.it.thanh.test.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import duan.it.thanh.test.db.DBase;
import duan.it.thanh.test.model.Maps;

public class MapDAO {
    private SQLiteDatabase db;
    private DBase dbql;
    public MapDAO(Context context){
        dbql = new DBase(context);
        db = dbql.getWritableDatabase();
    }
    public List<Maps> getPr(String sql, String...selectionArgs){
        List<Maps> list = new ArrayList<>();
        Cursor c =db.rawQuery(sql,selectionArgs);
        while (c.moveToNext()){
            Maps pr = new Maps();
            pr.id = c.getInt(c.getColumnIndex("ID"));
            pr.v = c.getString(c.getColumnIndex("V"));
            pr.v1 = c.getString(c.getColumnIndex("V1"));
            pr.tiltles = c.getString(c.getColumnIndex("TITLE"));
            list.add(pr);
        }
        return list;
    }
    public List<Maps> getMapAll(){
        String sql = "SELECT * FROM MAP";
        return getPr(sql);
    }

    public long insert(Maps p){
        ContentValues values = new ContentValues();
        values.put("V",p.v);
        values.put("V1",p.v1);
        values.put("TITLE",p.tiltles);
        return db.insert("MAP",null,values);
    }
    public int update(Maps p){
        ContentValues values = new ContentValues();
        values.put("V",p.v);
        values.put("V1",p.v1);
        values.put("TITLE",p.tiltles);
        return db.update("MAP",values,"ID=?",new String[]{String.valueOf(p.id)});
    }
    public int delete(int id){
        return db.delete("MAP","ID=?",new String[]{String.valueOf(id)});
    }

}
