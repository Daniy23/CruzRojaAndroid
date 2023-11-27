package mx.tec.a01736594;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * Activity to display the details of an service hour
 */
public class ServiceHourDetailsActivity extends AppCompatActivity {
    TextView tvServiceHourVolunteerName;
    TextView tvServiceHourVolunteerId;
    TextView tvServiceHourHours;
    TextView tvServiceHourEventName;
    ImageView ivServiceHourEvidence;

    FirebaseFirestore db;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_hour_details);

        // If no data was passed, return
        Intent intent = getIntent();
        if (intent == null) { finish(); }

        // Get the view elements
        tvServiceHourVolunteerName = findViewById(R.id.tvServiceHourVolunteerName);
        tvServiceHourVolunteerId = findViewById(R.id.tvServiceHourVolunteerId);
        tvServiceHourEventName = findViewById(R.id.tvServiceHourEventName);
        tvServiceHourHours = findViewById(R.id.tvServiceHourHours);
        db = FirebaseFirestore.getInstance();
        ivServiceHourEvidence = findViewById(R.id.ivServiceHourEvidence);
        mProgressDialog = new ProgressDialog(this);

        // Prepare the view elements data
        String serviceHourVolunteerName = intent.getStringExtra("serviceHourVolunteerName");
        String serviceHourVolunteerId = intent.getStringExtra("serviceHourVolunteerId");
        String serviceHourEventName = intent.getStringExtra("serviceHourEventName");
        String serviceHourHours = intent.getStringExtra("serviceHourHours");
        String serviceHourEvidence = intent.getStringExtra("serviceHourEvidence");

        // Set the service hour data to the view elements
        // Service hour volunteer name
        tvServiceHourVolunteerName.setText(serviceHourVolunteerName);
        
        // Service hour volunteer id
        tvServiceHourVolunteerId.setText(serviceHourVolunteerId);

        // Service hour hours
        tvServiceHourHours.setText(serviceHourHours);

        // Service hour event name
        tvServiceHourEventName.setText(serviceHourEventName);

        // Service hour evidence
        Glide.with(this).load(serviceHourEvidence).fitCenter().into(ivServiceHourEvidence);
    }

    /**
     * Prepare the service hour approval process
     * 
     * @param v The view that called the method
     */
    public void prepareServiceHourApprove(View v) {
        // Display a confirmation dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmación");
        builder.setMessage("¿Aprobar horas de servicio?");

        // Positive button (Yes)
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = getIntent();

                // Show a progress dialog while the service hour is being approved
                mProgressDialog.setTitle("Cargando");
                mProgressDialog.setMessage("Aprobando horas...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();

                // Get necessary data to approve the service hour
                String serviceHourId = intent.getStringExtra("serviceHourId");
                String serviceHourVolunteerId = intent.getStringExtra("serviceHourVolunteerId");
                int serviceHourHours = Integer.parseInt(intent.getStringExtra("serviceHourHours"));

                approveServiceHours(serviceHourVolunteerId, serviceHourId, serviceHourHours,
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Notify user of successfull approval
                                mProgressDialog.dismiss();
                                Toast.makeText(ServiceHourDetailsActivity.this, "Horas de servicio aprobadas exitosamente", Toast.LENGTH_SHORT)
                                        .show();

                                // Keep track of the user data in the app
                                Intent intent = getIntent();
                                Intent newIntent = new Intent(ServiceHourDetailsActivity.this, horasservicio.class);
                                newIntent.putExtra("userId", intent.getStringExtra("userId"));
                                newIntent.putExtra("userEmail", intent.getStringExtra("userEmail"));
                                newIntent.putExtra("userName", intent.getStringExtra("userName"));

                                // Redirect to the service hour list activity
                                startActivity(newIntent);
                            }
                        },
                        // Handle error if the service hour approval fails
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Notify user of error
                                mProgressDialog.dismiss();
                                Toast.makeText(ServiceHourDetailsActivity.this, "Error al aprobar las horas de servicio",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        // Negative button (No)
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
            }
        });

        // Show the dialog
        builder.show();
    }

    /**
     * Approve the volunteer's hours
     * 
     * @param volunteerId The id of the volunteer
     * @param serviceHourId The id of the service hour
     * @param serviceHoursToApprove The number of hours to approve
     * @param onSuccessListener Listener for the success event
     * @param onFailureListener Listener for the failure event
     */
    private void approveServiceHours(String serviceHourVolunteerId, String serviceHourId,
                                       int serviceHourHours,
                                       OnSuccessListener<Void> onSuccessListener,
                                       OnFailureListener onFailureListener) {
        // Map object to update the fields
        Map<String, Object> updates = new HashMap<>();
        updates.put("estatus", "aprobadas");
        updates.put("aprobadas", true);
        
        // Update the "estatus" and "aprobadas" fields of the service hour
        db.collection("horasVoluntarios")
                .document(serviceHourId).update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Update the volunteer's "hrsAcumuladas" field
                        // (add the service hours to the total hours of the volunteer)
                        db.collection("users")
                                .document(serviceHourVolunteerId).update("hrsAcumuladas", FieldValue.increment(serviceHourHours))
                                .addOnSuccessListener(onSuccessListener)
                                .addOnFailureListener(onFailureListener);
                    }
                })
                .addOnFailureListener(onFailureListener);
    }

    /**
     * Prepare the service hour decline process
     * 
     * @param v The view that called the method
     */
    public void prepareServiceHourDecline(View v) {
        // Display a confirmation dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmación");
        builder.setMessage("¿Rechazar horas de servicio?");

        // Positive button (Yes)
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = getIntent();

                // Show a progress dialog while the service hour is being declined
                mProgressDialog.setTitle("Cargando");
                mProgressDialog.setMessage("Rechazando horas...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();

                // Get necessary data to decline the service hour
                String serviceHourId = intent.getStringExtra("serviceHourId");
                String serviceHourVolunteerId = intent.getStringExtra("serviceHourVolunteerId");
                int serviceHourHours = Integer.parseInt(intent.getStringExtra("serviceHourHours"));

                declineServiceHours(serviceHourVolunteerId, serviceHourId, serviceHourHours,
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Notify user of successfull decline
                                mProgressDialog.dismiss();
                                Toast.makeText(ServiceHourDetailsActivity.this, "Horas de servicio rechazadas exitosamente", Toast.LENGTH_SHORT)
                                        .show();

                                // Keep track of the user data in the app
                                Intent intent = getIntent();
                                Intent newIntent = new Intent(ServiceHourDetailsActivity.this, horasservicio.class);
                                newIntent.putExtra("userId", intent.getStringExtra("userId"));
                                newIntent.putExtra("userEmail", intent.getStringExtra("userEmail"));
                                newIntent.putExtra("userName", intent.getStringExtra("userName"));

                                // Redirect to the service hour list activity
                                startActivity(newIntent);
                            }
                        },
                        // Handle error if the service hour decline fails
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Notify user of error
                                mProgressDialog.dismiss();
                                Toast.makeText(ServiceHourDetailsActivity.this, "Error al rechazar las horas de servicio",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        // Negative button (No)
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
            }
        });

        // Show the dialog
        builder.show();

    }

    /**
     * Decline the volunteer's hours
     * 
     * @param volunteerId The id of the volunteer
     * @param serviceHourId The id of the service hour
     * @param serviceHoursToApprove The number of hours to approve
     * @param onSuccessListener Listener for the success event
     * @param onFailureListener Listener for the failure event
     */
    private void declineServiceHours(String serviceHourVolunteerId, String serviceHourId,
                                       int serviceHourHours,
                                       OnSuccessListener<Void> onSuccessListener,
                                       OnFailureListener onFailureListener) {
        // Map object to update the fields
        Map<String, Object> updates = new HashMap<>();
        updates.put("estatus", "rechazadas");
        
        // Update the "estatus" field of the service hour
        db.collection("horasVoluntarios")
                .document(serviceHourId).update(updates)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);

    }
}
