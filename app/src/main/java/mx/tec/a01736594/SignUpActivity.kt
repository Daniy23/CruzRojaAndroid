package mx.tec.a01736594

import android.content.Intent

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.regex.Pattern

@Suppress("SpellCheckingInspection")
class SignUpActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
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
        val rolRef = db.collection("roles").document("admin")
        val estado = "pendiente"
        val emailEditText = (findViewById<View>(R.id.emailEditText2) as EditText)
        val passwordEditText = (findViewById<View>(R.id.passwordEditText2) as EditText)
        val nombreEditText = (findViewById<View>(R.id.nombreEditText) as EditText)
        val repasswordEditText = (findViewById<View>(R.id.repasswordEditText) as EditText)

        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        val nombre = nombreEditText.text.toString()
        val repassword = repasswordEditText.text.toString()

        // Definir una expresión regular para validar la contraseña
        val passwordPattern = "^(?=.*[0-9].*[0-9])(?=.*[!@#\$%^&*])(?=\\S+\$).{8,}\$"
        val passwordMatcher = Pattern.compile(passwordPattern).matcher(password)

        if (email.isNotEmpty() && password.isNotEmpty() ) {
            if(passwordMatcher.matches() ) {
                if(password == repassword) {
                    FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val user = FirebaseAuth.getInstance().currentUser
                                val uid = user?.uid
                                if (uid != null) {
                                    val userData = hashMapOf(
                                        "email" to email,
                                        "Nombre" to nombre,
                                        "rol" to rolRef,
                                        "estado" to estado
                                    )
                                    db.collection("users").document(uid)
                                        .set(userData)
                                        .addOnSuccessListener {
                                            showConfirmacion(email)
                                        }
                                        .addOnFailureListener { e ->
                                            showAlert("Error al guardar datos: ${e.message}")
                                        }
                                } else {
                                    showAlert("Error al obtener el ID de usuario")
                                }
                            } else {
                                showAlert("Error al registrar usuario: ${task.exception?.message}")
                            }
                        }
                } else {
                    showAlert( "Las contraseñas deben coincidir.")
                }
            } else {
                showAlert("La contraseña no cumple con los requisitos: debe tener al menos 8 caracteres, 2 números y un carácter especial.")
            }
        } else {
            showAlert("Los campos de correo electrónico y contraseña son obligatorios")
        }
    }

    private fun showAlert(errorMessage: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(errorMessage)
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