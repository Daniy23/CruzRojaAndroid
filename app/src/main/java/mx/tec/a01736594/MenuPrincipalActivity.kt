package mx.tec.a01736594

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MenuPrincipalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menuprincipal)
    }

    fun logOutAction(view: View?){
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }
}