package sqlitephysio;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import model.HealthRecord;

public class HealthRecordDB extends SQLiteOpenHelper
{
    public static final String dbName = "myPhysio";
    public static final String tblNameHR = "healthrecords";
    public static final String colHRRefer = "REFER_NUMBER";
    public static final String colHRDate = "RECORD_DATE";
    public static final String colHRSubject = "SUBJECTIVEAX";
    public static final String colHRObject = "OBJECTIVEAX";
    public static final String colHRAnalysis = "ANALYSIS";
    public static final String colHRProblem = "PROBLEMLIST";
    public static final String colHRIntervention = "INTERVENTION";
    public static final String colHREvaluation = "EVALUATION";
    public static final String colHRReview = "REVIEW";
    public static final String colHRStaff = "STAFFID";
    public static final String colHRId = "HR_ID";


    public static final String strCrtTblHR ="CREATE TABLE " + tblNameHR + " ( " +
            colHRId + " INTEGER PRIMARY KEY, " + colHRRefer + " TEXT, " + colHRDate + " TEXT, " + colHRSubject +
            " TEXT, "+ colHRObject + " TEXT, "+ colHRAnalysis + " TEXT, "+ colHRProblem +
            " TEXT, "+ colHRIntervention + " TEXT,"+ colHREvaluation + " TEXT, "+ colHRReview +
            " TEXT, "+ colHRStaff +" TEXT)";

    public static final String strDrpTblHR = "DROP TABLE IF EXISTS "+ tblNameHR;

    public HealthRecordDB(Context context)
    {
        super(context, dbName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(strCrtTblHR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(strDrpTblHR);
        onCreate(db);
    }

    public float fnInsertHR(HealthRecord myHR)
    {
        float retResult = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(colHRRefer, myHR.getRecordNumber());
        values.put(colHRDate, myHR.getRecordDate());
        values.put(colHRSubject, myHR.getSubAx());
        values.put(colHRObject, myHR.getObjAx());
        values.put(colHRAnalysis, myHR.getAnalysis());
        values.put(colHRProblem, myHR.getProblemList());
        values.put(colHRIntervention, myHR.getIntervention());
        values.put(colHREvaluation, myHR.getEvaluation());
        values.put(colHRReview, myHR.getReview());
        values.put(colHRStaff, myHR.getStaffID());

        retResult = db.insert(tblNameHR,null,values);
        return retResult;

    }

}
