package mx.tec.a01736594;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Activity to create a new ad
 */
public class nuevoanuncio extends AppCompatActivity {

    EditText txtAdTitle;
    EditText txtAdDescription;
    CheckBox cbAdIsEvent;
    LinearLayout linearLayoutOptional;
    Button btnAdEventDate;
    EditText txtAdEventHours;
    Calendar calendar;
    Button mUploadBtn;

    StorageReference mStorage;
    static final int GALLERY_INTENT = 1;
    ProgressDialog mProgressDialog;
    FirebaseFirestore db;

    boolean adDateIsSet = false;
    boolean adImageIsSet = false;
    Uri uri;
    String downloadUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevoanuncio);

        // If no data was passed, return
        Intent intent = getIntent();
        if (intent == null) { finish(); }
        
        // Get the view elements
        txtAdTitle = findViewById(R.id.txtAdTitle);
        cbAdIsEvent = findViewById(R.id.cbAdIsEvent);
        txtAdDescription = findViewById(R.id.txtAdDescription);
        btnAdEventDate = findViewById(R.id.btnAdEventDate);
        calendar = Calendar.getInstance();
        txtAdEventHours = findViewById(R.id.txtAdEventHours);
        mStorage = FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();
        mUploadBtn = (Button) findViewById(R.id.btnAdImage);
        mProgressDialog = new ProgressDialog(this);
        linearLayoutOptional = findViewById(R.id.linearLayoutOptional);

        // Hide optional fields (event date and hours)
        cbAdIsEvent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    linearLayoutOptional.setVisibility(View.VISIBLE);
                } else {
                    linearLayoutOptional.setVisibility(View.GONE);
                }
            }
        });

        // Show date picker
        btnAdEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        // Choose image from gallery
        mUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*"); // Only image files
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });
    }

    /**
     * Show a DatePickerDialog to select a date
     */
    private void showDatePicker() {
        // Get current year, month and day
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // Configure the selected date
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        // Show the TimePickerDialog after selecting the date
                        showTimePicker();
                    }
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    /**
     * Show a TimePickerDialog to select a time
     */
    private void showTimePicker() {
        // Get current hour and minute
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create a 24-hour TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Configure the selected time
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0); // Set seconds to 0
                        String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                                Locale.getDefault()).format(calendar.getTime());
                        btnAdEventDate.setText(formattedDate);

                        // Keep track whether the date has been set
                        adDateIsSet = true;
                    }
                },
                hour, minute, true
        );
        timePickerDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Save image locally
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            uri = data.getData();
            mUploadBtn.setText("Cambiar imagen");

            // Keep track whether the image has been set
            adImageIsSet = true;
        }
    }

    /**
     * Create a new ad
     * 
     * @param v The view that called the method
     */
    public void newAd(View v) {
        // Create a map to store the values
        Map<String, Object> map = new HashMap<>();
        
        // Get the values from the form
        String adTitle = txtAdTitle.getText().toString();
        String adDescription = txtAdDescription.getText().toString();
        boolean adIsEvent = cbAdIsEvent.isChecked();
        String adEventHours = txtAdEventHours.getText().toString();

        // Validate the required fields
        if ((adTitle == null || adTitle.isEmpty()) || (adIsEvent &&
                (!adDateIsSet || adEventHours == null || adEventHours.isEmpty()))) {
            this.showAlert("Ingresa todos los campos requeridos (*)");
            return;
        }

        // Show a progress dialog
        mProgressDialog.setTitle("Cargando");
        mProgressDialog.setMessage("Creando anuncio...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        // Add the values to the map
        // Ad author
        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");
        DocumentReference userRef = db.document("users/" + userId);
        map.put("author", userRef);
        
        // Ad creation date
        Timestamp adDate = Timestamp.now();
        map.put("fecha", adDate);
        map.put("titulo", adTitle); // Ad title
        
        // Ad description
        if (adDescription != null && !adDescription.isEmpty()) {
            map.put("descripcion", adDescription);
        }
        
        // Ad type
        map.put("tipo", adIsEvent);
        
        // Ad event date and hours
        if (adIsEvent) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                    Locale.getDefault());
            String formattedDate = sdf.format(calendar.getTime());
            map.put("fechaEvento", formattedDate);
            map.put("hrsMax", Integer.parseInt(adEventHours));
        }

        // Ad image (use task chaining to get the image URL before uploading to the database)
        Task<Uri> imageUploadTask;
        if (adImageIsSet) {
            // Upload the image to the Storage and get the URL
            StorageReference filePath = mStorage.child("anuncios")
                    .child(uri.getLastPathSegment());
            imageUploadTask = filePath.putFile(uri)
                .continueWithTask(task -> task.getResult().getStorage().getDownloadUrl());
        } else {
            // If there is no image, create a completed task with a null URL
            imageUploadTask = Tasks.forResult(null);
        }

        // Continue with the task to handle the data in the database
        imageUploadTask.continueWithTask(task -> {
            // Only add the "image" field if there is a valid URL
            if (task.getResult() != null) {
                downloadUri = task.getResult().toString();
                map.put("imagen", downloadUri);
            }

            // Upload the data to the database
            return db.collection("anuncios").add(map);
        })
        .addOnSuccessListener(documentReference -> {
            // Notify user of successfull upload
            mProgressDialog.dismiss();
            Toast.makeText(this, "Anuncio creado exitosamente", Toast.LENGTH_SHORT)
                    .show();
            
            // Keep track of the user data in the app
            Intent newIntent = new Intent(this, anuncios.class);
            newIntent.putExtra("userId", intent.getStringExtra("userId"));
            newIntent.putExtra("userEmail", intent.getStringExtra("userEmail"));
            newIntent.putExtra("userName", intent.getStringExtra("userName"));

            // Redirect to the ads list activity
            startActivity(newIntent);
        })
        // Handle error if the upload fails
        .addOnFailureListener(e -> {
            // Notify user of error
            Toast.makeText(this, "Error al crear el anuncio", Toast.LENGTH_SHORT)
                    .show();
        });
    }

    /**
     * Show an alert dialog with an error message
     * 
     * @param errorMessage The error message to be displayed
     */
    public void showAlert(String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(errorMessage).setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void go(View v){
        Intent intent = new Intent(this, anuncios.class);
        startActivity(intent);
    }
}
