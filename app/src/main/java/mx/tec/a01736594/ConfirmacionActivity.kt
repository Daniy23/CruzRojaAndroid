package mx.tec.a01736594

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ConfirmacionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmacion)

        //Setup
        val bundle = intent.extras
        val email = bundle?.getString("email")
        setup(email ?:"")
    }

    private fun setup(email: String) {
        val emailTextView = findViewById<TextView>(R.id.emailTextView)
        emailTextView.text = email


    }

    fun loginAction(view: View?) {
        val intent = Intent(this@ConfirmacionActivity, LoginActivity::class.java)
        startActivity(intent)
    }
}