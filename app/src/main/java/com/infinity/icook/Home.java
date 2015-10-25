package com.infinity.icook;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
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

import com.infinity.adapter.CustomDrawerAdapter;
import com.infinity.adapter.ImageAdapter;
import com.infinity.fragment.CategoryDetails;
import com.infinity.fragment.ChatFragment;
import com.infinity.model.CatItem;
import com.infinity.model.DrawerItem;
import com.infinity.service.ClockService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class Home extends Activity implements View.OnClickListener {
    private static final int REQUEST_CODE = 1234;

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

    // tao enum mode cac kieu che do o man hinh chinh, Talk : noi chuyen voi AI, Browse : xem Category, Details :xem ben trong category
    private enum Mode {
        TALK, BROWSE, DETAILS
    }

    Mode mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
                OpenDetails(items.get(position));
            }
        });
        barbtn.setOnClickListener(this);
        iCookBtnLayout.setOnClickListener(this);
        btnSend.setOnClickListener(this);
    }

    private void addItemToCategoryList() {
        items.add(new CatItem(R.drawable.cat_cake, "Cake"));
        items.add(new CatItem(R.drawable.cat_egg, "Egg"));
        items.add(new CatItem(R.drawable.cat_fish, "Fish"));
        items.add(new CatItem(R.drawable.cat_meat, "Meat"));
        items.add(new CatItem(R.drawable.cat_soup, "Soup"));
        items.add(new CatItem(R.drawable.cat_vegetable, "Vegetable"));
    }
    // ham mo 1 category
    public void OpenDetails(CatItem item) {
        catdetails = new CategoryDetails(item.getName());
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
        fragmentManager.popBackStack();
        fragmentTransaction = null;
    }

    private ArrayList initDrawerData() {
        ArrayList list = new ArrayList();
        list.add(new DrawerItem("Hoàng Tiến", "Đẹp Trai", R.drawable.ava));
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

                    break;
                case 3:

                    break;
                default:
                    break;
            }

        }
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
                                CloseDetails();
                                break;
                            case DETAILS:  // truong hop bam nut back khi dang o fragment xem category
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
                chatview.sendChatMessage(chatText.getText().toString());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String s = chatText.getText().toString();
                        if (!s.equals("")) {
                            speakTTS(s);
                        }
                    }
                }).start();
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
                            InputMethodManager imanager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imanager.hideSoftInputFromWindow(chatText.getWindowToken(), 0);

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

    //mo google voice
    private void startVoiceRecognitionActivity() {
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

    /**
     * Handle the results from the voice recognition activity.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Populate the wordsList with the String values the recognition
            // engine thought it heard
            final ArrayList<String> matches = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches.size() != 0) {
                chatview.sendChatMessage(matches.get(0));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String s = matches.get(0);
                        if (!s.equals("")) {
                            speakTTS(s);
                        }
                    }
                }).start();
                chatText.setText("");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void stopSpeakVi() {
        mediaPlayer.stop();
    }

    private String TAG = "SMAC 2015 TTS";
    private String mHost = "http://118.69.135.22";

    //tai file wav tu host va phat am
    @SuppressWarnings("deprecation")
    public void speakTTS(String msg) {
        String URL = mHost + "/synthesis/file?voiceType=female&text=" + URLEncoder.encode(msg);
        downloadFile(URL, "sdcard/sound.wav");
    }

    //phat am
    public void speakVi(final String filePath) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initMediaPlayer(filePath);
                mediaPlayer.start();
            }
        });
    }

    public void downloadFile(final String URL, final String filePath) {
        try {
            java.net.URL url = new URL(URL);
            Log.e(TAG, "Download URL: " + url.toString());
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("accept-charset", "UTF-8");
            urlConnection.setRequestProperty("content-type", "application/x-www-form-urlencoded; charset=utf-8");
            urlConnection.setDoOutput(true);
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            final File file = new File(filePath);
            FileOutputStream fileOutput = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int bufferLength = 0;
            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
            }
            speakVi(file.getAbsolutePath());
            fileOutput.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int stateMediaPlayer;
    public final int stateMP_Error = 0;
    public final int stateMP_NotStarter = 1;
    public MediaPlayer mediaPlayer;

    public void initMediaPlayer(String path) {
        String PATH_TO_FILE = path;
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(PATH_TO_FILE);
            mediaPlayer.prepare();
            stateMediaPlayer = stateMP_NotStarter;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            stateMediaPlayer = stateMP_Error;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            stateMediaPlayer = stateMP_Error;
        } catch (IOException e) {
            e.printStackTrace();
            stateMediaPlayer = stateMP_Error;
        }
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
//        if (fragmentManager == null) {
//            Toast.makeText(getApplicationContext(), "Press agian", Toast.LENGTH_SHORT).show();
//        }
    }

    private int time;

    public void startService(String tmp) {
        if (tmp.equals("")) {
            Toast.makeText(this, "set Time again !", Toast.LENGTH_SHORT).show();
        } else {
            time = Integer.parseInt(tmp);
            Intent intent = (new Intent(getBaseContext(), ClockService.class));
            intent.putExtra("time", time);
            stopService(intent);
            startService(intent);
        }

    }

    public void stopService() {
        Intent intent = (new Intent(getBaseContext(), ClockService.class));
        stopService(intent);
    }
}
