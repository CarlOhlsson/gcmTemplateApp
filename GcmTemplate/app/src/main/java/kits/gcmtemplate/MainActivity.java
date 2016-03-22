package kits.gcmtemplate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import kits.gcmtemplate.gcmServices.QuckstartPreferences;
import kits.gcmtemplate.gcmServices.RegistrationIntentService;

public class MainActivity extends AppCompatActivity {
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private BroadcastReceiver mMessageBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Will show a simple Snackbar when the registration token has been received.
        // Can be completely removed if you want to.
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, Intent intent) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences.getBoolean(QuckstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Token successfully registered", Snackbar.LENGTH_LONG);
                    Log.v("Token", "Successful");
                    snackbar.show();
                } else {
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Token could not be registered", Snackbar.LENGTH_LONG);
                    Log.e("Token", "Failed");
                    snackbar.show();
                }
            }
        };

        // Making sure the received message end up in the UI.
        mMessageBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String message = intent.getExtras().getString("Message");

                TextView textMessageView = (TextView) findViewById(R.id.textMessage);
                textMessageView.setText(message);
            }
        };


        // Start listening for messages.
        if (checkPlayServices()) {
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(QuckstartPreferences.REGISTRATION_COMPLETE));
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageBroadcastReceiver, new IntentFilter(QuckstartPreferences.MESSAGE_RECEIVED));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageBroadcastReceiver);
        super.onPause();
    }

    /**
     * Will check if Google Play Services is available.
     * Can be completely removed if you do not want to make this check.
     *
     * @return true if supported
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, 9000).show();
            } else {
                Log.i("MainActivity", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
