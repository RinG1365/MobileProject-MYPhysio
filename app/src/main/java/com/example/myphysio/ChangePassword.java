package com.example.myphysio;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

public class ChangePassword extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String strURL = "http://192.168.1.101:8888/myPhysioWebService/profileService.php";
    Profile myProfile;

    EditText edtTxtOldPass, edtTxtNewPass, edtTxtRptPass;
    String oldPass,checkOldPass,newPass,rptPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        sharedPreferences = getApplicationContext().getSharedPreferences("MYPhysio",0);
        editor = sharedPreferences.edit();
        oldPass = sharedPreferences.getString("password","");
        myProfile = new Profile();
        myProfile.setIcno(sharedPreferences.getString("username",""));

        edtTxtOldPass = findViewById(R.id.edtTxtOldPass);
        edtTxtNewPass = findViewById(R.id.edtTxtNewPass);
        edtTxtRptPass = findViewById(R.id.edtTxtRptPass);


    }

    public void fnChangePassword(View view)
    {
        checkOldPass = edtTxtOldPass.getText().toString();
        newPass = edtTxtNewPass.getText().toString();
        rptPass = edtTxtRptPass.getText().toString();

        if(checkOldPass.equalsIgnoreCase(oldPass))
        {
            if(rptPass.equalsIgnoreCase(newPass))
            {
                /*Change Password */
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
                                        editor.putString("password",newPass);
                                        editor.commit();

                                        Toast.makeText(getApplicationContext(),"Password Successfully changed",Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(ChangePassword.this, ProfileActivity.class);
                                        startActivity(intent);
                                    }
                                    else if (result.equalsIgnoreCase("0"))
                                    {
                                        Toast.makeText(getApplicationContext(),"Fail to change password",Toast.LENGTH_SHORT).show();
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
                        params.put("selectFn","fnChangePassword");
                        params.put("icno",myProfile.getIcno());
                        params.put("password",newPass);


                        return params;
                    }
                };
                requestQueue.add(stringRequest);
                /*END Change Password */
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Repeat Password and New Password is not same ",Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Old Password is wrong",Toast.LENGTH_SHORT).show();

        }
    }

    public void fnBackProfile(View view){
        Intent intent = new Intent(this,ProfileActivity.class);
        startActivity(intent);
    }
}
