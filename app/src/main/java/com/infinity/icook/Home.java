package com.infinity.icook;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.infinity.adapter.CustomDrawerAdapter;
import com.infinity.adapter.ImageAdapter;
import com.infinity.data.ConnectionDetector;
import com.infinity.data.Data;
import com.infinity.data.Progress;
import com.infinity.data.Var;
import com.infinity.fragment.CategoryDetails;
import com.infinity.fragment.ChatFragment;
import com.infinity.model.CatItem;
import com.infinity.model.ChatMessage;
import com.infinity.model.DishItem;
import com.infinity.model.DrawerItem;
import com.infinity.model.MaterialItem;
import com.infinity.model.OutputItem;
import com.infinity.service.ClockService;
import com.infinity.service.TextToSpeech;
import com.infinity.volley.APIConnection;
import com.infinity.volley.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;

import static edu.cmu.pocketsphinx.SpeechRecognizerSetup.defaultSetup;

public class Home extends Activity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, RecognitionListener {
    private static final int REQUEST_CODE = 1234;
    private GoogleApiClient mGoogleApiClient;

    private TextView NavTitle, iCookBtnText;
    private View iCookBtnLayout, chatBar;
    private EditText chatText;
    private TextView btnSend;
    private Button barbtn;
    Typeface font_awesome, font_tony;

    //fragment
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    CategoryDetails catdetails;
    ChatFragment chatview;

    //drawer
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CustomDrawerAdapter drawerAdapter;
    private List<DrawerItem> drawerDataList;


    private int barid;
    private int screenWidth, screenHeight;

    private ArrayList<CatItem> items = new ArrayList<CatItem>();

    private SharedPreferences sharedPreferences;
    private String accessToken;
    private String idEmail;

    private int steps = 0;
    private boolean mIsResolving = false;
    private boolean mShouldResolve = false;
    private static final int RC_SIGN_IN = 0;

    Mode mode;
    private ProgressDialog progressDialog;

    // tao enum mode cac kieu che do o man hinh chinh, Talk : noi chuyen voi AI, Browse : xem Category, Details :xem ben trong category
    private enum Mode {
        TALK, BROWSE, DETAILS
    }
    @Override
    public void onConnected(Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (!mIsResolving && mShouldResolve) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
                    mIsResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    Log.e("TienDH", "Could not resolve ConnectionResult.", e);
                    mIsResolving = false;
                    mGoogleApiClient.connect();
                }
            } else {
                Toast.makeText(this, "Xảy ra lỗi", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .addScope(new Scope(Scopes.EMAIL))
                .build();

        loadRecording();

        sharedPreferences = getSharedPreferences(Var.MY_PREFERENCES, Context.MODE_PRIVATE);
        accessToken = sharedPreferences.getString(Var.ACCESS_TOKEN, "");
        idEmail = sharedPreferences.getString(Var.USER_EMAIL, "");
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        screenWidth = displaymetrics.widthPixels;

        //khai bao font
        font_awesome = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        font_tony = Typeface.createFromAsset(this.getAssets(), "uvf-slimtony.ttf");

        //thiet lap thanh menu o bottom
        NavTitle = (TextView) findViewById(R.id.NavTitle);
        iCookBtnText = (TextView) findViewById(R.id.icookbtn);
        iCookBtnLayout = findViewById(R.id.icookbtnlayout);
        btnSend = (TextView) findViewById(R.id.btnSend);
        chatText = (EditText) findViewById(R.id.chatText);

        //setup drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        drawerDataList = initDrawerData();
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        drawerAdapter = new CustomDrawerAdapter(getApplicationContext(), R.layout.custom_drawer_item,
                drawerDataList);
        mDrawerList.setAdapter(drawerAdapter);

        NavTitle.setTypeface(font_tony);
        iCookBtnText.setTypeface(font_tony);
        btnSend.setTypeface(font_awesome);

        // khai bao fragment cho phan chat
        chatview = new ChatFragment();
        // setup
        barbtn = (Button) findViewById(R.id.barbtn); // button toggle de mo nav drawer
        chatBar = findViewById(R.id.chatBar); //khung chat de push len luc can hoi con bot
        barid = R.string.icon_toggle; //
        barbtn.setTypeface(font_awesome);

        // khoi tao la vao mode browse, xem category
        mode = Mode.BROWSE;

        int cat_size = (screenWidth - 80) / 2;
        fragmentManager = getFragmentManager();

        // add item vao list category
        addItemToCategoryList();

        // setup gridview
        GridView gridview = (GridView) this.findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this, cat_size, items));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                OpenDetails(items.get(position), String.valueOf(position + 1));
            }
        });
        barbtn.setOnClickListener(this);
        iCookBtnLayout.setOnClickListener(this);
        btnSend.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        recognizer.cancel();
        recognizer.shutdown();
    }
    private void addItemToCategoryList() {
        items.add(new CatItem(R.drawable.cat_egg, "Poultry"));
        items.add(new CatItem(R.drawable.cat_meat, "Meat"));
        items.add(new CatItem(R.drawable.cat_vegetable, "Vegetable"));
        items.add(new CatItem(R.drawable.cat_soup, "Soup"));
        items.add(new CatItem(R.drawable.cat_fish, "Fish"));
        items.add(new CatItem(R.drawable.cat_cake, "Cake"));
    }

    // ham mo 1 category
    public void OpenDetails(CatItem item, String position) {
        catdetails = new CategoryDetails(position);
        FragmentTransaction fragmentTransaction;
        barid = R.string.icon_back;
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);

        this.NavTitle.setText(item.getName());
        this.barbtn.setText(R.string.icon_back);

        fragmentTransaction.replace(R.id.contents, catdetails);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        // chuyen mod
        mode = Mode.DETAILS;
    }

    // ham dong category
    private void CloseDetails() {
        Log.d("TienDH", "close details");
        fragmentManager.popBackStack();
        fragmentTransaction = null;
    }

    private ArrayList initDrawerData() {
        ArrayList list = new ArrayList();
        list.add(new DrawerItem(idEmail, "", R.drawable.ava));
        list.add(new DrawerItem("Thông tin gia đình", R.string.users));
        list.add(new DrawerItem("Báo thức", R.string.settings_icon));
        list.add(new DrawerItem("Đăng xuất", R.string.logout_icon));
        return list;
    }

    //xu ly su kien cho drawer
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent;
            switch (position) {
                case 0:
                    break;
                case 1:
                    intent = new Intent(getApplicationContext(), ManagerUser.class);
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    startActivity(intent);
                    break;
                case 2:
                    intent = new Intent(getApplicationContext(), ClockActivity.class);
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    startActivity(intent);
                    break;
                case 3:
                    //logout
                    createDialog();
                    break;
                default:
                    break;
            }
        }
    }

    public void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("\n Đăng xuất ngay bây giờ? \n");
        builder.setCancelable(false);
        builder.setPositiveButton("HỦY",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.setNegativeButton("ĐĂNG XUẤT", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                onSignOutClicked();
            }
        });
        builder.create().show();
    }

    //logout
    private void onSignOutClicked() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đăng xuất...");
        progressDialog.show();
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Var.ACCESS_TOKEN);
        editor.remove(Var.USER_EMAIL);
        editor.commit();
        progressDialog.cancel();
        Intent intent = new Intent(this, SqlashScreen.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icookbtnlayout: { // action khi bam vao nut icook o giua thanh menu bottom
                switch (mode) {
                    case BROWSE:
                        mode = Mode.TALK;
                        break;
                    case TALK:
                        startVoiceRecognitionActivity();
                        mode = Mode.TALK;
                        Log.d("TienDH", "mode:  " + mode.toString());
                        break;
                }
                switchMode();
                break;
            }
            case R.id.barbtn: {
                switch (barid) {
                    case R.string.icon_back: // action khi bam vao nut back khi dang xem chi tiet category, binh thuong app bi loi khi bam nut back la do no ko chay nhung ham o trong nay
                    {
                        switch (mode) {
                            case TALK: // truong hop bam nut back khi dang o fragment chat voi bot
                                mode = Mode.BROWSE; // set mode = browse roi chay ham switchMode() de tro ve mode browse
                                Log.d("TienDH", "mode click barbtn:  " + mode.toString());
                                CloseDetails();
                                break;
                            case DETAILS:  // truong hop bam nut back khi dang o fragment xem category
                                mode = Mode.BROWSE;
                                CloseDetails();
                                break;
                        }
                        //setup lai man hinh chinh
                        barid = R.string.icon_toggle;
                        barbtn.setText(R.string.icon_toggle);
                        this.NavTitle.setText(R.string.category);
                        switchMode();
                        break;
                    }
                    case R.string.icon_toggle: {
                        mDrawerLayout.openDrawer(Gravity.LEFT);
                        break;
                    }
                }
            }
            case R.id.btnSend:
                String text = chatText.getText().toString();
                if (text != null && !text.equals("")) {
                    ChatMessage message = new ChatMessage(false, text, "");
                    chatview.sendChatMessage(message);
                    chat(text);
                }
                chatText.setText("");
                break;
            default:
                break;
        }
    }

    private int switchButtonposition() {
        return screenWidth / 2 - iCookBtnLayout.getWidth() / 4;
    }

    // ham chay khi chuyen qua lai cac mode
    private void switchMode() {
        switch (mode) {
            case BROWSE: // tro ve mode browse tu cac mode khac
                CloseDetails();
                Log.d("Value", "Switch to browse");
                iCookBtnLayout.animate().translationX(0).setInterpolator(new AccelerateDecelerateInterpolator());
                iCookBtnText.setTypeface(font_tony);
                iCookBtnText.setText(R.string.app_name);
                iCookBtnText.setTextSize(20);
                chatBar.animate().translationY(0).setInterpolator(new AccelerateDecelerateInterpolator());
                break;
            case TALK: // chuyen sang mode chat voi bot
                this.NavTitle.setText(R.string.assist_title);
                this.barbtn.setText(R.string.icon_back);
                barid = R.string.icon_back;
                iCookBtnLayout.animate().translationX(-switchButtonposition()).setDuration(500).setInterpolator(new AccelerateDecelerateInterpolator());
                chatBar.animate().translationY(-chatBar.getHeight()).setInterpolator(new AccelerateDecelerateInterpolator());
                iCookBtnText.setTypeface(font_awesome);
                iCookBtnText.setText(R.string.icon_microphone);
                iCookBtnText.setTextSize(50);
                chatText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            hideKeyboard();
                        }
                    }
                    private void hideKeyboard() {
                        if (chatText != null) {
                            closeKeyboard();
                        }
                    }
                });
                chatText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String text = s.toString().toLowerCase(Locale.getDefault());
                        if (text.equals("")) {
                            btnSend.setTextColor(Color.parseColor("#FFFFFF"));
                        } else {
                            btnSend.setTextColor(Color.parseColor("#00AFF0"));
                        }
                    }
                });

                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
                fragmentTransaction.replace(R.id.contents, chatview);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case DETAILS: // tro ve mode details khi dang xem details
                mode = Mode.BROWSE;
                CloseDetails();
                break;
        }
    }

    private void closeKeyboard() {
        InputMethodManager imanager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imanager.hideSoftInputFromWindow(chatText.getWindowToken(), 0);
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        switch (barid) {
            case R.string.icon_back: {
                switch (mode) {
                    case TALK:
                        mode = Mode.BROWSE;
                        CloseDetails();
                        break;
                    case DETAILS:
                        break;
                }
                barid = R.string.icon_toggle;
                barbtn.setText(R.string.icon_toggle);
                this.NavTitle.setText(R.string.category);
                switchMode();
                break;
            }
        }
    }

    public void startService(final String tag, int tmp) {
        if (tmp != 0) {
            Intent intent = (new Intent(getBaseContext(), ClockService.class));
            intent.addCategory(tag);
            intent.putExtra("time", tmp);
            Log.d("TienDH", "start service");
            stopService(intent);
            startService(intent);
        }

    }

    public void stopService(final String tag) {
        Intent intent = (new Intent(getBaseContext(), ClockService.class));
        intent.addCategory(tag);
        stopService(intent);
    }

    public void talkTTS(final String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!msg.equals("")) {
                    TextToSpeech.speakTTS(msg);
                }
            }
        }).start();
    }

    private void chat(String text) {
        if (!Progress.CheckList(text, Var.nextStep)) {
            if (ConnectionDetector.isNetworkConnected(getApplicationContext())) {
                try {
                    APIConnection.getChat(this, text, new VolleyCallback() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            Log.d("TienDH", "chat res: " + response);
                            // xu ly
                            String result = "";
                            try {
                                int type = response.getInt("type");
                                switch (type) {
                                    case 1:
                                        //DONE : tra loi
                                        result = response.getString("result");
                                        if (!result.equals("") && result != null) {
                                            TalkAndSendMess(new OutputItem(result));
                                        }
                                        break;
                                    case 2:
                                        steps = 0;
                                        //TAM DONE: lay ve string
                                        ArrayList<String> steps = new ArrayList<String>();
                                        JSONArray data = response.getJSONArray("data");
                                        for (int i = 0; i < data.length(); i++) {
                                            String s = (String) data.get(i);
                                            steps.add(s);
                                        }
                                        //convert string
                                        ArrayList<String> stepsTalk = Progress.tokenizer(steps);
                                        Data.stepInstruction = (ArrayList<String>) stepsTalk.clone();
                                        result = stepsTalk.get(0);
                                        Log.d("TienDH", "thuc hien: " + result);
                                        if (!result.equals("") && result != null) {
                                            TalkAndSendMess(new OutputItem(result));
                                        }
                                        break;
                                    case 3:
                                        result = "";
                                        ArrayList<DishItem> dishList = APIConnection.parseDishSmart(response);
                                        for (DishItem dish : dishList) {
                                            result = result + dish.getName() + ", ";
                                        }
                                        if (!result.equals("") && result != null) {
                                            TalkAndSendMess(new OutputItem("Bạn có thể nấu " + result));
                                        } else {
                                            TalkAndSendMess(new OutputItem("Tôi không hiểu bạn nói gì cả"));
                                        }
                                        break;
                                    case 4:
                                        Log.d("TienDH", "Hôm nay ăn gì?");
                                        smartConsult(accessToken);
                                        break;
                                    case 5:
                                        //DONE
                                        //chuyen xau ve so
                                        result = response.getString("result");
                                        String[] timeResult = result.split(":");
                                        int h = 0, m = 0, t = 0;
                                        if (timeResult.length >= 2) {
                                            h = Integer.parseInt(timeResult[0]);
                                            m = Integer.parseInt(timeResult[1]);
                                            t = (h * 60 + m) * 60;
                                        }
                                        //them tin nhan
                                        if (t != 0) {
                                            result = "Tôi sẽ nhắc bạn sau " + Progress.convertTime(h, m) + " nữa";
                                            TalkAndSendMess(new OutputItem(result));
                                            //tinh tgian bao thuc
                                            long timeCurrent = System.currentTimeMillis();
                                            long endTime = timeCurrent + t * 1000;
                                            String dateString = new SimpleDateFormat("h:mm a").format(new Date(endTime));
                                            //luu lai
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString(Var.CLOCK_TIME, dateString);
                                            editor.commit();
                                            //goi service
                                            startService(ClockService.TAG, t);
                                        }
                                        break;
                                    case 6:
                                        int aop = response.getInt("aop");
                                        if (aop != 0) {
                                            //show nguyen lieu
                                            String mess = "Bạn cần chuẩn bị ";
                                            String talk = "Bạn cần chuẩn bị ";
                                            JSONArray dataMat = response.getJSONArray("data");
                                            if (dataMat != null) {
                                                ArrayList<MaterialItem> materials = APIConnection.parseMaterialList(dataMat);
                                                OutputItem outMat = Progress.convertMatListToOutput(materials);
                                                if (!outMat.getMess().equals("") && !outMat.getTalk().equals("")) {
                                                    talk += outMat.getTalk();
                                                    mess += outMat.getMess();
                                                    TalkAndSendMess(new OutputItem(talk, mess));
                                                }
                                            } else {
                                                TalkAndSendMess(new OutputItem("Tôi không hiểu bạn nói gì cả"));
                                            }
                                        } else {
                                            String idDish = response.getString("result");
                                            getMat(accessToken, idDish);
                                            //gui token
                                        }
                                        break;
                                    case 7:
                                        //can chuan bi gi?
                                        if (Data.recomendDish != null) {
                                            String talkFinal = "";
                                            String messFinal = "";
                                            for (DishItem dish : Data.recomendDish) {
                                                OutputItem outMat = Progress.convertMatListToOutput(dish.getMaterials());
                                                talkFinal += outMat.getTalk();
                                                messFinal += outMat.getMess();
                                            }
                                            if (!messFinal.equals("") && !talkFinal.equals("")) {
                                                talkFinal = "Bạn cần chuẩn bị " + talkFinal;
                                                messFinal = "Bạn cần chuẩn bị " + messFinal;
                                                TalkAndSendMess(new OutputItem(talkFinal, messFinal));
                                            } else {
                                                TalkAndSendMess(new OutputItem("Tôi không hiểu bạn nói gì cả"));
                                            }
                                        } else {
                                            TalkAndSendMess(new OutputItem("Ý bạn là chuẩn bị gì cơ"));
                                        }
                                        Data.recomendDish = null;
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Xảy ra lỗi!", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                //show thog bao
                Toast.makeText(getApplicationContext(), "Vui lòng kết nối internet!", Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d("TienDH", "Steps: " + Data.stepInstruction.size());
            if (steps >= Data.stepInstruction.size() - 1) {
                String s = "Món ăn đã hoàn thành rồi, chúc bạn ngon miệng";
                TalkAndSendMess(new OutputItem(s));
                steps = 0;
                Data.stepInstruction = null;
            } else {
                steps++;
                String s = Data.stepInstruction.get(steps);
                TalkAndSendMess(new OutputItem(s));
            }
        }
    }

    // lay nguyen lieu theo thong tin ng dung
    private void getMat(String token, String idDish) {
        try {
            APIConnection.getMat(getApplicationContext(), token, idDish, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    //show nguyen lieu
                    String mess = "Bạn cần chuẩn bị ";
                    String talk = "Bạn cần chuẩn bị ";
                    DishItem dish = APIConnection.parseDish(response);
                    OutputItem outMat = Progress.convertMatListToOutput(dish.getMaterials());
                    if (!outMat.getMess().equals("") && !outMat.getTalk().equals("")) {
                        talk += outMat.getTalk();
                        mess += outMat.getMess();
                        TalkAndSendMess(new OutputItem(talk, mess));

                    } else {
                        TalkAndSendMess(new OutputItem("Tôi không hiểu bạn nói gì cả"));
                    }
                }
                @Override
                public void onError(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Xảy ra lỗi!", Toast.LENGTH_LONG).show();
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    //lay mon an thong minh
    private void smartConsult(String token) {
        try {
            APIConnection.sendToken(getApplicationContext(), token, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    String talk = "Bạn có thể nấu ";
                    String dishTalk = "";
                    ArrayList<DishItem> listDish = APIConnection.parseDishSmart(response);
                    Data.recomendDish = (ArrayList<DishItem>) listDish.clone();
                    for (DishItem dish : listDish) {
                        dishTalk = dishTalk + dish.getName() + ", ";
                    }

                    if (!dishTalk.equals("")) {
                        talk += dishTalk;
                        TalkAndSendMess(new OutputItem(talk));
                    } else {
                        TalkAndSendMess(new OutputItem("Tôi không hiểu bạn nói gì cả"));
                    }
                }

                @Override
                public void onError(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Xảy ra lỗi!", Toast.LENGTH_LONG).show();
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void TalkAndSendMess(OutputItem out) {
        ChatMessage message = new ChatMessage(true, out.getMess(), "");
        chatview.sendChatMessage(message);
        talkTTS(out.getTalk());
    }

    //mo google voice
    private void startVoiceRecognitionActivity() {
        //tat lang nghe de gianh mic cho google voice
        recognizer.stop();
        try {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                    Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Bạn cần tư vấn....");
            startActivityForResult(intent, REQUEST_CODE);
        } catch (Exception e) {
            Toast.makeText(this,
                    "Điện thoại của bạn không hỗ trợ Google Voice", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            final ArrayList<String> matches = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches.size() != 0) {
                String text = matches.get(0);
                if (text != null && !text.equals("")) {
                    ChatMessage message = new ChatMessage(false, text, "");
                    chatview.sendChatMessage(message);
                    chat(text);
                }
                chatText.setText("");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
        //mo lai lang nghe
        if (KWS_SEARCH.equals(KWS_SEARCH))
            recognizer.startListening(KWS_SEARCH);
    }

    //listening icook
      /* Named searches allow to quickly reconfigure the decoder */
    private static final String KWS_SEARCH = "wakeup";

    /* Keyword we are looking for to activate menu */
    private static final String KEYPHRASE = "open voice";

    private SpeechRecognizer recognizer;

    private void loadRecording() {
        new AsyncTask<Void, Void, Exception>() {
            @Override
            protected Exception doInBackground(Void... params) {
                try {
                    Assets assets = new Assets(Home.this);
                    File assetDir = assets.syncAssets();
                    setupRecognizer(assetDir);
                } catch (IOException e) {
                    return e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Exception result) {
                if (result != null) {
                    Toast.makeText(getApplicationContext(), "Recording failed", Toast.LENGTH_SHORT).show();
                } else {
                    switchSearch(KWS_SEARCH);
                }
            }
        }.execute();
    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onEndOfSpeech() {
        if (!recognizer.getSearchName().equals(KWS_SEARCH))
            switchSearch(KWS_SEARCH);
    }

    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis == null)
            return;
        String text = hypothesis.getHypstr();
        if (text.equals(KEYPHRASE)) {
            switchSearch(KWS_SEARCH);
//            Toast.makeText(this, "Ok google demo", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResult(Hypothesis hypothesis) {
        if (hypothesis != null) {
            String text = hypothesis.getHypstr();
            Toast.makeText(getApplicationContext(), text + "Turn on", Toast.LENGTH_SHORT).show();
            switch (mode) {
                case BROWSE:
                    mode = Mode.TALK;
                    talkTTS("tôi có thể giúp gì cho bạn");
                    break;
                case TALK:

                    startVoiceRecognitionActivity();
//                    loadRecording();
//                    recognizer.startListening(KWS_SEARCH, 10000);
                    mode = Mode.TALK;
                    break;
            }
            switchMode();
        }
    }

    @Override
    public void onError(Exception e) {
    }

    @Override
    public void onTimeout() {
        switchSearch(KWS_SEARCH);
    }

    private void switchSearch(String searchName) {
        recognizer.stop();
        // If we are not spotting, start listening with timeout (10000 ms or 10 seconds).
        if (searchName.equals(KWS_SEARCH))
            recognizer.startListening(searchName);
        else
            recognizer.startListening(searchName, 1000);
    }

    private void setupRecognizer(File assetsDir) throws IOException {
        recognizer = defaultSetup()
                .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))
                .setRawLogDir(assetsDir)
                .setKeywordThreshold(1e-45f)
                .setBoolean("-allphone_ci", true)
                .getRecognizer();
        recognizer.addListener(this);
        recognizer.addKeyphraseSearch(KWS_SEARCH, KEYPHRASE);
    }
}
