package mx.tec.a01736594;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ajustes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);
    }
    public void go(View v){
        Intent intent = new Intent(this, menuprincipal.class);
        startActivity(intent);
    }
    public void nombreusuario(View v){
        Intent intent = new Intent(this, nombreusuario.class);
        startActivity(intent);
    }
    public void fotoperfil(View v){
        Intent intent = new Intent(this, fotoperfil.class);
        startActivity(intent);
    }
    public void correo(View v){
        Intent intent = new Intent(this, correoelectronico.class);
        startActivity(intent);
    }
    public void contrasena(View v){
        Intent intent = new Intent(this, contrasena.class);
        startActivity(intent);
    }
    public void aprobarusuario(View v){
        Intent intent = new Intent(this, aprobarusuarios.class);
        startActivity(intent);
    }
}