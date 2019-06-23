package com.example.myphysio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import model.CustomAdapterHRList;
import model.HealthRecord;

public class ViewHealthRecord extends AppCompatActivity {

    HealthRecord hr;
    String strURL = "http://192.168.1.101:8888/myPhysioWebService/healthRecordService.php";
    TextView txtVwRN,txtVwSubAx,txtVwObjAx,txtVwAnalysis, txtVwPL,txtVwInter,txtVwEva,txtVwRev,txtVwStaff,txtVwRD;
    Button btnBackHR;
    ImageView imgVw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_health_record);

        hr = new HealthRecord();

        Intent intent = getIntent();
        hr.setRecordNumber(intent.getStringExtra("referNumber"));


        btnBackHR = findViewById(R.id.btnBackHR);
        txtVwRN = findViewById(R.id.txtVwFN);
        txtVwRD = findViewById(R.id.txtVwRD);
        txtVwSubAx = findViewById(R.id.txtVwSubAx);
        txtVwObjAx = findViewById(R.id.txtVwObjAx);
        txtVwAnalysis = findViewById(R.id.txtVwAnalysis);
        txtVwPL = findViewById(R.id.txtVwPL);
        txtVwInter = findViewById(R.id.txtVwInter);
        txtVwEva = findViewById(R.id.txtVwEva);
        txtVwRev = findViewById(R.id.txtVwRev);
        txtVwStaff = findViewById(R.id.txtVwStaff);


        txtVwRN.setText(hr.getRecordNumber() + "  Health Record");

        /* Get HR */
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, strURL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response)
                    {
                        try {

                            JSONObject jobj = new JSONObject(response);
                            txtVwRD.setText(jobj.optString("RECORD_DATE"));
                            txtVwSubAx.setText(jobj.optString("SUBJECTIVEAX"));
                            txtVwObjAx.setText(jobj.optString("OBJECTIVEAX"));
                            txtVwAnalysis.setText(jobj.optString("ANALYSIS"));
                            txtVwPL.setText(jobj.optString("PROBLEMLIST"));
                            txtVwInter.setText(jobj.optString("INTERVENTION"));
                            txtVwEva.setText(jobj.optString("EVALUATION"));
                            txtVwRev.setText(jobj.optString("REVIEW"));
                            txtVwStaff.setText(jobj.optString("STAFFID"));


                        } catch (Exception ee) {
                            Toast.makeText(getApplicationContext(),ee.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }



                },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();

            }

        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("selectFn","fnGetHR");
                params.put("referNumber",hr.getRecordNumber());

                return params;
            }
        };
        requestQueue.add(stringRequest);
       /* END Get HR */

    }



    public void fnBackHR(View view){
        Intent intent = new Intent(this, HealthRecordActivity.class);
        startActivity(intent);
    }
}
