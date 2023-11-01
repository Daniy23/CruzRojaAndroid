package mx.tec.a01736594

import android.content.Intent
import android.util.Log;

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Analytics Event
        val analytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message", "Integración de Firebase Completa")
        analytics.logEvent("SignUpScreen", bundle)
    }

    fun signUpAction(view: View?) {

        val email = (findViewById<View>(R.id.emailEditText2) as EditText).text.toString()
        val password = (findViewById<View>(R.id.passwordEditText2) as EditText).text.toString()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful){
                        showConfirmacion(it.result?.user?.email ?:"")
                    } else {
                        showAlert()
                    }
                }

        }
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se a producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showConfirmacion(email: String){
        val viewIntent = Intent(this, ConfirmacionActivity::class.java).apply{
            putExtra("email", email)
        }

        startActivity(viewIntent)
    }

    fun loginView(view: View?) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

}