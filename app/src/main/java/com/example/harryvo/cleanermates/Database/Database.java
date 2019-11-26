package com.example.harryvo.cleanermates.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.harryvo.cleanermates.Model.Booking;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {
    private static final String DB_NAME="CleanDB.db";
    private static final int DB_VER=1;
    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public List<Booking> getCarts()
    {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String [] sqlSelect={"IDClean","Kindofclean","Room","Price","Discount"};
        String sqlTable="BookingDetail";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db,sqlSelect,null,null,null,null,null);

        final  List<Booking> result = new ArrayList<>();
        if (c.moveToFirst())
        {
            do{
                result.add(new Booking(c.getString(c.getColumnIndex("IDClean")),
                        c.getString(c.getColumnIndex("Kindofclean")),
                        c.getString(c.getColumnIndex("Room")),
                        c.getString(c.getColumnIndex("Price")),
                        c.getString(c.getColumnIndex("Discount"))
                        ));
            }while (c.moveToNext());
        }
        return result;
    }

    public  void  addToCart(Booking booking)
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO BookingDetail(IDClean,Kindofclean,Room,Price,Discount) VALUES('%s','%s','%s','%s','%s');",
                booking.getIDClean(),
                booking.getKingofclean(),
                booking.getRoom(),
                booking.getPrice(),
                booking.getDiscount());

        db.execSQL(query);
    }

    public  void  cleanCart()
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM BookingDetail");

        db.execSQL(query);
    }
}
