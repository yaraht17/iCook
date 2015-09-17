package com.infinity.icook;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class Home extends Activity implements View.OnClickListener {

    private TextView NavTitle, iCookBtnText;
    private View iCookBtnLayout, chatBar;
    private int screenWidth,screenHeight;

    FragmentTransaction fragmentTransaction;
    private Button barbtn;
    private int barid;
    Typeface font_awesome,font_tony;
    FragmentManager fragmentManager;
    private ArrayList<Cat_Item> items = new ArrayList<Cat_Item>();
    CategoryDetails catdetails;
    ChatFragment chatview;
    private enum Mode {
        TALK, BROWSE, DETAILS
    }

    Mode mode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        chatview = new ChatFragment();

        barbtn = (Button) findViewById(R.id.barbtn);
        chatBar = findViewById(R.id.chatBar);
        barid = R.string.icon_toggle;
        font_awesome = Typeface.createFromAsset( getAssets(), "fontawesome-webfont.ttf" );
        font_tony = Typeface.createFromAsset(this.getAssets(), "uvf-slimtony.ttf");

        barbtn.setTypeface(font_awesome);
        mode = Mode.BROWSE;
        Display display = getWindowManager().getDefaultDisplay();
        screenWidth =  display.getWidth();
        screenHeight = display.getHeight();
        int cat_size = (screenWidth-80)/2;
        fragmentManager = getFragmentManager();

        items.add(new Cat_Item(R.drawable.cat_cake,"Cake"));
        items.add(new Cat_Item(R.drawable.cat_egg,"Egg"));
        items.add(new Cat_Item(R.drawable.cat_fish,"Fish"));
        items.add(new Cat_Item(R.drawable.cat_meat,"Meat"));
        items.add(new Cat_Item(R.drawable.cat_soup,"Soup"));
        items.add(new Cat_Item(R.drawable.cat_vegetable,"Vegetable"));

        NavTitle = (TextView) findViewById(R.id.NavTitle);
        iCookBtnText = (TextView) findViewById(R.id.icookbtn);
        iCookBtnLayout = findViewById(R.id.icookbtnlayout);
        NavTitle.setTypeface(font_tony);
        iCookBtnText.setTypeface(font_tony);
        GridView gridview = (GridView) this.findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this,cat_size, items));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                OpenDetails(items.get(position));
            }
        });
        barbtn.setOnClickListener(this);
        iCookBtnLayout.setOnClickListener(this);
    }

    public void OpenDetails(Cat_Item item) {
        catdetails = new CategoryDetails(item.name);
        FragmentTransaction fragmentTransaction;
        barid = R.string.icon_back;
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in,R.anim.slide_out);

        this.NavTitle.setText(item.name);
        this.barbtn.setText(R.string.icon_back);

        fragmentTransaction.replace(R.id.contents, catdetails);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        mode=Mode.DETAILS;
    }

    private void CloseDetails(){
        fragmentManager.popBackStack();

        fragmentTransaction = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.icookbtnlayout: {
                switch (mode) {
                    case BROWSE:
                        mode = Mode.TALK;
                        break;
                    case TALK:

                        break;
                }
                switchMode();
                break;
            }
            case R.id.barbtn: {
                switch (barid) {
                    case R.string.icon_back:
                    {
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
                    case R.string.icon_toggle:
                    {
                        break;
                    }
                }
            }
            default:
                break;
        }
    }
    private int switchButtonposition(){
        return screenWidth/2 - iCookBtnLayout.getWidth()/4;
    }
    private void switchMode(){
        switch (mode) {
            case BROWSE:
                Log.d("Value", "Switch to browse");
                iCookBtnLayout.animate().translationX(0).setInterpolator(new AccelerateDecelerateInterpolator());
                iCookBtnText.setTypeface(font_tony);
                iCookBtnText.setText(R.string.app_name);
                iCookBtnText.setTextSize(20);
                chatBar.animate().translationY(0).setInterpolator(new AccelerateDecelerateInterpolator());
                break;
            case TALK:
                this.NavTitle.setText(R.string.assist_title);
                this.barbtn.setText(R.string.icon_back);
                barid = R.string.icon_back;
                iCookBtnLayout.animate().translationX(-switchButtonposition()).setDuration(500).setInterpolator(new AccelerateDecelerateInterpolator());
                chatBar.animate().translationY(-chatBar.getHeight()).setInterpolator(new AccelerateDecelerateInterpolator());
                iCookBtnText.setTypeface(font_awesome);
                iCookBtnText.setText(R.string.icon_microphone);
                iCookBtnText.setTextSize(50);

                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in,R.anim.slide_out);
                fragmentTransaction.replace(R.id.contents, chatview);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case DETAILS:
                mode = Mode.BROWSE;
                CloseDetails();
                break;
        }
    }

}
