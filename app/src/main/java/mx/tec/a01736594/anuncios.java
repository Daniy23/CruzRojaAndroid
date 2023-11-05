package mx.tec.a01736594;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


/**
 * Activity to display the ads
 */
public class anuncios extends AppCompatActivity implements AdAdapter.OnAdDeleteListener {
    private RecyclerView recyclerView;
    private AdAdapter adAdapter;
    
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressDialog mProgressDialog;
    List<Ad> adList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncios);

        // If no data was passed, return
        Intent intent = getIntent();
        if (intent == null) { finish(); }

        // Get the view elements
        recyclerView = findViewById(R.id.rvAdList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mProgressDialog = new ProgressDialog(this);
        
        // Prepare the view elements data (get ads from the database)
        CollectionReference adsRef = db.collection("anuncios");
        Query query = adsRef.orderBy("fecha", Query.Direction.DESCENDING);
        
        // Get results from the query
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                adList = new ArrayList<>();

                // Iterate over the results of the query
                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                    // Convert the document to an "Ad" object
                    Ad ad = document.toObject(Ad.class);
                    ad.setUid(document.getId());
                    adList.add(ad);
                }
                // Create an adapter to display the ads in the RecyclerView
                adAdapter = new AdAdapter(adList, anuncios.this);
                recyclerView.setAdapter(adAdapter);
            }
        // Handle error if the query fails
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NotNull Exception e) {
                // Notify user of error
                Toast.makeText(anuncios.this, "Error al visualizar los anuncios",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAdDelete(int position) {
        // Display a confirmation dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmación");
        builder.setMessage("¿Eliminar anuncio?");

        // Positive button (Yes)
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Show a progress dialog
                mProgressDialog.setTitle("Cargando");
                mProgressDialog.setMessage("Eliminando anuncio...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                
                // Get the ad to be deleted
                Ad deletedAd = adList.get(position);
                String adId = deletedAd.getUid();

                // If the ad has an image, delete it from the storage first
                String imageUrl = deletedAd.getImageUrl();
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    StorageReference storageRef = FirebaseStorage.getInstance()
                            .getReferenceFromUrl(imageUrl);
                    storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Delete the ad from the database
                            deleteAdFromDatabase(adId);
                        }
                    // Handle error if the image deletion fails
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Notify user of error
                            Toast.makeText(anuncios.this,
                                    "Error al eliminar la imagen del anuncio",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                // Otherwise, just delete the ad from the database
                } else {
                    // Delete the ad from the database
                    deleteAdFromDatabase(adId);
                }
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
     * Delete ad
     * 
     * @param adId The id of the ad to be deleted
     */
    private void deleteAdFromDatabase(String adId) {
        // Delete the ad from the database
        db.collection("anuncios").document(adId).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Notify user of successfull deletion
                mProgressDialog.dismiss();
                Toast.makeText(anuncios.this, "Anuncio eliminado exitosamente",
                        Toast.LENGTH_SHORT).show();

                // Keep track of the user data in the app
                Intent intent = getIntent();
                Intent newIntent = new Intent(anuncios.this, anuncios.class);
                newIntent.putExtra("userId", intent.getStringExtra("userId"));
                newIntent.putExtra("userEmail", intent.getStringExtra("userEmail"));
                newIntent.putExtra("userName", intent.getStringExtra("userName"));

                // Redirect to the ads list activity
                startActivity(newIntent);
            }
        // Handle error if the deletion fails
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Notify user of error
                mProgressDialog.dismiss();
                Toast.makeText(anuncios.this, "Error al eliminar el anuncio",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void go(View v){
        Intent intent = new Intent(this, MenuPrincipalActivity.class);
        startActivity(intent);
    }

    /**
     * Open the activity for new ad creation
     * 
     * @param v The view that called the method
     */
    public void createAd(View v) {
        // Keep track of the user data in the app
        Intent intent = getIntent();
        Intent newIntent = new Intent(this, nuevoanuncio.class);
        newIntent.putExtra("userId", intent.getStringExtra("userId"));
        newIntent.putExtra("userEmail", intent.getStringExtra("userEmail"));
        newIntent.putExtra("userName", intent.getStringExtra("userName"));

        // Redirect to the new ad activity
        startActivity(newIntent);
    }
}
