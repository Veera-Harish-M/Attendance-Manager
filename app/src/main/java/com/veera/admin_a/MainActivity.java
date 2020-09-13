package com.veera.admin_a;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar,toolbartab;
    ViewPager viewPager;
    TabLayout tabLayout;
    PageAdapter pageAdapter;

    MenuItem logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=(Toolbar) findViewById(R.id.toolbar);
        toolbartab=(Toolbar)findViewById(R.id.toolbartab);
        viewPager=(ViewPager)findViewById(R.id.viewpager);
        tabLayout=(TabLayout)findViewById(R.id.tablayout);
        logout=(MenuItem) findViewById(R.id.action_Logout);

        setSupportActionBar(toolbar);

        viewPager.setPageTransformer(true,new DepthPageTransformer());

        pageAdapter=new PageAdapter(getSupportFragmentManager());
        pageAdapter.addFragment(new AttendanceFragment(),"Attendance");
        pageAdapter.addFragment(new InputFragment(),"Input");
        pageAdapter.addFragment(new TrackFragment(),"Track");

        viewPager.setAdapter(pageAdapter);
        tabLayout.setupWithViewPager(viewPager);


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.clear();
                myEdit.commit();


                Intent i=new Intent(MainActivity.this,login.class);
                startActivity(i);
                finish();

                return false;
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

}
