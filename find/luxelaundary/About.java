package com.find.luxelaundary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.find.luxelaundary.User.Homescreen_user;

public class About extends AppCompatActivity {
ImageView arrowback;
Button followButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_about);

        arrowback=findViewById(R.id.arrowback);
        followButton=findViewById(R.id.followButton);

        arrowback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back=new Intent(About.this, Homescreen_user.class);
                startActivity(back);            }
        });
    }
}