package mx.tec.a01736594;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.firestore.DocumentReference;

import java.text.SimpleDateFormat;
import java.util.Locale;


/**
 * Representation of an Ad
 */
@IgnoreExtraProperties
public class Ad {
    // Extra fields
    public String uid;
    // Original field names from the database
    public DocumentReference author;
    public String titulo;
    public boolean tipo;
    public String descripcion;
    public Timestamp fecha;
    public String fechaEvento;
    public String imagen;
    public int hrsMax;

    /**
     * Initialize an Ad object without parameters
     */
    public Ad() {
        // Default constructor required for Firebase
    }

    /**
     * Initialize an Ad object with parameters
     *
     * @param authorReference Reference to the author of the ad
     * @param title           Title of the ad
     * @param type            Ad type (false = common ad, true = event ad)
     * @param description     Description of the ad
     * @param date            Date of creation of the ad
     * @param dateEvent       Date of the event
     * @param image           URL of the ad image
     * @param maxHours        Maximum hours to be granted when is an event ad
     */
    public Ad(DocumentReference authorReference, String title, boolean type, String description,
              Timestamp date, String dateEvent, String image, int maxHours) {
        this.author = authorReference;
        this.titulo = title;
        this.tipo = type;
        this.descripcion = description;
        this.fecha = date;
        this.fechaEvento = dateEvent;
        this.imagen = image;
        this.hrsMax = maxHours;
    }

    /**
     * uid getter
     */
    public String getUid() {
        return this.uid;
    }

    /**
     * author getter
     */
    public DocumentReference getAuthor() {
        return this.author;
    }

    /**
     * author (id) getter
     */
    public String getAuthorId() {
        return this.author.getId();
    }

    /**
     * author (name) getter
     */
    public void getAuthorName(OnSuccessListener<String> onSuccessListener,
                              OnFailureListener onFailureListener) {
        // Get the author name from the author document
        this.author.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String adAuthorName = documentSnapshot.getString("Nombre");
                onSuccessListener.onSuccess(adAuthorName);
            } else {
                onFailureListener.onFailure(new Exception("No se encontró el autor del anuncio."));
            }
        }).addOnFailureListener(onFailureListener);
    }

    /**
     * titulo getter
     */
    public String getTitle() {
        return this.titulo;
    }

    /**
     * tipo getter
     */
    public boolean getType() {
        return this.tipo;
    }

    /**
     * descripcion getter
     */
    public String getDescription() {
        return this.descripcion;
    }

    /**
     * fecha getter
     */
    public Timestamp getDate() {
        return this.fecha;
    }

    /**
     * fecha (string) getter
     */
    public String getDateString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy, HH:mm:ss'h'",
                Locale.getDefault());
        return dateFormat.format(this.getDate().toDate());
    }

    /**
     * fechaEvento getter
     */
    public String getEventDate() {
        return this.fechaEvento;
    }

    /**
     * imagen getter
     */
    public String getImageUrl() {
        return this.imagen;
    }

    /**
     * hrsMax getter
     */
    public int getEventHours() {
        return this.hrsMax;
    }

    /**
     * uid setter
     */
    public void setUid(String uid) {
        this.uid = uid;
    }
}
