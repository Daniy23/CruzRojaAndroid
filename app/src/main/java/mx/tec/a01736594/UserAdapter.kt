package mx.tec.a01736594

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class UserAdapter(private val userList: List<User>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private val db  = FirebaseFirestore.getInstance()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val mailTextView: TextView = itemView.findViewById(R.id.mailTextView)
        private val approveButton: Button = itemView.findViewById(R.id.aprroveButton)

        fun bind(user: User) {
            nameTextView.text = user.nombre
            mailTextView.text = user.email

            approveButton.setOnClickListener {
                val userDocumentref = db.collection("users").document(user.userId)

                userDocumentref.update("estado", "aprobado")
                    .addOnSuccessListener {

                        val originalIntent = (itemView.context as Activity).intent
                        val userId = originalIntent.getStringExtra("userId")
                        val userEmail = originalIntent.getStringExtra("userEmail")
                        val userName = originalIntent.getStringExtra("userName")

                        Toast.makeText(itemView.context, "Usuario aprobado exitosamente", Toast.LENGTH_SHORT).show()

                        // Keep track of the user data in the app
                        val newIntent = Intent(itemView.context, AdminActivity::class.java).apply {
                            putExtra("userId", userId)
                            putExtra("userEmail", userEmail)
                            putExtra("userName", userName)
                        }

                        // Redirect to the main menu activity
                        itemView.context.startActivity(newIntent)
                    }
            }
        }
    }

}