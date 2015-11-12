package com.infinity.icook;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.infinity.data.Var;
import com.infinity.model.DishItem;
import com.infinity.model.MaterialItem;
import com.infinity.service.TextToSpeech;

public class DishDetailActivity extends AppCompatActivity {

    private DishItem dish;
    private TextView name, description, instruction, aop, material;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_detail);

        dish = (DishItem) getIntent().getSerializableExtra(Var.DISH_EXTRA);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = (TextView) findViewById(R.id.name);
        description = (TextView) findViewById(R.id.description);
        instruction = (TextView) findViewById(R.id.instruction);
        aop = (TextView) findViewById(R.id.aop);
        material = (TextView) findViewById(R.id.material);

        name.setText(dish.getName());
        description.setText(dish.getIntroduce());
        instruction.setText(dish.getInstruction());
        aop.setText("Nguyên liệu cho " + dish.getAop() + " người");
        String materialText = "";
        for (MaterialItem material : dish.getMaterials()) {
            String s = "";
            if (Integer.parseInt(material.getAmount()) != 0) {
                s = material.getAmount() + " " + material.getUnit() + " " + material.getName();
            } else {
                s = material.getName();
            }
            materialText = materialText + s + "\n";
        }
        material.setText(materialText);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(dish.getName());

        loadBackdrop();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String s = dish.getName() + " . " + dish.getIntroduce();
                        if (!s.equals("")) {
                            TextToSpeech.speakTTS(s);
                        }
                    }
                }).start();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void loadBackdrop() {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(dish.getImage()).centerCrop().into(imageView);
    }

//    public void stopSpeakVi() {
//        mediaPlayer.stop();
//    }
//
//    private String TAG = "SMAC 2015 TTS";
//    private String mHost = "http://118.69.135.22";
//
//    //tai file wav tu host va phat am
//    @SuppressWarnings("deprecation")
//    public void speakTTS(String msg) {
//        String URL = mHost + "/synthesis/file?voiceType=female&text=" + URLEncoder.encode(msg);
//        downloadFile(URL, "sdcard/sound.wav");
//    }
//
//    //phat am
//    public void speakVi(final String filePath) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                initMediaPlayer(filePath);
//                mediaPlayer.start();
//            }
//        });
//    }
//
//    public void downloadFile(final String URL, final String filePath) {
//        try {
//            java.net.URL url = new java.net.URL(URL);
//            Log.e(TAG, "Download URL: " + url.toString());
//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setRequestMethod("GET");
//            urlConnection.setRequestProperty("accept-charset", "UTF-8");
//            urlConnection.setRequestProperty("content-type", "application/x-www-form-urlencoded; charset=utf-8");
//            urlConnection.setDoOutput(true);
//            urlConnection.connect();
//            InputStream inputStream = urlConnection.getInputStream();
//            final File file = new File(filePath);
//            FileOutputStream fileOutput = new FileOutputStream(file);
//            byte[] buffer = new byte[1024];
//            int bufferLength = 0;
//            while ((bufferLength = inputStream.read(buffer)) > 0) {
//                fileOutput.write(buffer, 0, bufferLength);
//            }
//            speakVi(file.getAbsolutePath());
//            fileOutput.close();
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public int stateMediaPlayer;
//    public final int stateMP_Error = 0;
//    public final int stateMP_NotStarter = 1;
//    public MediaPlayer mediaPlayer;
//
//    public void initMediaPlayer(String path) {
//        String PATH_TO_FILE = path;
//        mediaPlayer = new MediaPlayer();
//        try {
//            mediaPlayer.setDataSource(PATH_TO_FILE);
//            mediaPlayer.prepare();
//            stateMediaPlayer = stateMP_NotStarter;
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//            stateMediaPlayer = stateMP_Error;
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
//            stateMediaPlayer = stateMP_Error;
//        } catch (IOException e) {
//            e.printStackTrace();
//            stateMediaPlayer = stateMP_Error;
//        }
//    }
}
