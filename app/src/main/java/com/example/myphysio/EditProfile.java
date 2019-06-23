package com.example.myphysio;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
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

import model.Profile;

public class EditProfile extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String strURL = "http://192.168.1.101:8888/myPhysioWebService/profileService.php";
    Profile myProfile;

    EditText edtTxtName, edtTxtAge,edtTxtOcc, edtTxtAdd, edtTxtNo, edtTxtEm;
    Spinner edtTxtGen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        sharedPreferences = getApplicationContext().getSharedPreferences("MYPhysio",0);
        editor = sharedPreferences.edit();

        myProfile = new Profile();
        myProfile.setIcno(sharedPreferences.getString("username",""));
        myProfile.setPatName(sharedPreferences.getString("pat_name",""));

        edtTxtName = findViewById(R.id.edtTxtName);
        edtTxtAge = findViewById(R.id.edtTxtAge);
        edtTxtOcc = findViewById(R.id.edtTxtOcc);
        edtTxtAdd = findViewById(R.id.edtTxtAdd);
        edtTxtNo = findViewById(R.id.edtTxtNo);
        edtTxtEm = findViewById(R.id.edtTxtEm);
        edtTxtGen = findViewById(R.id.edtTxtGen);

        edtTxtName.setText(myProfile.getPatName());
        /* Get Profile */
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, strURL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response)
                    {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            myProfile.setAge(jsonObject.optString("AGE"));
                            myProfile.setGender(jsonObject.optString("GENDER"));
                            myProfile.setOcc(jsonObject.optString("OCCUPATION"));
                            myProfile.setAddress(jsonObject.optString("ADDRESS"));
                            myProfile.setPhoneNo(jsonObject.optString("PHONENO"));
                            myProfile.setEmail(jsonObject.optString("EMAIL"));
                            myProfile.setPhyName(jsonObject.optString("PHY_NAME"));

                            edtTxtOcc.setText(myProfile.getOcc());
                            edtTxtAdd.setText(myProfile.getAddress());
                            edtTxtEm.setText(myProfile.getEmail());
                            edtTxtNo.setText(myProfile.getPhoneNo());
                            edtTxtAge.setText(myProfile.getAge());

                            if(myProfile.getGender().equalsIgnoreCase("M"))
                            {
                                edtTxtGen.setSelection(0);
                            }
                            else
                            {
                                edtTxtGen.setSelection(1);
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
                params.put("selectFn","fnGetProfile");
                params.put("icno",myProfile.getIcno());

                return params;
            }
        };
        requestQueue.add(stringRequest);
        /*END Get Profile */

    }


    public void fnEditProfile(View view) {

        myProfile.setAge(edtTxtAge.getText().toString());
        myProfile.setGender(edtTxtGen.getSelectedItem().toString());
        myProfile.setOcc(edtTxtOcc.getText().toString());
        myProfile.setAddress(edtTxtAdd.getText().toString());
        myProfile.setPhoneNo(edtTxtNo.getText().toString());
        myProfile.setEmail(edtTxtEm.getText().toString());
        myProfile.setPatName(edtTxtName.getText().toString());



        /* Update Profile */
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, strURL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response)
                    {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String result = jsonObject.optString("respond");

                            if(result.equalsIgnoreCase("1"))
                            {
                                editor.putString("pat_name",myProfile.getPatName());
                                editor.commit();

                                Intent intent = new Intent(EditProfile.this,ProfileActivity.class);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(),"Edit Successfully",Toast.LENGTH_SHORT).show();

                            }
                            else if(result.equalsIgnoreCase("0"))
                            {
                                Toast.makeText(getApplicationContext(),"Fail to Update.Please Try Again",Toast.LENGTH_SHORT).show();
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
                params.put("selectFn","fnUpdateProfile");
                params.put("icno",myProfile.getIcno());
                params.put("patName",myProfile.getPatName());
                params.put("gender",myProfile.getGender());
                params.put("age",myProfile.getAge());
                params.put("occupation",myProfile.getOcc());
                params.put("address",myProfile.getAddress());
                params.put("email",myProfile.getEmail());
                params.put("phoneNo",myProfile.getPhoneNo());

                return params;
            }
        };
        requestQueue.add(stringRequest);
        /*END Update Profile */



    }


    public void fnBackProfile(View view)
    {
        Intent intent = new Intent(this,ProfileActivity.class);
        startActivity(intent);
    }

}