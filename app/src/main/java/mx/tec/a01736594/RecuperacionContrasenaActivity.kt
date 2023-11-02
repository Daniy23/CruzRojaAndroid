package mx.tec.a01736594

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RecuperacionContrasenaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperacioncontrasena)
    }

    fun recuperarAction(view: View){
        val auth = FirebaseAuth.getInstance()
        val emailEditText = (findViewById<View>(R.id.emailEditText3) as EditText)

        val email = emailEditText.text.toString()

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showAlert("éxito","Se ha enviado al correo las instrucciones") {
                        // Después de mostrar la alerta, inicia la actividad de inicio de sesión
                        val loginIntent = Intent(this, LoginActivity::class.java)
                        startActivity(loginIntent)
                    }
                } else {
                    showAlert("error","No se han podido enviar las instrucciones") {
                        // Después de mostrar la alerta, inicia la actividad de inicio de sesión
                        val loginIntent = Intent(this, LoginActivity::class.java)
                        startActivity(loginIntent)
                    }

                }
            }


    }

    private fun showAlert(errorTitle: String, errorMessage: String, function: () -> Unit) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(errorTitle)
        builder.setMessage(errorMessage)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}