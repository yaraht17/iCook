package com.infinity.icook;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class ManagerUser extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

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

        NavTitle.setText("Gia đình");
        NavTitle.setTypeface(font_tony);
        barbtn.setText(R.string.icon_back);
        barbtn.setTypeface(font_awesome);
        barbtn.setOnClickListener(this);
        listUser = (ListView) findViewById(R.id.userList);

        usersAdapter = new UserListAdapter(this, -1, userItems);
        listUser.setAdapter(usersAdapter);
        listUser.setOnItemLongClickListener(this);
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
        popupMenu = new PopupMenu(getApplicationContext(),
                listUser);
        popupMenu.inflate(R.menu.menu_popup);
    }

    private PopupMenu popupMenu;

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        setDialog(userItems.get(position));
        return false;
    }

    private String[] items = {"Sửa", "Xóa"};

    public void setDialog(final UserItem userItem) {
        AlertDialog.Builder dialog = new Builder(this);
        dialog.setTitle(userItem.getName());
        dialog.setItems(items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        switch (i) {
                            case 0:
                                Intent intent = new Intent(ManagerUser.this,
                                        AddUser.class);
                                intent.putExtra(Var.USER_EXTRA, userItem);
                                startActivity(intent);
                                break;
                            case 1:

                                AlertDialog.Builder builder = new AlertDialog.Builder(ManagerUser.this);
                                builder.setMessage("\n Xóa thông tin người này? \n");
                                builder.setCancelable(false);
                                builder.setPositiveButton("KHÔNG",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                builder.setNegativeButton("XÓA", new DialogInterface.OnClickListener() {


                                            public void onClick(DialogInterface dialog, int id) {
                                                Log.d("TienDH", "id del: " + userItem.getId());
                                                if (ConnectionDetector.isNetworkConnected(getApplicationContext())) {
                                                    try {
                                                        APIConnection.delUser(getApplicationContext(), token, userItem.getId(), new VolleyCallback() {
                                                            @Override
                                                            public void onSuccess(JSONObject response) {

                                                                Log.d("TienDH", "update user: " + response);
                                                                try {
                                                                    int code = response.getInt("code");
                                                                    if (code == 201 || code == 200) {
                                                                        Toast.makeText(getApplicationContext(),
                                                                                "Xóa thành công", Toast.LENGTH_LONG).show();
                                                                        onResume();
                                                                    }
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }

                                                            }

                                                            @Override
                                                            public void onError(VolleyError error) {
                                                                Toast.makeText(getApplicationContext(),
                                                                        "Xảy ra lỗi, vui lòng thử lại!", Toast.LENGTH_LONG).show();
                                                            }
                                                        });
                                                    } catch (UnsupportedEncodingException e) {
                                                        e.printStackTrace();
                                                    }
                                                } else {
                                                    Toast.makeText(getApplicationContext(),
                                                            "Vui lòng kết nối internet!", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        }
                                );
                                builder.create().
                                        show();
                                break;
                        }
                    }
                }
        );
        dialog.show();
    }

}
