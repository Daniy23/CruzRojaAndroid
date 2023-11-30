package mx.tec.a01736594

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class AdminActivity : AppCompatActivity() {
    private val db  = FirebaseFirestore.getInstance()
    private lateinit var userRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        userRecyclerView = findViewById(R.id.userRecyclerView)
        userRecyclerView.layoutManager = LinearLayoutManager(this)

        // Aquí deberías recuperar la lista de usuarios pendientes y configurar el adaptador del RecyclerView
        // Puedes usar un ArrayList<User> para almacenar los usuarios pendientes y un adaptador personalizado para mostrarlos.
        // Luego, configura el adaptador en el RecyclerView.
        val solicitudRegistroRef = db.collection("users")

        solicitudRegistroRef.whereEqualTo("estado", "pendiente")
            .get()
            .addOnSuccessListener { documents ->
                val userList = ArrayList<User>()

                for (document in documents) {
                    val userData = document.data
                    val nombre = userData["Nombre"] as String
                    val email = userData["email"] as String
                    val estado = userData["estado"] as String
                    val userId = document.id

                    val user = User(userId,nombre, email, estado)
                    userList.add(user)
                }

                val userAdapter = UserAdapter(userList)
                userRecyclerView.adapter = userAdapter
            }
            .addOnFailureListener {_ ->

            }

        // Ejemplo de cómo configurar un adaptador (debes implementar tu propio adaptador):
        // val userAdapter = UserAdapter(userList) // userList es tu lista de usuarios pendientes
        // userRecyclerView.adapter = userAdapter
    }
    fun go(v: View?) {
        // Keep track of the user data in the app
        val intent = intent
        val newIntent = Intent(this, MenuPrincipalActivity::class.java).apply {
            putExtra("userId", intent.getStringExtra("userId"))
            putExtra("userEmail", intent.getStringExtra("userEmail"))
            putExtra("userName", intent.getStringExtra("userName"))
        }

        // Redirect to the main menu activity
        startActivity(newIntent)
    }
}
