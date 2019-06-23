package com.example.myphysio;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import model.CustomAdapterEVList;
import model.Profile;
import model.Rehab;

public class ViewRehabPlan extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String strURL = "http://192.168.1.101:8888/myPhysioWebService/rehabService.php";

    Profile myProfile;
    Rehab rehab;

    TextView txtVwRTitle, txtVwDescription, txtVwRPhy,txtVwETitle,txtVwEVDate, txtVwEvFeed;
    VideoView videoViewRehab, videoVideoEV;
    Button btnStar,btnDelete,btnAdd;
    WebView webViewYou;

     boolean isContinuously = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_rehab_plan);
        sharedPreferences = getApplicationContext().getSharedPreferences("MYPhysio",0);
        editor = sharedPreferences.edit();

        myProfile = new Profile();
        myProfile.setIcno(sharedPreferences.getString("username",""));

        rehab = new Rehab();
        Intent intent = getIntent();
        rehab.setRehabPlanID(intent.getStringExtra("rpid"));

        txtVwRTitle = findViewById(R.id.txtVwRTitle);
        txtVwDescription  = findViewById(R.id.txtVwDescription);
        txtVwRPhy = findViewById(R.id.txtVwRphy);
        txtVwETitle = findViewById(R.id.txtVwETitle);
        txtVwEVDate = findViewById(R.id.txtVwEVDate);
        txtVwEvFeed = findViewById(R.id.txtVwEVFeed1);
        videoViewRehab = findViewById(R.id.videoViewRehab);
        videoVideoEV = findViewById(R.id.videoViewEV);
        btnStar = findViewById(R.id.btnStar);
        btnDelete = findViewById(R.id.btnDeleteEV);
        btnAdd = findViewById(R.id.btnAddEV);
        webViewYou = findViewById(R.id.webViewYou);



        txtVwRTitle.setText("Rehab Plan "+ rehab.getRehabPlanID());

        /* Get RP */
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, strURL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response)
                    {
                        try {

                           JSONObject jObj = new JSONObject(response);
                            rehab.setRehabID(jObj.optString("RID"));
                            rehab.setRehabDescription(jObj.optString("DESCRIPTION"));
                            rehab.setRehabstaffid(jObj.optString("STAFFID"));
                            rehab.setRehabPhyName(jObj.optString("PHY_NAME"));
                            rehab.setRehabVideo(jObj.optString("RVIDEO"));
                            rehab.setEvLink(jObj.optString("LINK"));
                            rehab.setEvFeedback(jObj.optString("FEEDBACK"));
                            rehab.setEvDate(jObj.optString("EDATE"));

                            txtVwDescription.setText(rehab.getRehabID() + " - " +rehab.getRehabDescription());
                            txtVwRPhy.setText(rehab.getRehabstaffid() + " - " + rehab.getRehabPhyName());


                            if(rehab.getRehabVideo().contains("<iframe"))
                            {
                                videoViewRehab.setVisibility(View.INVISIBLE);
                                String frameVideo = "<html><body>" + rehab.getRehabVideo() + "</body></head>";
                                webViewYou.setWebViewClient(new WebViewClient() {
                                    @Override
                                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                        return false;
                                    }
                                });
                                WebSettings webSettings = webViewYou.getSettings();
                                webSettings.setJavaScriptEnabled(true);
                                webViewYou.loadData(frameVideo, "text/html", "utf-8");
                            }
                            else {
                                webViewYou.setVisibility(View.INVISIBLE);
                                videoViewRehab.setVideoPath(rehab.getRehabVideo());
                                MediaController mediaController = new
                                        MediaController(ViewRehabPlan.this);
                                mediaController.setAnchorView(videoViewRehab);
                                videoViewRehab.setMediaController(mediaController);
                                videoViewRehab.start();
                            }

                            if(rehab.getEvLink().equalsIgnoreCase("null"))
                            {
                                txtVwETitle.setText("No Exercise Video Uploaded.\nUpload 1 Now");
                                videoVideoEV.setVisibility(View.INVISIBLE);
                                txtVwEVDate.setVisibility(View.INVISIBLE);
                                txtVwEvFeed.setVisibility(View.INVISIBLE);
                                btnStar.setVisibility(View.INVISIBLE);
                                btnDelete.setVisibility(View.INVISIBLE);

                            }
                            else
                                {
                                    txtVwETitle.setVisibility(View.INVISIBLE);
                                txtVwEVDate.setText(rehab.getEvDate());
                                txtVwEvFeed.setText("Feedback : "+rehab.getEvFeedback());
                                btnAdd.setVisibility(View.INVISIBLE);
                                videoVideoEV.setVideoPath(rehab.getEvLink());
                                    MediaController mediaController = new
                                            MediaController(ViewRehabPlan.this);
                                    mediaController.setAnchorView(videoVideoEV);
                                    videoVideoEV.setMediaController(mediaController);

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
                params.put("selectFn","fnSelectRP");
                params.put("rpid",rehab.getRehabPlanID());

                return params;
            }
        };
        requestQueue.add(stringRequest);
        /*END Get RP */



    }

    public void fnAddEV(View view)
    {
        Intent intent = new Intent(this,AddExerciseVideo.class);
        intent.putExtra("rpid",rehab.getRehabPlanID());
        startActivity(intent);
    }

    public void fnDeleteEV(View view){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Are you sure you want to delete?");
        alertBuilder.setPositiveButton(getString(R.string.btnPositive), new
                DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        /* Delete App */
                        RequestQueue requestQueue = Volley.newRequestQueue(ViewRehabPlan.this);
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
                                                Intent intent = new Intent(ViewRehabPlan.this, ExerciseVideo.class);
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
                                params.put("selectFn","fnDeleteEV");
                                params.put("rpid",rehab.getRehabPlanID());

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

    public void fnBackRehab(View view){
        Intent intent = new Intent(this,ExerciseVideo.class);
        startActivity(intent);
    }

    public void fnRating(View view){
        Intent intent = new Intent(this,RatingActivity.class);
        intent.putExtra("rid", rehab.getRehabID());
        intent.putExtra("rpid",rehab.getRehabPlanID());
        startActivity(intent);
    }
}
