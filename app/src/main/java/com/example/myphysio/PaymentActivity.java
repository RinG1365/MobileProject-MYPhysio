package com.example.myphysio;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

import model.Appointment;

public class PaymentActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String strURL = "http://192.168.1.101:8888/myPhysioWebService/paymentService.php";
    Appointment app;

    TextView txtVwPbid, txtVwPtype, txtVwPprice;
    EditText edtTxtPDate, edtTxtPTime, edtTxtPCredit, edtTxtPExpired, edtTxtPCvv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        sharedPreferences = getApplicationContext().getSharedPreferences("MYPhysio",0);
        editor = sharedPreferences.edit();

        Intent intent = getIntent();
        app = new Appointment();
        app.setBid(intent.getStringExtra("bid"));
        app.setBprice(intent.getStringExtra("price"));

        txtVwPbid = findViewById(R.id.txtVwPbid);
        txtVwPtype = findViewById(R.id.txtVwPtype);
        txtVwPprice = findViewById(R.id.txtVwPprice);
        edtTxtPDate = findViewById(R.id.edtTxtPDate);
        edtTxtPTime = findViewById(R.id.edtTxtPTime);
        edtTxtPCredit = findViewById(R.id.edtTxtPCredit);
        edtTxtPExpired = findViewById(R.id.edtTxtPexpired);
        edtTxtPCvv = findViewById(R.id.edtTxtPCvv);

        txtVwPbid.setText(app.getBid());
        txtVwPprice.setText(app.getBprice());


    }

    public void fnConfirmPay(View view)
    {

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Are you sure you want to pay?");
        alertBuilder.setPositiveButton(getString(R.string.btnPositive), new
                DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        /* Delete App */
                        RequestQueue requestQueue = Volley.newRequestQueue(PaymentActivity.this);
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
                                                Toast.makeText(getApplicationContext(),"Pay Successfully",Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(PaymentActivity.this, AppointmentActivity.class);
                                                startActivity(intent);

                                            }
                                            else if(respond.equalsIgnoreCase("0"))
                                            {
                                                Toast.makeText(getApplicationContext(),"Fail to Pay",Toast.LENGTH_SHORT).show();

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
                                params.put("selectFn","fnPayApp");
                                params.put("bid",app.getBid());
                                params.put("pdate",edtTxtPDate.getText().toString());
                                params.put("ptime",edtTxtPTime.getText().toString());
                                params.put("pprice",txtVwPprice.getText().toString());
                                params.put("pcredit",edtTxtPCredit.getText().toString());
                                params.put("pcvv",edtTxtPCvv.getText().toString());
                                params.put("pexpired",edtTxtPExpired.getText().toString());

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

    public void fnBackVwApp(View view)
    {
        Intent intent = new Intent(this, AppointmentActivity.class);
        startActivity(intent);
    }
}
