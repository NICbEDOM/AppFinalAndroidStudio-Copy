package com.example.proyectoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import android.os.Bundle;

public class cursoHome extends AppCompatActivity {
    private ViewPager viewPager;
    private TextView home,estudiantes,novedades,eventos;
    private PagerViewAdapter pagerViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curso_home);

        home=findViewById(R.id.Home);
        estudiantes=findViewById(R.id.Estu);
        novedades=findViewById(R.id.Nov);
        eventos=findViewById(R.id.Even);
        viewPager=findViewById(R.id.FragmentContainer);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });

        estudiantes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });


        novedades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });

        eventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(3);
            }
        });

        pagerViewAdapter = new PagerViewAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerViewAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                onChangeTab(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void onChangeTab(int position) {

        if(position==0)
        {
            home.setTextSize(25);
            estudiantes.setTextSize(15);
            novedades.setTextSize(15);
            eventos.setTextSize(15);
        }
        if(position==1)
        {
            home.setTextSize(15);
            estudiantes.setTextSize(25);
            novedades.setTextSize(15);
            eventos.setTextSize(15);
        }
        if(position==2)
        {
            home.setTextSize(15);
            estudiantes.setTextSize(15);
            novedades.setTextSize(25);
            eventos.setTextSize(15);
        }
        if(position==3)
        {
            home.setTextSize(15);
            estudiantes.setTextSize(15);
            novedades.setTextSize(15);
            eventos.setTextSize(25);
        }


    }
}