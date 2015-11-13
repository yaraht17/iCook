package com.infinity.icook;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.infinity.adapter.UserListAdapter;
import com.infinity.data.ConnectionDetector;
import com.infinity.data.Data;
import com.infinity.data.Var;
import com.infinity.model.UserItem;
import com.infinity.volley.APIConnection;
import com.infinity.volley.VolleyCallback;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class ManagerUser extends Activity implements View.OnClickListener {

    private Typeface font_awesome, font_tony;
    private ListView listUser;
    private UserListAdapter usersAdapter;
    private ArrayList<UserItem> userItems = new ArrayList<>();
    private String token;
    private SharedPreferences sharedPreferences;
    //nav
    private Button barbtn;
    private TextView NavTitle;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_user);
        sharedPreferences = getSharedPreferences(Var.MY_PREFERENCES, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(Var.ACCESS_TOKEN, "");
        font_awesome = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        font_tony = Typeface.createFromAsset(this.getAssets(), "uvf-slimtony.ttf");
        NavTitle = (TextView) findViewById(R.id.NavTitle);
        barbtn = (Button) findViewById(R.id.barbtn);

        NavTitle.setText("Family");
        NavTitle.setTypeface(font_tony);
        barbtn.setText(R.string.icon_back);
        barbtn.setTypeface(font_awesome);
        barbtn.setOnClickListener(this);
        listUser = (ListView) findViewById(R.id.userList);

        usersAdapter = new UserListAdapter(this, -1, userItems);
        listUser.setAdapter(usersAdapter);

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
//        loadUsers();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddUser.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadUsers();
    }

    private void loading() {
        if (Data.usersCache != null) {
            hidePDialog();
            userItems = Data.usersCache;
            usersAdapter = new UserListAdapter(this, -1, userItems);
            listUser.setAdapter(usersAdapter);
        } else {
            loadUsers();
        }
    }
    private void loadUsers() {
        if (ConnectionDetector.isNetworkConnected(getApplicationContext())) {
            try {
                APIConnection.getUser(this, token, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        userItems = APIConnection.parseUser(response);
                        Data.usersCache = userItems;
                        usersAdapter = new UserListAdapter(getApplicationContext(), -1, userItems);
                        listUser.setAdapter(usersAdapter);
                        hidePDialog();
                    }

                    @Override
                    public void onError(VolleyError error) {
                        hidePDialog();
                    }
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            hidePDialog();
            Toast.makeText(this, "Vui lòng kết nối internet!", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.barbtn:
                finish();
                break;
            default:
                break;
        }
    }
}
