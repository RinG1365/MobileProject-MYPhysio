package sqlitephysio;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import model.Appointment;


public class BookingDB extends SQLiteOpenHelper
{
    public static final String dbName="myPhysio";
    public static final String tblBooking="BOOKING";
    public static final String colBookingID="BOOKINGID";
    public static final String colBookingDate="BOOKINGDATE";
    public static final String colBookingTime="BOOKINGTIME";
    public static final String colBookingType="BOOKINGTYPE";
    public static final String colBookingCat="BOOKINGCAT";
    public static final String colBookingPrice="BOOKINGPRICE";
    public static final String colBookingStatus="BOOKINGSTATUS";

    public static final String strCrtTblBooking="CREATE TABLE "+tblBooking+"( "+colBookingID+" INTEGER PRIMARY KEY, "+colBookingDate+" TEXT, "+colBookingTime+" TEXT, "+colBookingType+" TEXT, "
            +colBookingCat+" TEXT, "+colBookingPrice+" TEXT, "+colBookingStatus+" TEXT) ";
    public static final String strDrpTblBooking="DROP TABLE IF EXISTS "+tblBooking;

    public BookingDB(Context context)
    {
        super(context,dbName,null,1);
    }


    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(strCrtTblBooking);
    }

    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion)
    {
        db.execSQL(strDrpTblBooking);
        onCreate(db);

    }

    public float fnInsertBooking(Appointment Bmodel)
    {
        float retResult= 0;
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(colBookingDate,Bmodel.getBdate());
        values.put(colBookingTime,Bmodel.getBtime());
        values.put(colBookingType,Bmodel.getBtype());
        values.put(colBookingCat,Bmodel.getBcat());
        values.put(colBookingPrice,Bmodel.getBprice());
        values.put(colBookingStatus,Bmodel.getBstatus());

        retResult = db.insert(tblBooking,null,values);
        return retResult;

    }


}
