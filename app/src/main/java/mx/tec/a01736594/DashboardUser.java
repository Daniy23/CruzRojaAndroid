package mx.tec.a01736594;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.firestore.DocumentReference;


/**
 * Representation of an DashboardUser
 */
@IgnoreExtraProperties
public class DashboardUser {
    // Original field names from the database
    public String id;
    public String nombre;
    public String matricula;
    public String email;
    public DocumentReference rol;
    public int hrsAcumuladas;

    /**
     * Initialize an DashboardUser object without parameters
     */
    public DashboardUser() {
        // Default constructor required for Firebase
    }

    /**
     * Initialize an DashboardUser object with parameters
     *
     * @param id             The id of the user
     * @param name           The name of the user
     * @param registration   The registration id of the user
     * @param email          The email of the user
     * @param role           The user role
     * @param cumulativeHours The cumulative hours of the user
     */
    public DashboardUser(String id, String name, String registration, String email, DocumentReference role, int cumulativeHours) {
        this.id = id;
        this.nombre = name;
        this.matricula = registration;
        this.email = email;
        this.rol = role;
        this.hrsAcumuladas = cumulativeHours;
    }

    /**
     * id getter
     */
    public String getId() {
        return this.id;
    }

    /**
     * nombre getter
     */
    public String getName() {
        return this.nombre;
    }

    /**
     * matricula getter
     */
    public String getRegistration() {
        return this.matricula;
    }

    /**
     * email getter
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * rol getter
     */
    public DocumentReference getRole() {
        return this.rol;
    }

    /**
     * hrsAcumuladas getter
     */
    public int getCumulativeHours() {
        return this.hrsAcumuladas;
    }
}
