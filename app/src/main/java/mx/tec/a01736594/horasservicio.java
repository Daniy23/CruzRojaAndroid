package mx.tec.a01736594;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * Activity to display the social service hours requests
 */
public class horasservicio extends AppCompatActivity implements ServiceHourAdapter.OnServiceHourDeleteListener {
    private RecyclerView recyclerView;
    private ServiceHourAdapter serviceHourAdapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressDialog mProgressDialog;
    List<ServiceHour> serviceHourList;
    ServiceHour serviceHour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horasservicio);

        // If no data was passed, return
        Intent intent = getIntent();
        if (intent == null) { finish(); }

        // Get the view elements
        recyclerView = findViewById(R.id.rvServiceHourList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mProgressDialog = new ProgressDialog(this);

        // Prepare the view elements data (get volunteer users from the database)
        CollectionReference serviceHoursRef = db.collection("horasVoluntarios");
        String requiredStatus = "pendiente";
        Query query = serviceHoursRef.whereEqualTo("estatus", requiredStatus);

        // Get results from the query
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                serviceHourList = new ArrayList<>();

                // Iterate over the results of the query
                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                    // Convert the document to an "DashboardUser" object
                    serviceHour = document.toObject(ServiceHour.class);
                    serviceHour.setUid(document.getId());
                    // Retrieve the author name from the database
                    serviceHourList.add(serviceHour);
                }
                // Create an adapter to display the volunteer users in the RecyclerView
                serviceHourAdapter = new ServiceHourAdapter(serviceHourList, horasservicio.this);
                recyclerView.setAdapter(serviceHourAdapter);
            }
            // Handle error if the query fails
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NotNull Exception e) {
                // Notify user of error
                Toast.makeText(horasservicio.this, "Error al visualizar las solicitudes de horas de servicio",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onServiceHourDelete(int position) {
        // Do nothing
    }
    public void go(View v){
        Intent intent = new Intent(this, MenuPrincipalActivity.class);
        startActivity(intent);
    }
}
