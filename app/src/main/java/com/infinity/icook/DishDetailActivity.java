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
}
