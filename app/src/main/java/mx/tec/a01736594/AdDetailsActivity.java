package mx.tec.a01736594;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Activity to display the details of an ad
 */
public class AdDetailsActivity extends AppCompatActivity {
    TextView tvAdAuthor;
    TextView tvAdTitle;
    TextView tvAdIsEvent;
    TextView tvAdDescription;
    ImageView ivAdImage;
    TextView tvAdEventDate;
    TextView tvAdEventHours;
    LinearLayout linearLayoutEventDate;
    LinearLayout linearLayoutEventHours;
    int adAuthorBottomMargin;

    FirebaseFirestore db;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_details);

        // If no data was passed, return
        Intent intent = getIntent();
        if (intent == null) { finish(); }

        // Get the view elements
        tvAdAuthor = findViewById(R.id.tvAdAuthor);
        tvAdTitle = findViewById(R.id.tvAdTitle);
        tvAdIsEvent = findViewById(R.id.tvAdIsEvent);
        tvAdDescription = findViewById(R.id.tvAdDescription);
        ivAdImage = (ImageView) findViewById(R.id.ivAdImage);
        db = FirebaseFirestore.getInstance();
        tvAdEventDate = findViewById(R.id.tvAdEventDate);
        tvAdEventHours = findViewById(R.id.tvAdEventHours);
        linearLayoutEventDate = findViewById(R.id.linearLayoutEventDate);
        linearLayoutEventHours = findViewById(R.id.linearLayoutEventHours);
        mProgressDialog = new ProgressDialog(this);

        // Prepare the view elements data
        String adAuthorName = intent.getStringExtra("adAuthorName");
        String adTitle = intent.getStringExtra("adTitle");
        boolean adIsEvent = intent.getBooleanExtra("adIsEvent", false);
        String adDescription = intent.getStringExtra("adDescription");
        String adEventDate = intent.getStringExtra("adEventDate");
        String adEventHours = intent.getStringExtra("adEventHours");
        String adImage = intent.getStringExtra("adImage");

        // Set the ad data to the view elements
        // Ad author name
        tvAdAuthor.setText(adAuthorName);
        
        // Ad title
        tvAdTitle.setText(adTitle);
        
        // Ad event details and visibility
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams)
                tvAdAuthor.getLayoutParams();
        // If the ad is an event, show the event details
        if (adIsEvent) {
            adAuthorBottomMargin = 2;
            tvAdIsEvent.setVisibility(View.VISIBLE);
            tvAdIsEvent.setText("Evento");
            linearLayoutEventDate.setVisibility(View.VISIBLE);
            linearLayoutEventHours.setVisibility(View.VISIBLE);
            tvAdEventDate.setText(adEventDate);
            tvAdEventHours.setText(adEventHours);
        // Otherwise, hide the event details
        } else {
            adAuthorBottomMargin = 10;
            tvAdIsEvent.setVisibility(View.GONE);
            linearLayoutEventDate.setVisibility(View.GONE);
            linearLayoutEventHours.setVisibility(View.GONE);
        }
        // Set the bottom margin of the author name
        layoutParams.bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                adAuthorBottomMargin, getResources().getDisplayMetrics());
        tvAdAuthor.setLayoutParams(layoutParams);

        // Ad description
        if (adDescription == null || adDescription.isEmpty()) {
            tvAdDescription.setVisibility(View.GONE);
        } else {
            tvAdDescription.setText(adDescription);
        }

        // Ad image
        Glide.with(this).load(adImage).fitCenter().into(ivAdImage);
    }

    /**
     * Prepare the ad deleting process
     * 
     * @param v The view that called the method
     */
    public void prepareAdDeleting(View v) {
        // Display a confirmation dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmación");
        builder.setMessage("¿Eliminar anuncio?");

        // Positive button (Yes)
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = getIntent();
        
                // Show a progress dialog
                mProgressDialog.setTitle("Cargando");
                mProgressDialog.setMessage("Eliminando anuncio...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();

                // Get necessary data to delete the ad
                String adId = intent.getStringExtra("adId");
                String imageUrl = intent.getStringExtra("adImage");

                // If the ad has an image, delete it from the storage first
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    StorageReference storageRef = FirebaseStorage.getInstance()
                            .getReferenceFromUrl(imageUrl);
                    storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Delete ad from database
                            deleteAdFromDatabase(adId);
                        }
                    // Handle error if image deletion fails
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Notify user of error
                            Toast.makeText(AdDetailsActivity.this,
                                    "Error al eliminar el campo imagen del anuncio",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                // Otherwise, just delete the ad from the database
                } else {
                    // Delete ad from database
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
        // Delete ad from the database
        db.collection("anuncios").document(adId).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Notify user of successfull deletion
                mProgressDialog.dismiss();
                Toast.makeText(AdDetailsActivity.this, "Anuncio eliminado exitosamente",
                        Toast.LENGTH_SHORT).show();

                // Keep track of the user data in the app
                Intent intent = getIntent();
                Intent newIntent = new Intent(AdDetailsActivity.this, anuncios.class);
                newIntent.putExtra("userId", intent.getStringExtra("userId"));
                newIntent.putExtra("userEmail", intent.getStringExtra("userEmail"));
                newIntent.putExtra("userName", intent.getStringExtra("userName"));

                // Redirect to the ads list activity
                startActivity(newIntent);
            }
        // Handle error if the ad deletion fails
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NotNull Exception e) {
                // Notify user of error
                mProgressDialog.dismiss();
                Toast.makeText(AdDetailsActivity.this, "Error al eliminar el anuncio",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
