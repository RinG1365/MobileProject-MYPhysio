package com.example.myphysio;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.MediaController;
import android.widget.RatingBar;
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

import model.Rehab;

public class RatingActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String strURL = "http://192.168.1.101:8888/myPhysioWebService/rehabService.php";

    Rehab rehab;
    RatingBar rbPain, rbMood,rbPhy,rbService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        sharedPreferences = getApplicationContext().getSharedPreferences("MYPhysio",0);
        editor = sharedPreferences.edit();

        rehab = new Rehab();
        Intent intent = getIntent();
        rehab.setRehabID(intent.getStringExtra("rid"));
        rehab.setRehabPlanID(intent.getStringExtra("rpid"));

        rbPain = findViewById(R.id.rbPain);
        rbMood = findViewById(R.id.rbMood);
        rbPhy = findViewById(R.id.rbPhy);
        rbService = findViewById(R.id.rbService);



    }

    public void fnRateRP(View view)
    {
        /* Get Add rate */
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
                                Toast.makeText(getApplicationContext(),"Rate Successfully",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RatingActivity.this, ViewRehabPlan.class);
                                intent.putExtra("rpid",rehab.getRehabPlanID());
                                startActivity(intent);

                            }
                            else if(respond.equalsIgnoreCase("0"))
                            {
                                Toast.makeText(getApplicationContext(),"Fail to Rate",Toast.LENGTH_SHORT).show();

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
                params.put("selectFn","fnRateRP");
                params.put("rid",rehab.getRehabID());
                params.put("rbPain",String.valueOf(rbPain.getRating()));
                params.put("rbMood", String.valueOf(rbMood.getRating()));
                params.put("rbServices",String.valueOf(rbService.getRating()));
                params.put("rbPhy",String.valueOf(rbPhy.getRating()));

                return params;
            }
        };
        requestQueue.add(stringRequest);
        /*END addRate */
    }

    public void fnBackVRP(View view)
    {
        Intent intent = new Intent(this,ViewRehabPlan.class);
        intent.putExtra("rpid",rehab.getRehabPlanID());
        startActivity(intent);
    }

}
