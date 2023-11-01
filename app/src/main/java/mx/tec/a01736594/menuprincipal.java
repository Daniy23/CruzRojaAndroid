package mx.tec.a01736594;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class menuprincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuprincipal);
    }
    public void dashboard(View v){
        Intent intent = new Intent(this, dashboard.class);
        startActivity(intent);
    }
    public void ajustes(View v){
        Intent intent = new Intent(this, ajustes.class);
        startActivity(intent);
    }
    public void anuncio(View v){
        Intent intent = new Intent(this, anuncios.class);
        startActivity(intent);
    }
    public void horasservicio(View v){
        Intent intent = new Intent(this, horasservicio.class);
        startActivity(intent);
    }
    public void login(View v){
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
    }

}