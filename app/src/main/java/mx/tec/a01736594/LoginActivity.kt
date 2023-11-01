package mx.tec.a01736594

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Administrador)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun logInAction(view: View?) {
        val email = (findViewById<View>(R.id.emailEditText) as EditText).text.toString()
        val password = (findViewById<View>(R.id.passwordEditText) as EditText).text.toString()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful){
                        showView(it.result?.user?.email ?:"")
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

    private fun showView(email: String){
        val viewIntent = Intent(this, MenuPrincipalActivity::class.java).apply{
            putExtra("email", email)
        }

        startActivity(viewIntent)
    }
}