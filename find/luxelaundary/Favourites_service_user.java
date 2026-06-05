package com.find.luxelaundary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.find.luxelaundary.User.Homescreen_user;

public class Favourites_service_user extends AppCompatActivity {
    ImageView back;
    ImageView delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favourites_service_user);

        back=findViewById(R.id.back);
        delete=findViewById(R.id.delete);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back=new Intent(Favourites_service_user.this, Homescreen_user.class);
                startActivity(back);            }
        });




    }
}