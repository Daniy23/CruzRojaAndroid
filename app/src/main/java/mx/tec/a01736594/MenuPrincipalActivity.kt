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
        val intent = intent
        if (intent == null) {
            finish()
        }
    }

    fun dashboard(v: View?) {
        // Keep track of the user data in the app
        val intent = intent
        val newIntent = Intent(this, dashboard::class.java).apply {
            putExtra("userId", intent.getStringExtra("id"))
            putExtra("userEmail", intent.getStringExtra("email"))
            putExtra("userName", intent.getStringExtra("name"))
        }

        // Redirect to the ads list activity
        startActivity(newIntent)
    }

    fun ajustes(v: View?) {
        val intent = Intent(this, AdminActivity::class.java)
        startActivity(intent)
    }

    /**
     * Open the activity of ads display
     * 
     * @param v The view that called the method
     */
    fun anuncio(v: View?) {
        // Keep track of the user data in the app
        val intent = intent
        val newIntent = Intent(this, anuncios::class.java).apply {
            putExtra("userId", intent.getStringExtra("id"))
            putExtra("userEmail", intent.getStringExtra("email"))
            putExtra("userName", intent.getStringExtra("name"))
        }

        // Redirect to the ads list activity
        startActivity(newIntent)
    }

    fun horasservicio(v: View?) {
        val intent = Intent(this, horasservicio::class.java)
        startActivity(intent)
    }

    fun login(v: View?) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    /*
    fun guardarAction(view: View?){
        val bundle = intent.extras
        val email = bundle?.getString("email")
        val rol = "admin"

        val userData = hashMapOf(
            "email" to email,
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

*/
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
