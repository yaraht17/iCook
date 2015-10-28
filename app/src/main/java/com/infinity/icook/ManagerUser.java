package com.infinity.icook;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ManagerUser extends AppCompatActivity implements View.OnClickListener {

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

        if (Data.usersCache != null) {
            hidePDialog();
            userItems = Data.usersCache;
            usersAdapter = new UserListAdapter(this, -1, userItems);
            listUser.setAdapter(usersAdapter);
        } else {
            loadUsers();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(ManagerUser.this);
                dialog.setContentView(R.layout.dialog_add_user);
                // Set dialog title
                dialog.setTitle("Thêm thành viên");
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);

                TextView iconName, iconBirthdate, iconHeight, iconWeight;
                iconName = (TextView) dialog.findViewById(R.id.iconName);
                iconBirthdate = (TextView) dialog.findViewById(R.id.iconBirthdate);
                iconHeight = (TextView) dialog.findViewById(R.id.iconHeight);
                iconWeight = (TextView) dialog.findViewById(R.id.iconWeight);
//                DatePicker birthdate = (DatePicker) dialog.findViewById(R.id.dpBirthdate);

                final EditText txtName, txtBirthdate, txtHeight, txtWeight;
                txtName = (EditText) dialog.findViewById(R.id.txtName);
                txtBirthdate = (EditText) dialog.findViewById(R.id.txtBirthdate);
                txtHeight = (EditText) dialog.findViewById(R.id.txtHeight);
                txtWeight = (EditText) dialog.findViewById(R.id.txtWeight);

                iconName.setTypeface(font_awesome);
                iconBirthdate.setTypeface(font_awesome);
                iconHeight.setTypeface(font_awesome);
                iconWeight.setTypeface(font_awesome);
                dialog.show();
                // kiem tra xem la nam hay nu


                Button btnSave, btnCancel;
                btnSave = (Button) dialog.findViewById(R.id.btnSave);
                btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int checkSex = 2;
                        RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radio_group);
                        int check = radioGroup.getCheckedRadioButtonId();
                        if (check == R.id.rbtnMale) {
                            checkSex = 1;
                        } else {
                            if (check == R.id.rbtnFemale) {
                                checkSex = 0;
                            }
                        }
                        if (txtName.getText().equals("") || txtHeight.getText().equals("")
                                || txtBirthdate.getText().equals("")
                                || txtWeight.getText().equals("")
                                || checkSex == 2) {
                            Toast.makeText(getApplicationContext(), "Vui lòng điền đầy đủ thông tin",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            String date = txtBirthdate.getText().toString();
                            String dateString = "";
                            try {
                                dateString = formatDate(date, "dd/mm/yyyy", "yyyy:mm:dd");
                                Log.d("TienDH", "date :" + dateString);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            UserItem user = new UserItem(txtName.getText().toString(), dateString,
                                    Double.parseDouble(txtHeight.getText().toString()),
                                    Double.parseDouble(txtWeight.getText().toString()),
                                    checkSex);
                            usersAdapter.add(user);
                            Log.d("TienDH", "Add user - token :" + token);
                            if (ConnectionDetector.isNetworkConnected(getApplicationContext())) {
                                try {
                                    APIConnection.addUser(getApplicationContext(), user, token, new VolleyCallback() {
                                        @Override
                                        public void onSuccess(JSONObject response) {
                                            //check
                                            Log.d("TienDH", "add user: " + response);
                                            try {
                                                int code = response.getInt("code");
                                                if (code == 201) {
                                                    Toast.makeText(getApplicationContext(), "Thêm thành công",
                                                            Toast.LENGTH_SHORT).show();
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

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Vui lòng kết nối internet!", Toast.LENGTH_LONG).show();
                            }
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
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
                    public void onSuccess(JSONArray response) {
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

    public String formatDate(String date, String initDateFormat, String endDateFormat) throws ParseException {

        Date initDate = new SimpleDateFormat(initDateFormat).parse(date);
        SimpleDateFormat formatter = new SimpleDateFormat(endDateFormat);
        String parsedDate = formatter.format(initDate);
        return parsedDate;
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
