package mx.tec.a01736594;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void go(View v){
        Intent intent = new Intent(this, menuprincipal.class);
        startActivity(intent);
    }
    public void logingo(View v){
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
    }
    public void signupgo(View v){
        Intent intent = new Intent(this, signup.class);
        startActivity(intent);
    }
}