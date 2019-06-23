package com.example.myphysio;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import model.Rehab;
import model.Upload;

public class AddExerciseVideo extends AppCompatActivity implements View.OnClickListener{

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String strURL = "http://192.168.1.101:8888/myPhysioWebService/rehabService.php";

    private Button buttonChoose;
    private Button buttonUpload;
    private TextView textView;
    private TextView textViewResponse;

    private static final int SELECT_VIDEO = 3;

    private String selectedPath;
    Rehab rehab;

    EditText edtTxtUEVDate;
    Button btnAddUEV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise_video);
        sharedPreferences = getApplicationContext().getSharedPreferences("MYPhysio",0);
        editor = sharedPreferences.edit();

        rehab = new Rehab();
        Intent intent =  getIntent();
        rehab.setRehabPlanID(intent.getStringExtra("rpid"));

        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);

        edtTxtUEVDate = findViewById(R.id.edtTxtUEVDate);
        btnAddUEV = findViewById(R.id.btnAddUEV);

        edtTxtUEVDate.setVisibility(View.INVISIBLE);
        btnAddUEV.setVisibility(View.INVISIBLE);

        textView = (TextView) findViewById(R.id.textView);
        textViewResponse = (TextView) findViewById(R.id.textViewResponse);

        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);

        ActivityCompat.requestPermissions(AddExerciseVideo.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);


    }

    private void chooseVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a Video "), SELECT_VIDEO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_VIDEO) {
                System.out.println("SELECT_VIDEO");
                Uri selectedImageUri = data.getData();
                selectedPath = getPath(selectedImageUri);
                textView.setText(selectedPath);
            }
        }
    }

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        cursor.close();

        return path;
    }

    private void uploadVideo() {
        class UploadVideo extends AsyncTask<Void, Void, String> {

            ProgressDialog uploading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                uploading = ProgressDialog.show(AddExerciseVideo.this, "Uploading File", "Please wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                uploading.dismiss();
                rehab.setEvLink(s);
                edtTxtUEVDate.setVisibility(View.VISIBLE);
                btnAddUEV.setVisibility(View.VISIBLE);
                textViewResponse.setText("Upload Sucessfully.Please insert the date and press the save button");
                textViewResponse.setMovementMethod(LinkMovementMethod.getInstance());
            }

            @Override
            protected String doInBackground(Void... params) {
                Upload u = new Upload();
                String msg = u.uploadVideo(selectedPath);
                return msg;
            }
        }
        UploadVideo uv = new UploadVideo();
        uv.execute();
    }

    @Override
    public void onClick(View v) {
        if (v == buttonChoose) {
            chooseVideo();
        }
        if (v == buttonUpload) {
            uploadVideo();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted and now can proceed
                    chooseVideo(); //a sample method called

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(AddExerciseVideo.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            // add other cases for more permissions
        }
    }

    public void fnAddUEV(View view)
    {
        /* Get Add EV */
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
                                Toast.makeText(getApplicationContext(),"Add Successfully",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddExerciseVideo.this, ViewRehabPlan.class);
                                intent.putExtra("rpid",rehab.getRehabPlanID());
                                startActivity(intent);

                            }
                            else if(respond.equalsIgnoreCase("0"))
                            {
                                Toast.makeText(getApplicationContext(),"Fail to Add",Toast.LENGTH_SHORT).show();

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
                params.put("selectFn","fnAddEV");
                params.put("rpid",rehab.getRehabPlanID());
                params.put("link",rehab.getEvLink());
                params.put("edate",edtTxtUEVDate.getText().toString());

                return params;
            }
        };
        requestQueue.add(stringRequest);
        /*END add EV */

    }

    public void fnBackUVRP(View view)
    {
        Intent intent = new Intent(this,ViewRehabPlan.class);
        intent.putExtra("rpid",rehab.getRehabPlanID());
        startActivity(intent);
    }

}




