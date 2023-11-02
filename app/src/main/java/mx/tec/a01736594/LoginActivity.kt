package mx.tec.a01736594

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Administrador)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun logInAction(view: View) {
        val email = (findViewById<View>(R.id.emailEditText) as EditText).text.toString()
        val password = (findViewById<View>(R.id.passwordEditText) as EditText).text.toString()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password).addOnCompleteListener {task ->
                    if (task.isSuccessful){

                        val user = task.result?.user

                        if (user != null) {
                            verifyUserRoleAndState(user)

                        } else {
                            showAlert("Usuario nulo")
                        }
                    } else {
                        showAlert("Credenciales inválidas o usuario no aprobado")
                    }
                }
        }
    }

    private fun verifyUserRoleAndState(user: FirebaseUser){
        val userRef = db.collection("users").document(user.uid)

        userRef.get().addOnSuccessListener { document ->
            if(document.exists()) {
                val userData = document.data
                val userState = userData?.get("estado") as String
                val rolRef = userData["rol"] as DocumentReference

                rolRef.get().addOnSuccessListener { rolDocument ->
                    if(rolDocument.exists()) {
                        val rolData = rolDocument.data

                        val userRole = rolData?.get("id") as String


                        if ((userRole == "admin")) {
                            if (userState == "aprobado") {
                                showView(user.email ?: "")
                            } else {
                                showAlert("Aún no has sido aprobado")
                            }
                        } else {
                            showAlert("No eres un administrador")
                        }
                     }
                }
            } else {
                showAlert("Credenciales inválidas")
            }
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

    private fun showView(email: String){
        val viewIntent = Intent(this, MenuPrincipalActivity::class.java).apply{
            putExtra("email", email)
        }

        startActivity(viewIntent)
    }

    fun showViewRecuperar(view: View?){
        val viewIntent = Intent(this, RecuperacionContrasenaActivity::class.java)

        startActivity(viewIntent)
    }
}