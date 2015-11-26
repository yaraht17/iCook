package com.infinity.icook;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.infinity.data.ConnectionDetector;
import com.infinity.data.Progress;
import com.infinity.data.Var;
import com.infinity.model.PersonalItem;
import com.infinity.model.UserItem;
import com.infinity.volley.APIConnection;
import com.infinity.volley.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

public class AddUser extends Activity implements View.OnClickListener {
    private Typeface font_awesome, font_tony;
    private SharedPreferences sharedPreferences;
    private String accessToken;
    //nav
    private Button barbtn;
    private TextView NavTitle;
    private UserItem userExtra = null;
    private EditText txtName, txtBirthdate, txtHeight, txtWeight, txtLike, txtDislike, txtSick;
    private Button btnSave, btnCancel;
    private RadioGroup radioGroup;
    private RadioButton rbtnMale, rbtnFemale;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        txtName = (EditText) findViewById(R.id.txtName);
        txtBirthdate = (EditText) findViewById(R.id.txtBirthdate);
        txtHeight = (EditText) findViewById(R.id.txtHeight);
        txtWeight = (EditText) findViewById(R.id.txtWeight);
        txtLike = (EditText) findViewById(R.id.txtLike);
        txtDislike = (EditText) findViewById(R.id.txtDisLike);
        txtSick = (EditText) findViewById(R.id.txtSickHistory);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        rbtnMale = (RadioButton) findViewById(R.id.rbtnMale);
        rbtnFemale = (RadioButton) findViewById(R.id.rbtnFemale);

        TextView iconName, iconBirthdate, iconHeight, iconWeight;
        iconName = (TextView) findViewById(R.id.iconName);
        iconBirthdate = (TextView) findViewById(R.id.iconBirthdate);
        iconHeight = (TextView) findViewById(R.id.iconHeight);
        iconWeight = (TextView) findViewById(R.id.iconWeight);

        userExtra = (UserItem) getIntent().getSerializableExtra(Var.USER_EXTRA);

        sharedPreferences = getSharedPreferences(Var.MY_PREFERENCES, Context.MODE_PRIVATE);
        accessToken = sharedPreferences.getString(Var.ACCESS_TOKEN, "");
        font_awesome = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        font_tony = Typeface.createFromAsset(this.getAssets(), "uvf-slimtony.ttf");
        NavTitle = (TextView) findViewById(R.id.NavTitle);
        barbtn = (Button) findViewById(R.id.barbtn);
        if (userExtra != null) {
            fillData(userExtra);
            btnSave.setText("Sửa");
            NavTitle.setText("Edit User");
        } else {
            btnSave.setText("Thêm");
            NavTitle.setText("Add User");
        }
        NavTitle.setTypeface(font_tony);
        barbtn.setText(R.string.icon_back);
        barbtn.setTypeface(font_awesome);
        barbtn.setOnClickListener(this);
//                DatePicker birthdate = (DatePicker) findViewById(R.id.dpBirthdate);

        iconName.setTypeface(font_awesome);
        iconBirthdate.setTypeface(font_awesome);
        iconHeight.setTypeface(font_awesome);
        iconWeight.setTypeface(font_awesome);


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkSex = 2;
                radioGroup = (RadioGroup) findViewById(R.id.radio_group);
                int check = radioGroup.getCheckedRadioButtonId();
                if (check == R.id.rbtnMale) {
                    checkSex = 1;
                } else {
                    if (check == R.id.rbtnFemale) {
                        checkSex = 0;
                    }
                }
                if (txtName.getText().equals("") || txtHeight.getText().equals("")
                        || txtBirthdate.getText().equals("") || txtWeight.getText().equals("")
                        || checkSex == 2 || txtLike.getText().equals("")
                        || txtDislike.getText().equals("")
                        || txtSick.getText().equals("")) {
                    Toast.makeText(getApplicationContext(), "Vui lòng điền đầy đủ thông tin",
                            Toast.LENGTH_SHORT).show();
                } else {
                    String date = txtBirthdate.getText().toString();
                    String dateString = "";
                    try {
                        dateString = Progress.formatDate(date, "dd/mm/yyyy", "yyyy-mm-dd");
                        Log.d("TienDH", "date :" + dateString);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    PersonalItem personal = new PersonalItem(txtLike.getText().toString(), txtDislike.getText().toString(),
                            txtSick.getText().toString());
                    if (userExtra != null) {
                        UserItem user = new UserItem(userExtra.getId(), txtName.getText().toString(), dateString,
                                Double.parseDouble(txtHeight.getText().toString()),
                                Double.parseDouble(txtWeight.getText().toString()),
                                checkSex, personal);
                        updateUser(user);

                    } else {
                        UserItem user = new UserItem(txtName.getText().toString(), dateString,
                                Double.parseDouble(txtHeight.getText().toString()),
                                Double.parseDouble(txtWeight.getText().toString()),
                                checkSex, personal);
                        addUser(user);
                    }
                }
            }
        });

    }

    private void updateUser(UserItem user) {
        if (ConnectionDetector.isNetworkConnected(getApplicationContext())) {
            try {
                APIConnection.updateUser(getApplicationContext(), user, accessToken, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        //check
                        Log.d("TienDH", "update user: " + response);
                        try {
                            int code = response.getInt("code");
                            if (code == 201 || code == 200) {
                                Toast.makeText(getApplicationContext(), "Sửa thành công",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
    }

    private void addUser(UserItem user) {
        if (ConnectionDetector.isNetworkConnected(getApplicationContext())) {
            try {
                APIConnection.addUser(getApplicationContext(), user, accessToken, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        //check
                        Log.d("TienDH", "add user: " + response);
                        try {
                            int code = response.getInt("code");
                            if (code == 201 || code == 200) {
                                Toast.makeText(getApplicationContext(), "Thêm thành công",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
    }

    private void fillData(UserItem user) {
        txtName.setText(user.getName());
        String date = "";
        try {
            date = Progress.formatDate(user.getBirthdate(), "yyyy-mm-dd", "dd/mm/yyyy");
        } catch (ParseException e) {
            date = "01/01/2000";
        }
        if (user.getSex() == 1) {
            rbtnMale.setChecked(true);
        } else {
            rbtnFemale.setChecked(true);
        }
        txtBirthdate.setText(date);
        txtHeight.setText(user.getHeight() + "");
        txtWeight.setText(user.getWeight() + "");
        txtLike.setText(user.getPersonal().getLike());
        txtDislike.setText(user.getPersonal().getDislike());
        txtSick.setText(user.getPersonal().getSick());
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
