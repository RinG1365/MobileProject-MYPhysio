package com.example.myphysio;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Appointment;
import model.CustomAdapterAppList;
import model.Physio;
import model.Profile;
import sqlitephysio.BookingDB;

import static java.lang.Boolean.FALSE;

public class AddAppointment extends AppCompatActivity  {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String strURL = "http://192.168.1.101:8888/myPhysioWebService/appointmentService.php";

    EditText edtTxtADate, edtTxtATime;
    Spinner spinnerAType, spinnerASession, spinnerAPhy;
    TextView txtVwAPrice;

    Physio physio;
    Appointment app;
    BookingDB bookingDB;
    Profile myProfile;
    ArrayList<Physio> physioList;
    List<String> listStaffid, listPhyName,listPhyRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);
        sharedPreferences = getApplicationContext().getSharedPreferences("MYPhysio",0);
        editor = sharedPreferences.edit();

        bookingDB = new BookingDB(getApplicationContext());
        myProfile = new Profile();
        myProfile.setIcno(sharedPreferences.getString("username",""));

        edtTxtADate = findViewById(R.id.edtTxtADate);
        edtTxtATime = findViewById(R.id.edtTxtATime);
        spinnerAType = findViewById(R.id.spinnerAType);
        spinnerASession = findViewById(R.id.spinnerASession);
        spinnerAPhy = findViewById(R.id.spinnerAPhy);
        txtVwAPrice = findViewById(R.id.txtVwAPrice);

        physio = new Physio();
        physioList = new ArrayList<>();

        spinnerAType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

               if(position == 0)
               {
                   spinnerASession.setEnabled(false);
                   txtVwAPrice.setText("1.00");
               }
               else
               {
                   spinnerASession.setEnabled(true);
                   txtVwAPrice.setText("50.00");
                   spinnerASession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                       @Override
                       public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



                           if(position == 0)
                           {

                               txtVwAPrice.setText("50.00");
                           }
                           else
                           {
                               txtVwAPrice.setText("100.00");
                           }

                       }

                       @Override
                       public void onNothingSelected(AdapterView<?> parent) {

                       }
                   });

               }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        spinnerAPhy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

               physio.setStaffid(listStaffid.get(position));

                Toast.makeText(getApplicationContext(),"Physiotherapist Rating: "+listPhyRate.get(position),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /* Get Phy */
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, strURL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response)
                    {
                        try {

                            JSONArray jsnArr = new JSONArray(response);

                            listStaffid = new ArrayList<String>();
                            listPhyName = new ArrayList<String>();
                            listPhyRate = new ArrayList<String>();

                            for(int i=0;i<jsnArr.length();i++)
                            {
                                JSONObject jObj = jsnArr.getJSONObject(i);

                               listStaffid.add(jObj.optString("STAFFID"));
                               listPhyName.add(jObj.optString("PHY_NAME"));
                               listPhyRate.add(jObj.optString("RATING"));

                            }

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AddAppointment.this,
                                    android.R.layout.simple_spinner_item, listPhyName);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerAPhy.setAdapter(dataAdapter);



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
                params.put("selectFn","fnSelectPhy");

                return params;
            }
        };
        requestQueue.add(stringRequest);
        /*END Get Phy */






    }



    public void fnBookApp(View view){



        /* Add Book */
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, strURL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response)
                    {
                        try {

                            JSONObject jobj = new JSONObject(response);

                            String respond = jobj.optString("respond");

                            if(respond.equalsIgnoreCase("1"))
                            {
                                app = new Appointment();
                                app.setBdate(edtTxtADate.getText().toString());
                                app.setBtime(edtTxtATime.getText().toString());
                                app.setBtype(spinnerAType.getSelectedItem().toString());
                                app.setBprice(txtVwAPrice.getText().toString());
                                app.setBcat(spinnerASession.getSelectedItem().toString());
                                app.setBprice(txtVwAPrice.getText().toString());
                                app.setBstatus("BOOK");

                                float result = bookingDB.fnInsertBooking(app);

                                if(result == 0)
                                {
                                    Toast.makeText(getApplicationContext(),"Fail to add into sqlite",Toast.LENGTH_SHORT).show();

                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Added into sqlite",Toast.LENGTH_SHORT).show();

                                }

                                Toast.makeText(getApplicationContext(),"Book Successfully",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddAppointment.this, AppointmentActivity.class);
                                startActivity(intent);

                            }
                            else if(respond.equalsIgnoreCase("0"))
                            {
                                Toast.makeText(getApplicationContext(),"Fail to Book",Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),respond,Toast.LENGTH_SHORT).show();
                            }

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
                params.put("selectFn","fnAddBook");
                params.put("icno",myProfile.getIcno());
                params.put("staffid",physio.getStaffid());
                params.put("bdate",edtTxtADate.getText().toString());
                params.put("btime",edtTxtATime.getText().toString());
                params.put("btype",spinnerAType.getSelectedItem().toString());
                params.put("bsession",spinnerASession.getSelectedItem().toString());
                params.put("bprice", txtVwAPrice.getText().toString());


                return params;
            }
        };
        requestQueue.add(stringRequest);
        /*END Add Book */

    }

    public void fnBackFATA(View view){
        Intent intent = new Intent(this, AppointmentActivity.class);
        startActivity(intent);
    }
}
