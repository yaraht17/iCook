package com.infinity.icook;

import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.infinity.adapter.UserListAdapter;
import com.infinity.model.UserItem;

import java.util.ArrayList;

public class ManagerUser extends AppCompatActivity implements View.OnClickListener {

    private Typeface font_awesome, font_tony;
    private ListView listUser;
    private UserListAdapter usersAdapter;
    private ArrayList<UserItem> userItems = new ArrayList<>();

    //nav
    private Button barbtn;
    private TextView NavTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_user);
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


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(ManagerUser.this);
                dialog.setContentView(R.layout.dialog_add_user);
                // Set dialog title
                dialog.setTitle("Thêm thành viên");
                TextView iconName, iconBirthdate, iconHeight, iconWeight;
                iconName = (TextView) dialog.findViewById(R.id.iconName);
                iconBirthdate = (TextView) dialog.findViewById(R.id.iconBirthdate);
                iconHeight = (TextView) dialog.findViewById(R.id.iconHeight);
                iconWeight = (TextView) dialog.findViewById(R.id.iconWeight);
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
                        UserItem user = new UserItem(txtName.getText().toString(), txtBirthdate.getText().toString(),
                                Double.parseDouble(txtHeight.getText().toString()), Double.parseDouble(txtWeight.getText().toString()));
                        usersAdapter.add(user);
                        dialog.dismiss();

                    }
                });
            }
        });
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
