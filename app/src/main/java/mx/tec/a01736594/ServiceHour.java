package mx.tec.a01736594;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * Representation of a ServiceHour
 */
@IgnoreExtraProperties
public class ServiceHour {
    public String volunteerName;
    public String eventTitle;
    // Extra fields
    public String uid;
    // Original field names from the database
    public String idVoluntario;
    public String evento;
    public String estatus;
    public boolean aprobadas;
    public String evidencia;
    public int hrs;

    /**
     * Initialize an ServiceHour object without parameters
     */
    public ServiceHour() {
        // Default constructor required for Firebase
    }

    /**
     * Initialize an ServiceHour object with parameters
     *
     * @param volunteerId The id of the volunteer
     * @param event       The event of the service hour
     * @param status      The status of the service hour
     * @param approved    The approval status of the service hour
     * @param evidence    The evidence of the service hour
     * @param hours       The hours of the service hour
     */
    public ServiceHour(String volunteerId, String event, String status, boolean approved,
                       String evidence, int hours) {
        this.idVoluntario = volunteerId;
        this.evento = event;
        this.estatus = status;
        this.aprobadas = approved;
        this.evidencia = evidence;
        this.hrs = hours;
    }

    /**
     * uid getter
     * 
     * @return The uid of the service hour
     */
    public String getUid() {
        return this.uid;
    }

    /**
     * idVoluntario getter
     * 
     * @return The id of the volunteer
     */
    public String getVolunteerId() {
        return this.idVoluntario;
    }

    /**
     * idVoluntario (name) getter
     * 
     * @param volunteerId The id of the volunteer
     * @param onSuccessListener The listener for the success event
     * @param onFailureListener The listener for the failure event
     */
    public void getVolunteerName(String volunteerId,
                                 OnSuccessListener<String> onSuccessListener,
                                 OnFailureListener onFailureListener) {
        DocumentReference volunteerRef = FirebaseFirestore.getInstance()
                .collection("users")
                .document(volunteerId);

        // Get the volunteer name from the volunteer document
        volunteerRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String volunteerName = documentSnapshot.getString("nombre");
                    onSuccessListener.onSuccess(volunteerName);
                } else {
                    onFailureListener.onFailure(new Exception(
                            "No se encontró el voluntario de las horas de servicio."));
                }
            }
        }).addOnFailureListener(onFailureListener);
    }

    /**
     * idVoluntario (name) getter
     * 
     * @return The name of the volunteer
     */
    public String getVolunteerName() {
        return this.volunteerName;
    }

    /**
     * evento getter
     * 
     * @return The event of the service hour
     */
    public String getEvent() {
        return this.evento;
    }

    /**
     * evento (title) getter
     * 
     * @param eventId The id of the event
     * @param onSuccessListener The listener for the success event
     * @param onFailureListener The listener for the failure event
     */
    public void getEventTitle(String eventId,
                              OnSuccessListener<String> onSuccessListener,
                              OnFailureListener onFailureListener) {
        DocumentReference eventRef = FirebaseFirestore.getInstance()
                .collection("anuncios")
                .document(eventId);

        // Get the event title from the event document
        eventRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String eventTitle = documentSnapshot.getString("titulo");
                    onSuccessListener.onSuccess(eventTitle);
                } else {
                    onFailureListener.onFailure(new Exception(
                            "No se encontró el evento de las horas de servicio."));
                }
            }
        }).addOnFailureListener(onFailureListener);
    }

    /**
     * evento (title) getter
     * 
     * @return The title of the event
     */
    public String getEventTitle() {
        return this.eventTitle;
    }

    /**
     * estatus getter
     * 
     * @return The status of the service hour
     */
    public String getStatus() {
        return this.estatus;
    }

    /**
     * aprobadas getter
     * 
     * @return The approval status of the service hour
     */
    public boolean isApproved() {
        return this.aprobadas;
    }

    /**
     * evidencia getter
     * 
     * @return The evidence of the service hour
     */
    public String getEvidence() {
        return this.evidencia;
    }

    /**
     * hrs getter
     * 
     * @return The hours of the service hour
     */
    public int getHours() {
        return this.hrs;
    }

    /**
     * uid setter
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * idVoluntario (name) setter
     */
    public void setVolunteerName(String volunteerName) {
        this.volunteerName = volunteerName;
    }

    /**
     * evento (title) setter
     */
    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }
}
