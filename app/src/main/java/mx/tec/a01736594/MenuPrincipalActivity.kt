package mx.tec.a01736594

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MenuPrincipalActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menuprincipal)


    }

    fun guardarAction(view: View?){
        val bundle = intent.extras
        val email = bundle?.getString("email")
        val direccion = (findViewById<View>(R.id.direccionTextView) as EditText).text.toString()
        val phone = (findViewById<View>(R.id.telefonoTextView) as EditText).text.toString()
        val rol = "admin"

        val userData = hashMapOf(
            "email" to email,
            "address" to direccion,
            "phone" to phone,
            "rol" to rol
        )

        // Accede a la colección "users" y utiliza el email como ID del documento
        if (email != null) {
            db.collection("users")
                .document(email)
                .set(userData)
                .addOnSuccessListener {
                    // Los datos se guardaron con éxito
                }
                .addOnFailureListener { e ->
                    // Hubo un error al guardar los datos
                    Log.e("MenuPrincipalActivity", "Error al guardar datos: $e")
                }
        }
    }

    fun recuperarAction(view: View?){
        val bundle = intent.extras
        val email = bundle?.getString("email")
        val direccion = (findViewById<View>(R.id.direccionTextView) as EditText)
        val phone = (findViewById<View>(R.id.telefonoTextView) as EditText)

        db.collection("users").document(email ?: "").get().addOnSuccessListener {
            direccion.setText(it.get("address") as String?)
            phone.setText(it.get("phone") as String?)
        }

    }

    fun eliminarAction(view: View?){
        val bundle = intent.extras
        val email = bundle?.getString("email")
        db.collection("users").document(email ?: "").delete()
    }

    fun logOutAction(view: View?){
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    fun activityAdminAction(view: View?){
        val intent = Intent(this, AdminActivity::class.java)
        startActivity(intent)
    }

}