package mx.tec.a01736594;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class fotoperfil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fotoperfil);
    }
    public void regresarajustes(View v){
        Intent intent = new Intent(this, ajustes.class);
        startActivity(intent);
    }
}