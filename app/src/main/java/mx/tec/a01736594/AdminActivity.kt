package mx.tec.a01736594

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AdminActivity : AppCompatActivity() {
    private lateinit var userRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        //userRecyclerView = findViewById(R.id.userRecyclerView)
        //userRecyclerView.layoutManager = LinearLayoutManager(this)

        // Aquí deberías recuperar la lista de usuarios pendientes y configurar el adaptador del RecyclerView
        // Puedes usar un ArrayList<User> para almacenar los usuarios pendientes y un adaptador personalizado para mostrarlos.
        // Luego, configura el adaptador en el RecyclerView.

        // Ejemplo de cómo configurar un adaptador (debes implementar tu propio adaptador):
        // val userAdapter = UserAdapter(userList) // userList es tu lista de usuarios pendientes
        // userRecyclerView.adapter = userAdapter
    }
}
