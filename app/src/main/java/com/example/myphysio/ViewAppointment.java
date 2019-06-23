package com.example.myphysio;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import model.Appointment;

public class ViewAppointment extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String strURL = "http://192.168.1.101:8888/myPhysioWebService/appointmentService.php";
    Appointment app;

    TextView txtVwBid, txtVwBDate, txtVwBTime, txtVwBType, txtVwBCat, txtVwBPrice, txtVwBStatus, txtVwBStaff,txtVwHos;
    Button btnDelete,btnPay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointment);
        sharedPreferences = getApplicationContext().getSharedPreferences("MYPhysio",0);
        editor = sharedPreferences.edit();

        Intent intent = getIntent();
        app = new Appointment();
        app.setBid(intent.getStringExtra("bid"));

        txtVwBid = findViewById(R.id.txtVwBid);
        txtVwBDate = findViewById(R.id.txtVwBDate);
        txtVwBTime = findViewById(R.id.txtVwBTime);
        txtVwBType = findViewById(R.id.txtVwBType);
        txtVwBCat = findViewById(R.id.txtVwBCat);
        txtVwBPrice = findViewById(R.id.txtVwBPrice);
        txtVwBStatus = findViewById(R.id.txtVwBStatus);
        txtVwBStaff = findViewById(R.id.txtVwBStaff);
        txtVwHos = findViewById(R.id.txtVwHos);
        btnDelete = findViewById(R.id.btnDeleteApp);
        btnPay = findViewById(R.id.btnPay);

        txtVwBid.setText(app.getBid());

        /* Get App */
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, strURL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response)
                    {
                        try {

                            JSONObject jobj = new JSONObject(response);
                            txtVwBDate.setText(jobj.optString("BOOKING_DATE"));
                            txtVwBTime.setText(jobj.optString("BOOKING_TIME"));
                            txtVwBType.setText(jobj.optString("BOOKING_TYPE"));
                            txtVwBCat.setText(jobj.optString("BOOKING_CAT"));
                            txtVwBPrice.setText(jobj.optString("BOOKING_PRICE"));
                            txtVwBStatus.setText(jobj.optString("BOOKING_STATUS"));
                            txtVwBStaff.setText(jobj.optString("PHY_NAME"));
                            txtVwHos.setText(jobj.optString("HOSPITAL"));


                            String status = jobj.optString("BOOKING_STATUS");

                           if(status.equalsIgnoreCase("UNPAID") )
                            {
                                btnDelete.setVisibility(View.INVISIBLE);
                            }

                            else if(status.equalsIgnoreCase("BOOK"))
                            {
                                btnPay.setVisibility(View.INVISIBLE);
                            }

                            else if(status.equalsIgnoreCase("PAID"))
                           {
                               btnDelete.setVisibility(View.INVISIBLE);
                               btnPay.setVisibility(View.INVISIBLE);

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
                params.put("selectFn","fnGetApp");
                params.put("bid",app.getBid());

                return params;
            }
        };
        requestQueue.add(stringRequest);
        /* END Get App*/

    }

    public void fnPayApp(View view)
    {
        Intent intent = new Intent(ViewAppointment.this, PaymentActivity.class);
        intent.putExtra("bid",app.getBid());
        intent.putExtra("price",txtVwBPrice.getText().toString());
        startActivity(intent);
    }

    public void fnDeleteApp(View view)
    {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Are you sure you want to delete?");
        alertBuilder.setPositiveButton(getString(R.string.btnPositive), new
                DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        /* Delete App */
                        RequestQueue requestQueue = Volley.newRequestQueue(ViewAppointment.this);
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
                                                Toast.makeText(getApplicationContext(),"Delete Successfully",Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(ViewAppointment.this, AppointmentActivity.class);
                                                startActivity(intent);

                                            }
                                            else if(respond.equalsIgnoreCase("0"))
                                            {
                                                Toast.makeText(getApplicationContext(),"Fail to Delete",Toast.LENGTH_SHORT).show();

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
                                params.put("selectFn","fnDeleteApp");
                                params.put("bid",app.getBid());

                                return params;
                            }
                        };
                        requestQueue.add(stringRequest);
                        /* END Delete App*/




                    }
                }
        );

        alertBuilder.setNegativeButton(getString(R.string.btnNegative), new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });


        alertBuilder.show();
    }

    public void fnBackApp(View view){
        Intent intent = new Intent(this, AppointmentActivity.class);
        startActivity(intent);
    }
}
