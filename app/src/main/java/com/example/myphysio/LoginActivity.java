package com.example.myphysio;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText edtTxtIC,edtTxtPass;
    Button btnLogin,btnMic,btnMic2;
    TextView txtVwMic;
    ProgressBar loginPB;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private final int REQ_CODE_SPEECH_INPUT2 = 200;
    String icno, pass;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String strURL = "http://192.168.1.101:8888/myPhysioWebService/loginService.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        sharedPreferences = getApplicationContext().getSharedPreferences("MYPhysio",0);
        editor = sharedPreferences.edit();

        edtTxtIC = findViewById(R.id.edtTxtIC);
        edtTxtPass = findViewById(R.id.edtTxtPass);
        btnLogin = findViewById(R.id.btnLogin);
        btnMic = findViewById(R.id.btnMic);
        btnMic2 = findViewById(R.id.btnMic2);
        txtVwMic = findViewById(R.id.txtVwMic);
        loginPB = findViewById(R.id.loginPB);
        loginPB.setVisibility(View.INVISIBLE);

        icno = sharedPreferences.getString("username","");

        if(!(icno.equalsIgnoreCase("")))
        {
            Intent intent = new Intent(LoginActivity.this, MainMenu.class);
            startActivity(intent);
        }

        btnMic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput(REQ_CODE_SPEECH_INPUT);
            }
        });


        btnMic2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput(REQ_CODE_SPEECH_INPUT2);
            }
        });


    }

    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput(int code) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, code);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    edtTxtIC.setText(result.get(0));
                }
                break;
            }

            case REQ_CODE_SPEECH_INPUT2: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    edtTxtPass.setText(result.get(0));
                }
                break;
            }

        }
    }


    public void fnLogin(View view)
    {
        loginPB.setVisibility(View.VISIBLE);
     icno = edtTxtIC.getText().toString().replaceAll("\\s","");
     pass = edtTxtPass.getText().toString().replaceAll("\\s","");


        /*Login */
     RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, strURL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response)
                    {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String result = jsonObject.optString("respond");
                            String numRow = jsonObject.optString("numRow");

                            if(result.equalsIgnoreCase("1"))
                            {
                                loginPB.setVisibility(View.INVISIBLE);
                                editor.putString("username",icno);
                                editor.putString("password",pass);
                                editor.putString("pat_name",jsonObject.optString("pat_name"));
                                editor.commit();

                                Intent intent = new Intent(LoginActivity.this, MainMenu.class);
                                startActivity(intent);
                            }
                            else if (result.equalsIgnoreCase("0"))
                            {
                                loginPB.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(),"Invalid Username & Password" + numRow,Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception ee) {
                            Toast.makeText(getApplicationContext(),ee.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }



                },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error){
                    loginPB.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();

            }

        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("selectFn","fnLogin");
                params.put("icno",icno);
                params.put("password",pass);


                return params;
            }
        };
        requestQueue.add(stringRequest);
        /*END Login */


    }






}