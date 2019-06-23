package com.example.myphysio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.Appointment;
import model.CustomAdapterAppList;
import model.CustomAdapterHRList;
import model.HealthRecord;
import model.Profile;

public class AppointmentActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String strURL = "http://192.168.1.101:8888/myPhysioWebService/appointmentService.php";

    Profile myProfile;

    RecyclerView recyListApp;
    ArrayList<Appointment> appList;
    Appointment app;
    CustomAdapterAppList customAdapterAppList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        sharedPreferences = getApplicationContext().getSharedPreferences("MYPhysio",0);
        editor = sharedPreferences.edit();
        myProfile = new Profile();

        myProfile.setIcno(sharedPreferences.getString("username",""));

        recyListApp = findViewById(R.id.recyListApp);
        appList = new ArrayList<>();
        app = new Appointment();


        Toolbar toolbar = (Toolbar) findViewById(R.id.tbApp);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabApp);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_app);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_app);
        navigationView.setNavigationItemSelectedListener(this);

        /* Get App */
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, strURL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response)
                    {
                        try {

                            JSONArray jsnArr = new JSONArray(response);

                            for(int i=0;i<jsnArr.length();i++)
                            {
                                JSONObject jObj = jsnArr.getJSONObject(i);
                                app = new Appointment();
                                app.setBid(jObj.optString("BOOKING_ID"));
                                app.setBdate(jObj.optString("BOOKING_DATE"));
                                app.setBstatus(jObj.optString("BOOKING_STATUS"));
                                appList.add(app);

                            }


                            customAdapterAppList = new CustomAdapterAppList(appList);
                            recyListApp.setLayoutManager(new LinearLayoutManager(AppointmentActivity.this));
                            recyListApp.setAdapter(customAdapterAppList);
                            customAdapterAppList.notifyDataSetChanged();


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
                params.put("selectFn","fnSelectApp");
                params.put("icno",myProfile.getIcno());

                return params;
            }
        };
        requestQueue.add(stringRequest);
        /*END Get App */


        /**
         * RecyclerView: Implementing single item click and long press (Part-II)
         * */
        recyListApp.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyListApp, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //Values are passing to activity & to fragment as well

                Appointment app = appList.get(position);


                String bid = app.getBid();

                Intent intent = new Intent(AppointmentActivity.this, ViewAppointment.class);
                intent.putExtra("bid", bid);
                startActivity(intent);



            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(AppointmentActivity.this, "Long press on position :" + position,
                        Toast.LENGTH_LONG).show();
            }
        }) {
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_app);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.appointment, menu);
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
        if (id == R.id.add_app) {
            intent = new Intent(this,AddAppointment.class);
            startActivity(intent);
        }

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
            intent = new Intent(this,ProfileActivity.class);

        } else if (id == R.id.nav_healthRecord)
        {
            intent = new Intent(this,HealthRecordActivity.class);

        } else if (id == R.id.nav_appointment)
        {
            return true;
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_app);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * RecyclerView: Implementing single item click and long press (Part-II)
     *
     * - creating an Interface for single tap and long press
     * - Parameters are its respective view and its position
     * */

    public static interface ClickListener{
        public void onClick(View view,int position);
        public void onLongClick(View view,int position);
    }

    /**
     * RecyclerView: Implementing single item click and long press (Part-II)
     *
     * - creating an innerclass implementing RevyvlerView.OnItemTouchListener
     * - Pass clickListener interface as parameter
     * */

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener){

            this.clicklistener=clicklistener;
            gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child=recycleView.findChildViewUnder(e.getX(),e.getY());
                    if(child!=null && clicklistener!=null){
                        clicklistener.onLongClick(child,recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child=rv.findChildViewUnder(e.getX(),e.getY());
            if(child!=null && clicklistener!=null && gestureDetector.onTouchEvent(e)){
                clicklistener.onClick(child,rv.getChildAdapterPosition(child));

            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }


}
