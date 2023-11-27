package mx.tec.a01736594;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class dashboard extends AppCompatActivity implements DashboardUserAdapter.OnDashboardUserDeleteListener {
    private RecyclerView recyclerView;
    private DashboardUserAdapter dashboardUserAdapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressDialog mProgressDialog;
    List<DashboardUser> dashboardUserList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // If no data was passed, return
        Intent intent = getIntent();
        if (intent == null) { finish(); }

        // Get the view elements
        recyclerView = findViewById(R.id.rvDashboardUserList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mProgressDialog = new ProgressDialog(this);

        // Prepare the view elements data (get volunteer users from the database)
        CollectionReference dashboardUsersRef = db.collection("users");
        DocumentReference roleRef = db.document("roles/voluntario");
        Query query = dashboardUsersRef.whereEqualTo("rol", roleRef);

        // Get results from the query
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                dashboardUserList = new ArrayList<>();

                // Iterate over the results of the query
                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                    // Convert the document to an "DashboardUser" object
                    DashboardUser dashboardUser = document.toObject(DashboardUser.class);
                    dashboardUserList.add(dashboardUser);
                }
                // Create an adapter to display the volunteer users in the RecyclerView
                dashboardUserAdapter = new DashboardUserAdapter(dashboardUserList, dashboard.this);
                recyclerView.setAdapter(dashboardUserAdapter);
            }
            // Handle error if the query fails
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NotNull Exception e) {
                // Notify user of error
                Toast.makeText(dashboard.this, "Error al visualizar el dashboard",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDashboardUserDelete(int position) {
        // Do nothing
    }

        
    public void go(View v){
        Intent intent = new Intent(this, MenuPrincipalActivity.class);
        startActivity(intent);
    }
}
