package com.infinity.service;

import android.media.MediaPlayer;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class TextToSpeech {

    public static void stopSpeakVi() {
        mediaPlayer.stop();
    }

    private static String TAG = "SMAC 2015 TTS";
    private static String mHost = "http://118.69.135.22";

    //tai file wav tu host va phat am
    @SuppressWarnings("deprecation")
    public static void speakTTS(String msg) {
        String URL = mHost + "/synthesis/file?voiceType=female&text=" + URLEncoder.encode(msg) + "\"";
        downloadFile(URL, "sdcard/sound.wav");
    }

    //phat am
    public static void speakVi(final String filePath) {
        initMediaPlayer(filePath);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopSpeakVi();
                Log.d("TienDH", "tts complete");
            }
        });
    }


    public static void downloadFile(final String URL, final String filePath) {
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

    public static int stateMediaPlayer;
    public static final int stateMP_Error = 0;
    public static final int stateMP_NotStarter = 1;
    public static MediaPlayer mediaPlayer;

    public static void initMediaPlayer(String path) {
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
}
