package mx.tec.a01736594;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Adapter for the RecyclerView of service hours
 */
public class ServiceHourAdapter extends RecyclerView.Adapter<ServiceHourAdapter
        .ServiceHourViewHolder> {
    private List<ServiceHour> serviceHours;
    private static OnServiceHourDeleteListener onServiceHourDeleteListener;
    private View.OnClickListener onAcceptClickListener;
    private View.OnClickListener onDeclineClickListener;

    /**
     * Initialize an ServiceHourAdapter object with parameters
     * 
     * @param serviceHours List of service hours
     * @param onServiceHourDeleteListener Listener for the delete event
     */
    public ServiceHourAdapter(List<ServiceHour> serviceHours,
                              OnServiceHourDeleteListener onServiceHourDeleteListener) {
        this.serviceHours = serviceHours;
        this.onServiceHourDeleteListener = onServiceHourDeleteListener;
    }

    @NonNull
    @Override
    public ServiceHourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout of the service hour item
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service_hour,
                parent, false);
        return new ServiceHourViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceHourViewHolder holder, int position) {
        // Get the service hour at the current position
        ServiceHour serviceHour = serviceHours.get(position);

        // Bind the service hour to the view holder
        holder.bind(serviceHour);

        // Set the click listener for the service hour item and redirect to the
        // service hour details activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get the service hour data
                String serviceHourId = serviceHour.getUid();
                String serviceHourVolunteerName = serviceHour.getVolunteerName();
                String serviceHourVolunteerId = serviceHour.getVolunteerId();
                String serviceHourHours = Integer.toString(serviceHour.getHours());
                String serviceHourEventName = serviceHour.getEventTitle();
                String serviceHourEvidence = serviceHour.getEvidence();

                // Keep track of the user data in the app
                Intent intent = ((horasservicio) view.getContext()).getIntent();
                Intent newIntent = new Intent(view.getContext(), ServiceHourDetailsActivity.class);
                newIntent.putExtra("userId", intent.getStringExtra("userId"));
                newIntent.putExtra("userEmail",
                        intent.getStringExtra("userEmail"));
                newIntent.putExtra("userName",
                        intent.getStringExtra("userName"));
                newIntent.putExtra("serviceHourId", serviceHourId);
                newIntent.putExtra("serviceHourVolunteerName", serviceHourVolunteerName);
                newIntent.putExtra("serviceHourVolunteerId", serviceHourVolunteerId);
                newIntent.putExtra("serviceHourHours", serviceHourHours);
                newIntent.putExtra("serviceHourEventName", serviceHourEventName);
                newIntent.putExtra("serviceHourEvidence", serviceHourEvidence);

                // Redirect to the service hour details activity
                view.getContext().startActivity(newIntent);
            }
        });

        // Set the click listener for the approval button and approve the service hour
        holder.btnApproveListServiceHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayConfirmationDialog(view.getContext(), "¿Aprobar horas de servicio?",
                        new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Show a progress dialog while the service hour is being approved
                        ProgressDialog progressDialog = new ProgressDialog(view.getContext());
                        progressDialog.setTitle("Cargando");
                        progressDialog.setMessage("Aprobando horas...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        // Get the necessary data to approve the service hour
                        String volunteerId = serviceHour.getVolunteerId();
                        String serviceHourId = serviceHour.getUid();
                        int serviceHoursToApprove = serviceHour.getHours();

                        // Call the function to approve the volunteer's hours
                        approveServiceHours(volunteerId, serviceHourId, serviceHoursToApprove,
                            new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Notify user of successfull approval
                                    progressDialog.dismiss();
                                    Toast.makeText(view.getContext(),
                                            "Horas de servicio aprobadas exitosamente",
                                            Toast.LENGTH_SHORT).show();

                                    // Keep track of the user data in the app
                                    Intent intent = ((horasservicio) view.getContext()).getIntent();
                                    Intent newIntent = new Intent(view.getContext(),
                                            horasservicio.class);
                                    newIntent.putExtra("userId",
                                            intent.getStringExtra("userId"));
                                    newIntent.putExtra("userEmail",
                                            intent.getStringExtra("userEmail"));
                                    newIntent.putExtra("userName",
                                            intent.getStringExtra("userName"));

                                    // Redirect to the service hour list activity
                                    view.getContext().startActivity(newIntent);
                                }
                            },
                            // // Handle error if the service hour approval fails
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Notify user of error
                                    progressDialog.dismiss();
                                    Toast.makeText(view.getContext(),
                                            "Error al aprobar las horas de servicio",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                    }
                });
            }
        });

        // Set the click listener for the decline button and decline the service hour
        holder.btnDeclineListServiceHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayConfirmationDialog(view.getContext(),
                        "¿Rechazar horas de servicio?",
                        new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Show a progress dialog while the service hour is being declined
                        ProgressDialog progressDialog = new ProgressDialog(view.getContext());
                        progressDialog.setTitle("Cargando");
                        progressDialog.setMessage("Rechazando horas...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        // Get the necessary data to decline the service hour
                        String volunteerId = serviceHour.getVolunteerId();
                        String serviceHourId = serviceHour.getUid();
                        int serviceHoursToApprove = serviceHour.getHours();

                        // Call the function to decline the volunteer's hours
                        declineServiceHours(volunteerId, serviceHourId, serviceHoursToApprove,
                            new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Notify user of successfull decline
                                    progressDialog.dismiss();
                                    Toast.makeText(view.getContext(),
                                            "Horas de servicio rechazadas exitosamente",
                                            Toast.LENGTH_SHORT).show();

                                    // Keep track of the user data in the app
                                    Intent intent = ((horasservicio) view.getContext()).getIntent();
                                    Intent newIntent = new Intent(view.getContext(),
                                            horasservicio.class);
                                    newIntent.putExtra("userId",
                                            intent.getStringExtra("userId"));
                                    newIntent.putExtra("userEmail",
                                            intent.getStringExtra("userEmail"));
                                    newIntent.putExtra("userName",
                                            intent.getStringExtra("userName"));

                                    // Redirect to the service hour list activity
                                    view.getContext().startActivity(newIntent);
                                }
                            },
                            // Handle error if the service hour decline fails
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Notify user of error
                                    progressDialog.dismiss();
                                    Toast.makeText(view.getContext(),
                                            "Error al rechazar las horas de servicio",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                    }
                });
            }
        });
    }

    /**
     * Display a confirmation dialog
     * 
     * @param context The context of the dialog
     * @param message The message to display
     * @param listener The listener for the positive button
     */
    private void displayConfirmationDialog(Context context, String message,
                                           DialogInterface.OnClickListener listener) {
        // Display a confirmation dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirmación");
        builder.setMessage(message);
        builder.setPositiveButton("Sí", listener);
        builder.setNegativeButton("No", null);
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
    private void approveServiceHours(String volunteerId, String serviceHourId,
                                        int serviceHoursToApprove,
                                        OnSuccessListener<Void> onSuccessListener,
                                        OnFailureListener onFailureListener) {
        // Find the corresponding service hour in the database
        DocumentReference serviceHourRef = FirebaseFirestore.getInstance()
                .collection("horasVoluntarios")
                .document(serviceHourId);
        
        // Map object to update the fields
        Map<String, Object> updates = new HashMap<>();
        updates.put("estatus", "aprobadas");
        updates.put("aprobadas", true);

        // Update the "estatus" and "aprobadas" fields of the service hour
        serviceHourRef.update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Find the corresponding volunteer in the database
                DocumentReference volunteerRef = FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(volunteerId);

                // Update the volunteer's "hrsAcumuladas" field
                // (add the service hours to the total hours of the volunteer)
                volunteerRef.update("hrsAcumuladas",
                                FieldValue.increment(serviceHoursToApprove))
                        .addOnSuccessListener(onSuccessListener)
                        .addOnFailureListener(onFailureListener);
            }
        }).addOnFailureListener(onFailureListener);
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
    private void declineServiceHours(String volunteerId, String serviceHourId,
                                        int serviceHoursToApprove,
                                        OnSuccessListener<Void> onSuccessListener,
                                        OnFailureListener onFailureListener) {
        // Find the corresponding service hour in the database
        DocumentReference serviceHourRef = FirebaseFirestore.getInstance()
                .collection("horasVoluntarios")
                .document(serviceHourId);

        // Map object to update the fields
        Map<String, Object> updates = new HashMap<>();
        updates.put("estatus", "rechazadas");

        // Update the "estatus" field of the service hour
        serviceHourRef.update(updates)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    @Override
    public int getItemCount() {
        return serviceHours.size();
    }

    /*
     * Interface for the delete event
     */
    public interface OnServiceHourDeleteListener {
        void onServiceHourDelete(int position);
    }

    /**
     * Set the listener for the delete event
     * 
     * @param listener The listener for the delete event
     */
    public void setonAcceptClickListener(View.OnClickListener listener) {
        onAcceptClickListener = listener;
    }

    /**
     * ViewHolder for the service hour item
     */
    static class ServiceHourViewHolder extends RecyclerView.ViewHolder {
        TextView tvServiceHourVolunteerName;
        TextView tvServiceHourAuthorId;
        TextView tvServiceHourEventHours;
        TextView tvServiceHourEventName;
        Button btnApproveListServiceHour;
        Button btnDeclineListServiceHour;

        /**
         * Initialize a view holder for the service hour item
         * 
         * @param itemView The view of the service hour item
         */
        ServiceHourViewHolder(@NonNull View itemView) {
            super(itemView);

            // Get elements from the view of the service hour item
            tvServiceHourVolunteerName = itemView.findViewById(R.id.tvServiceHourVolunteerName);
            tvServiceHourAuthorId = itemView.findViewById(R.id.tvServiceHourVolunteerId);
            tvServiceHourEventName = itemView.findViewById(R.id.tvServiceHourEventName);
            tvServiceHourEventHours = itemView.findViewById(R.id.tvServiceHourEventHours);
            btnApproveListServiceHour = itemView.findViewById(R.id.btnApproveListServiceHour);
            btnDeclineListServiceHour = itemView.findViewById(R.id.btnDeclineListServiceHour);
        }

        /**
         * Bind the service hour data to the service hour item view
         */
        void bind(ServiceHour serviceHour) {
            // Service hour volunteer name
            serviceHour.getVolunteerName(serviceHour.getVolunteerId(),
                    new OnSuccessListener<String>() {
                @Override
                public void onSuccess(String volunteerName) {
                    // Set the service hour volunteer name
                    tvServiceHourVolunteerName.setText(volunteerName);
                    serviceHour.setVolunteerName(volunteerName);
                }
            // Handle error if the volunteer name could not be retrieved
            }, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Notify user of error
                    Toast.makeText(((horasservicio) itemView.getContext()),
                        "Error al mostrar el nombre del voluntario",
                        Toast.LENGTH_SHORT).show();
                }
            });

            // Service hour volunteer id
            tvServiceHourAuthorId.setText(serviceHour.getVolunteerId());

            // Service hour event hours
            tvServiceHourEventHours.setText(String.valueOf(serviceHour.getHours()));

            // Service hour event name
            serviceHour.getEventTitle(serviceHour.getEvent(), new OnSuccessListener<String>() {
                @Override
                public void onSuccess(String eventName) {
                    // Set the service hour event name
                    tvServiceHourEventName.setText(eventName);
                    serviceHour.setEventTitle(eventName);
                }
            // Handle error if the event name could not be retrieved
            }, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Notify user of error
                    Toast.makeText(((horasservicio) itemView.getContext()),
                        "Error al mostrar el nombre del evento",
                        Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
