package com.infinity.icook;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.infinity.data.ConnectionDetector;
import com.infinity.data.Var;
import com.infinity.volley.APIConnection;
import com.infinity.volley.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SqlashScreen extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {
    private SharedPreferences sharedPreferences;
    private static final String TAG = "TienDH";
    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;
    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;
    private SignInButton btnLoginGoogle;
    /* Is there a ConnectionResult resolution in progress? */
    private boolean mIsResolving = false;

    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;
    /* Keys for persisting instance variables in savedInstanceState */
    private static final String KEY_IS_RESOLVING = "is_resolving";
    private static final String KEY_SHOULD_RESOLVE = "should_resolve";
    private String accessToken;
    private ImageView imageLogo;
    private LinearLayout layoutLogo;
    private Typeface font_tony;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlash_screen);
        font_tony = Typeface.createFromAsset(this.getAssets(), "uvf-slimtony.ttf");
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        imageLogo = (ImageView) findViewById(R.id.logo);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .addScope(new Scope(Scopes.EMAIL))
                .build();
        btnLoginGoogle = (SignInButton) findViewById(R.id.sign_in_button);
        btnLoginGoogle.setOnClickListener(this);
        setGooglePlusButtonText(btnLoginGoogle, "Đăng nhập bằng Google");
        layoutLogo = (LinearLayout) findViewById(R.id.logoLayout);
        sharedPreferences = getSharedPreferences(Var.MY_PREFERENCES, Context.MODE_PRIVATE);
        accessToken = sharedPreferences.getString(Var.ACCESS_TOKEN, "");
        Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 2 seconds
                    sleep(1 * 1000);
                    checkLogin();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        background.start();
        if (savedInstanceState != null) {
            mIsResolving = savedInstanceState.getBoolean(KEY_IS_RESOLVING);
            mShouldResolve = savedInstanceState.getBoolean(KEY_SHOULD_RESOLVE);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

        mShouldResolve = false;
        if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            String personName = currentPerson.getDisplayName();
            String personGooglePlusProfile = currentPerson.getUrl();
            String personBirthday = currentPerson.getBirthday();
            Log.d(TAG, "Info: " + personName + "  " + personGooglePlusProfile + " " + personBirthday);
//            if (accessToken.equals("")) {
//                new GetIdTokenTask().execute();
//
//            }
            //checkLogin();
            //get token

        }
        if (accessToken.equals("")) {
            String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
            Log.d(TAG, "Info: " + email);
            APIConnection.loginString(this, email, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    Log.d(TAG, "res: " + response);
                    try {
                        int code = response.getInt("code");
                        if (code == 200 || code == 201) {
                            JSONObject data = response.getJSONObject("data");
                            String email = data.getString(Var.USER_ID);
                            String token = data.getString(Var.ACCESS_TOKEN);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(Var.USER_EMAIL, email);
                            editor.putString(Var.ACCESS_TOKEN, token);
                            editor.commit();
                            showSignedInUI();
                        } else {
                            Toast.makeText(getApplicationContext(), "Xảy ra lỗi, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                            onSignOut();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onSuccess(JSONArray response) {

                }

                @Override
                public void onError(VolleyError error) {

                }
            });
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Lỗi", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sign_in_button) {
            onSignInClicked();
        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);

        if (!mIsResolving && mShouldResolve) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
                    mIsResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Could not resolve ConnectionResult.", e);
                    mIsResolving = false;
                    mGoogleApiClient.connect();
                }
            } else {
                showErrorDialog(connectionResult);
            }
        } else {
//            if (!mGoogleApiClient.isConnected()) {
//                btnLoginGoogle.setVisibility(View.VISIBLE);
//            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode != RESULT_OK) {
                mShouldResolve = false;
            }
            mIsResolving = false;
            mGoogleApiClient.connect();
        }
    }

    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);
            if (v instanceof TextView) {
                TextView mTextView = (TextView) v;
                mTextView.setText(buttonText);
                return;
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_IS_RESOLVING, mIsResolving);
        outState.putBoolean(KEY_SHOULD_RESOLVE, mShouldResolve);
    }

    private void showLogin() {

        layoutLogo.animate().translationY(-300).setDuration(500).setInterpolator(new AccelerateDecelerateInterpolator());
        mHandler.postDelayed(delay, 1 * 1000);
    }

    private void showSignedInUI() {

        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        finish();
    }

    public void checkLogin() {
        if (!accessToken.equals("")) {
            showSignedInUI();
        } else {
            //khong token
            onSignOut();
            showLogin();

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    private void onSignInClicked() {
        if (ConnectionDetector.isNetworkConnected(getApplicationContext())) {
            mShouldResolve = true;
            mGoogleApiClient.connect();
        } else {
            Toast.makeText(getApplicationContext(), "Vui lòng kết nối internet!", Toast.LENGTH_LONG).show();
        }
    }

    private void showErrorDialog(ConnectionResult connectionResult) {
        int errorCode = connectionResult.getErrorCode();

        if (GooglePlayServicesUtil.isUserRecoverableError(errorCode)) {
            GooglePlayServicesUtil.getErrorDialog(errorCode, this, RC_SIGN_IN,
                    new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            mShouldResolve = false;
                        }
                    }).show();
        } else {
            Toast.makeText(this, "Google Play Service xảy ra lỗi", Toast.LENGTH_SHORT).show();
            mShouldResolve = false;
        }
    }

    private void onSignOut() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
        }
    }

    Handler mHandler = new Handler();

    private Runnable delay = new Runnable() {
        public void run() {
            btnLoginGoogle.setVisibility(View.VISIBLE);
        }
    };
}
