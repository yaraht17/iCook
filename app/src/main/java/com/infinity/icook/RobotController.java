package com.infinity.icook;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fpt.robot.RobotException;
import com.fpt.robot.app.RobotActivity;
import com.fpt.robot.motion.RobotGesture;
import com.fpt.robot.motion.RobotMotionAction;
import com.fpt.robot.motion.RobotMotionStiffnessController;
import com.fpt.robot.motion.RobotPosture;
import com.fpt.robot.tts.RobotTextToSpeech;

public class RobotController extends RobotActivity implements View.OnClickListener {

    private Button mBtScan, mBtSpeak, mBtRunGes, mBtStandup, mBtCrouch;
    private EditText mMessage;
    private Typeface font_awesome, font_tony;
    //nav
    private Button barbtn;
    private TextView NavTitle;
    private SharedPreferences sharedPreferences;

    @Override
    public void onRobotConnected(String addr, int port) {
        super.onRobotConnected(addr, port);
    }

    @Override
    public void onRobotDisconnected(String addr, int port) {
        super.onRobotDisconnected(addr, port);
    }

    @Override
    protected void onCreate(Bundle savedInstanceSate) {
        super.onCreate(savedInstanceSate);
        setContentView(R.layout.activity_robot_controller);

        mBtScan = (Button) findViewById(R.id.scan);
        mBtSpeak = (Button) findViewById(R.id.speak);
        mBtRunGes = (Button) findViewById(R.id.rungesture);
        mMessage = (EditText) findViewById(R.id.ed_message);
        mBtCrouch = (Button) findViewById(R.id.crouch);
        mBtStandup = (Button) findViewById(R.id.standup);

        mBtScan.setOnClickListener(this);
        mBtSpeak.setOnClickListener(this);
        mBtRunGes.setOnClickListener(this);
        mBtCrouch.setOnClickListener(this);
        mBtStandup.setOnClickListener(this);

        font_awesome = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        font_tony = Typeface.createFromAsset(this.getAssets(), "uvf-slimtony.ttf");
        NavTitle = (TextView) findViewById(R.id.NavTitle);
        barbtn = (Button) findViewById(R.id.barbtn);
        NavTitle.setText("Kết nối robot");
        NavTitle.setTypeface(font_tony);
        barbtn.setText(R.string.icon_back);
        barbtn.setTypeface(font_awesome);
        barbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.crouch:
                crouch();
                break;
            case R.id.standup:
                standUp();
                break;
            case R.id.scan:
                scan();
                break;
            case R.id.speak:
                speak(mMessage.getText().toString(), RobotTextToSpeech.ROBOT_TTS_LANG_VI);
                break;
            case R.id.rungesture:
                runRandomGesture(3);
                break;
            case R.id.barbtn:
                finish();
            default:
                break;
        }
    }

    public void speak(String message, String language) {
        if (getConnectedRobot() == null) {
            scan();
        } else {
            try {
                boolean b = RobotTextToSpeech.say(getConnectedRobot(), message, language);
                if (b) {
                    Log.e("nao api", "speak: " + message + " successful");
                } else {
                    Log.e("nao api", "speak: " + message + " failed");
                }
            } catch (RobotException e) {
                Log.e("nao api", "Speak failed: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void runRandomGesture(final int num) {
        if (getConnectedRobot() == null) {
            scan();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < num; i++) {
                        int j = (int) (Math.random() * 10) + 1;
                        runGesture("HandMotionBehavior" + j);
                    }
                }
            }).start();
        }
    }

    public void runGesture(final String name) {
        if (getConnectedRobot() == null) {
            scan();
        } else {
            try {
                Log.e("ano api", "Gesture: " + name);
                boolean b = RobotGesture.runGesture(getConnectedRobot(), name);
                if (b) {
                    Log.e("ano api", "run Gesture: " + name + " successful");
                } else {
                    Log.e("ano api", "run Gesture: " + name + " failed");
                }
            } catch (RobotException e) {
                e.printStackTrace();
            }
        }
    }

    public void standUp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (getConnectedRobot() == null) {
                    scan();
                } else {
                    boolean result = false;
                    try {
                        showToast("Standing up...");
                        result = RobotMotionAction.standUp(getConnectedRobot(), 0.5f);
                        if (!result) {
                            showToast("Stand up failed ");
                        } else {
                            showToast("Stand up successful");
                        }
                    } catch (RobotException e) {
                        showToast("Stand up failed: ");
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void crouch() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (getConnectedRobot() == null) {
                    scan();
                } else {
                    boolean result = false;
                    try {
                        showToast("Crouching...");
                        result = RobotPosture.goToPosture(getConnectedRobot(), "Crouch", 0.5f);
                        if (!result) {
                            showToast("crouch failed ");
                        } else {
                            showToast("crouch successful");
                        }
                        result = RobotMotionStiffnessController.rest(getConnectedRobot());
                        if (!result) {
                            showToast("rest failed ");
                        } else {
                            showToast("res successful");
                        }
                    } catch (RobotException e) {
                        showToast("Crouch failed: ");
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RobotController.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }
}
