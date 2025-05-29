package com.example.smartgarden;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class HelpActivity extends AppCompatActivity {

    private ScrollView scrollView;
    private CardView cardFitur, cardPertanyaan, cardTips;
    private ImageView btnBack;

    private boolean hasAnimatedFitur = false;
    private boolean hasAnimatedPertanyaan = false;
    private boolean hasAnimatedTips = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        // Inisialisasi view
        btnBack = findViewById(R.id.btnBack);
        scrollView = findViewById(R.id.scrollView);
        cardFitur = findViewById(R.id.cardFitur);
        cardPertanyaan = findViewById(R.id.cardPertanyaan);
        cardTips = findViewById(R.id.cardTips);

        // Tombol kembali
        btnBack.setOnClickListener(v -> onBackPressed());

        // Listener untuk animasi saat scroll
        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            if (isViewVisible(cardFitur) && !hasAnimatedFitur) {
                animateCard(cardFitur);
                hasAnimatedFitur = true;
            }
            if (isViewVisible(cardPertanyaan) && !hasAnimatedPertanyaan) {
                animateCard(cardPertanyaan);
                hasAnimatedPertanyaan = true;
            }
            if (isViewVisible(cardTips) && !hasAnimatedTips) {
                animateCard(cardTips);
                hasAnimatedTips = true;
            }
        });
    }

    private boolean isViewVisible(View view) {
        Rect scrollBounds = new Rect();
        scrollView.getHitRect(scrollBounds);
        return view.getLocalVisibleRect(scrollBounds);
    }

    private void animateCard(View view) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_slide_up);
        view.startAnimation(animation);
    }
}
