package com.infinity.icook;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.infinity.data.Var;
import com.infinity.model.DishItem;

public class DishDetailActivity extends AppCompatActivity {

    private DishItem dish;
    private TextView title, description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_detail);

        dish = (DishItem) getIntent().getSerializableExtra(Var.DISH_EXTRA);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        title = (TextView) findViewById(R.id.title);
        description = (TextView) findViewById(R.id.description);

        title.setText(dish.getName());
        description.setText(dish.getIntroduce());

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(dish.getName());

        loadBackdrop();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void loadBackdrop() {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(dish.getImage()).centerCrop().into(imageView);
    }
}
