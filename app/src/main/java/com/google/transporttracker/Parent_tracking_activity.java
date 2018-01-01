package com.google.transporttracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class Parent_tracking_activity extends AppCompatActivity implements View.OnClickListener {
    public static DatabaseReference mDatabaseReference, busNodeRef;
    private static final int CONFIG_CACHE_EXPIRY = 600;  // 10 minutes.
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    public static String tracking_number;
    public static EditText editText_bus_number;
    Button track_bus;
    LocationPointNode locationPointNode;
    public static boolean map_called;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_tracker_activity);
        map_called = false;
        editText_bus_number = (EditText) findViewById(R.id.editText);
        track_bus = (Button) findViewById(R.id.button4);
        track_bus.setOnClickListener(this);

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);

        authenticate("test.parent1@podar.org", "india@123");
    }

    private void authenticate(String email, String password) {
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        Log.d("<< Authenticating User", "authenticate: " + task.isSuccessful());
                        if (task.isSuccessful()) {
                            fetchRemoteConfig();
                        } else {
            Toast.makeText(getApplicationContext(), "Authentication Failed", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    private void fetchRemoteConfig() {
        long cacheExpiration = CONFIG_CACHE_EXPIRY;
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("<< fetchRemoteConfig", "Remote config fetched");
                        mFirebaseRemoteConfig.activateFetched();
                    }
                });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //map_called = true;
    }

    @Override
    public void onClick(View v) {
        //map_called = false;
        tracking_number = editText_bus_number.getText().toString();

        if(tracking_number.equalsIgnoreCase("")==true){
            Toast.makeText(this, "Please enter bus number to track!", Toast.LENGTH_LONG).show();
        }
        else {
            Log.d("<< Tracking number", tracking_number);

            mDatabaseReference = FirebaseDatabase.getInstance().getReference("raw-locations");

            //checking if database has bus
            mDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //Log.d("<<Bus Count" ,""+dataSnapshot.getChildrenCount());
                    boolean has_bus = dataSnapshot.hasChild(tracking_number);
                    if(has_bus == true){
                     //   Log.d("<< Bus Found"," YES ");

                        busNodeRef = mDatabaseReference.child(tracking_number);
                      //  Log.d("<< busNodeRef", busNodeRef.toString());

                        if(busNodeRef != null && map_called == false){
                            Log.d("<< MapActivity ", "Called");
                            map_called = true;
                            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                            startActivity(intent);
                        }

                    }else{
                        Log.d("<< Bus Found"," NO ");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

    }
}
