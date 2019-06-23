package com.example.myphysio;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

import model.Profile;

public class ProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String strURL = "http://192.168.1.101:8888/myPhysioWebService/profileService.php";

    Profile myProfile;
    TextView txtVwIC, txtVwLL, txtVwPhy, txtVwOcc, txtVwHA, txtVwEm, txtVwNo, txtVwStatus, txtVwAge, txtVwGender,txtVwName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        sharedPreferences = getApplicationContext().getSharedPreferences("MYPhysio",0);
        editor = sharedPreferences.edit();



        myProfile = new Profile();
        myProfile.setIcno(sharedPreferences.getString("username",""));
        myProfile.setPatName(sharedPreferences.getString("pat_name",""));

        txtVwIC = findViewById(R.id.txtVwIC);
        txtVwName = findViewById(R.id.txtVwName);
        txtVwLL = findViewById(R.id.txtVwLL);
        txtVwPhy = findViewById(R.id.txtVwPhy);
        txtVwOcc = findViewById(R.id.txtVwOcc);
        txtVwHA = findViewById(R.id.txtVwHA);
        txtVwEm = findViewById(R.id.txtVwEm);
        txtVwNo = findViewById(R.id.txtVwNo);
        txtVwStatus = findViewById(R.id.txtVwStatus);
        txtVwAge = findViewById(R.id.txtVwAge);
        txtVwGender = findViewById(R.id.txtVwGender);


        txtVwIC.setText(myProfile.getIcno());
        txtVwName.setText(myProfile.getPatName());


        Toolbar toolbar = (Toolbar) findViewById(R.id.tbProfile);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabProfile);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Use format with "tel:" and phoneNumber created is
                // stored in u.
                Uri u = Uri.parse("tel:999");

                // Create the intent and set the data for the
                // intent as the phone number.
                Intent i = new Intent(Intent.ACTION_DIAL, u);

                try
                {
                    // Launch the Phone app's dialer with a phone
                    // number to dial a call.
                    startActivity(i);
                }
                catch (SecurityException s)
                {
                    // show() method display the toast with
                    // exception message.
                    Toast.makeText(getApplicationContext(),s.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });



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
                            myProfile.setLastlogin(jsonObject.optString("LAST_LOG_IN"));
                            myProfile.setStatus(jsonObject.optString("PAT_STATUS"));
                            myProfile.setPhyName(jsonObject.optString("PHY_NAME"));

                            txtVwLL.setText(myProfile.getLastlogin());
                            txtVwPhy.setText(myProfile.getPhyName());
                            txtVwOcc.setText(myProfile.getOcc());
                            txtVwHA.setText(myProfile.getAddress());
                            txtVwEm.setText(myProfile.getEmail());
                            txtVwNo.setText(myProfile.getPhoneNo());
                            txtVwStatus.setText(myProfile.getStatus());
                            txtVwAge.setText(myProfile.getAge());
                            txtVwGender.setText(myProfile.getGender());



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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_profile);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_profile);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_profile);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent = null;
        //noinspection SimplifiableIfStatement
        if (id == R.id.act_editProfile) {
            intent = new Intent(this,EditProfile.class);
        }
        else if (id == R.id.act_changePassword) {
            intent = new Intent(this,ChangePassword.class);
        }

        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent =null;

        if (id == R.id.nav_mm)
        {
            intent = new Intent(this,MainMenu.class);
        }
        else if (id == R.id.nav_profile)
        {
            return true;

        } else if (id == R.id.nav_healthRecord)
        {
            intent = new Intent(this,HealthRecordActivity.class);

        } else if (id == R.id.nav_appointment)
        {
            intent = new Intent(this,AppointmentActivity.class);
        } else if (id == R.id.nav_exerciseVideo)
        {
            intent = new Intent(this,ExerciseVideo.class);

        }
        else if (id == R.id.nav_logout)
        {
            intent = new Intent(this,LoginActivity.class);
            editor.putString("username","");
            editor.putString("password","");
            editor.putString("pat_name","");
            editor.commit();
        }

        startActivity(intent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_profile);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
