package com.example.myphysio;

import android.content.Context;
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

public class MainMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String txtUsername;
    String strURL = "http://192.168.1.101:8888/myPhysioWebService/dashboardService.php";

    TextView txtVwR1,txtVwR2,txtVwR3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tbMainMenu);
        setSupportActionBar(toolbar);

        sharedPreferences = getApplicationContext().getSharedPreferences("MYPhysio",0);
        editor = sharedPreferences.edit();

        txtUsername=sharedPreferences.getString("pat_name","");
        Toast.makeText(getApplicationContext(),"Welcome Back "+txtUsername,Toast.LENGTH_SHORT).show();

        txtVwR1 = findViewById(R.id.txtVwR1);
        txtVwR2 = findViewById(R.id.txtVwR2);
        txtVwR3 = findViewById(R.id.txtVwR3);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabMainMenu);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_mm);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_mm);
        navigationView.setNavigationItemSelectedListener(this);

        /* Update Profile */
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, strURL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response)
                    {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String r1 = jsonObject.optString("r1");
                            String r2 = jsonObject.optString("r2");
                            String r3 = jsonObject.optString("r3");

                            txtVwR1.setText(r1 + " assigned Rehab Plan.\n Fast Upload your exercise video.");
                            txtVwR2.setText(r2+ " coming appointment. \nPlease check the detail");
                            txtVwR3.setText(r3 +" UNPAID appointment. \nPlease make your payment.");




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
                params.put("selectFn","fnGetReminder");
                params.put("icno",sharedPreferences.getString("username",""));


                return params;
            }
        };
        requestQueue.add(stringRequest);
        /*END Update Profile */


    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_mm);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = null;

        if (id == R.id.nav_mm)
        {
                return true;
        }
        else if (id == R.id.nav_profile)
        {
            intent = new Intent(this,ProfileActivity.class);

        } else if (id == R.id.nav_healthRecord)
        {
            intent = new Intent(this,HealthRecordActivity.class);
        } else if (id == R.id.nav_appointment)
        {
            intent = new Intent(this,AppointmentActivity.class);

        } else if (id == R.id.nav_exerciseVideo)
        {
            intent = new Intent(this,ExerciseVideo.class);

        }else if (id == R.id.nav_logout)
        {
            intent = new Intent(this,LoginActivity.class);
            editor.putString("username","");
            editor.putString("password","");
            editor.putString("pat_name","");
            editor.commit();
        }

        startActivity(intent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_mm);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
