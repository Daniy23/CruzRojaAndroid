package mx.tec.a01736594;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Administrador);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button loginBtn = findViewById(R.id.login);

        FirebaseAnalytics analytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString("message", "Integración de Firebase completa");
        analytics.logEvent("InitScreen", bundle);
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