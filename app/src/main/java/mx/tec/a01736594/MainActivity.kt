package mx.tec.a01736594

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Administrador)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val loginBtn = findViewById<Button>(R.id.login)
        val analytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message", "Integración de Firebase completa")
        analytics.logEvent("InitScreen", bundle)
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