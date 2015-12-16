package com.infinity.icook;

import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;

public class Intro extends AppIntro {
    @Override
    public void init(Bundle savedInstanceState) {
        addSlide(SampleSlide.newInstance(R.layout.test_intro));
        addSlide(SampleSlide.newInstance(R.layout.test_intro2));
        addSlide(SampleSlide.newInstance(R.layout.test_intro3));
        addSlide(SampleSlide.newInstance(R.layout.test_intro4));
        addSlide(SampleSlide.newInstance(R.layout.test_intro5));
        addSlide(SampleSlide.newInstance(R.layout.test_intro6));
        addSlide(SampleSlide.newInstance(R.layout.test_intro7));
        addSlide(SampleSlide.newInstance(R.layout.test_intro8));
    }


    @Override
    public void onSkipPressed() {
        finish();
    }

    @Override
    public void onDonePressed() {
        finish();
    }
}
