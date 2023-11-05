package mx.tec.a01736594;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;


/**
 * Adapter for the RecyclerView of ads
 */
public class AdAdapter extends RecyclerView.Adapter<AdAdapter.AdViewHolder> {
    private List<Ad> ads;
    private static OnAdDeleteListener onAdDeleteListener;

    /**
     * Initialize an AdAdapter object with parameters
     * @param ads List of ads
     * @param onAdDeleteListener Listener for the delete event
     */
    public AdAdapter(List<Ad> ads, OnAdDeleteListener onAdDeleteListener) {
        this.ads = ads;
        this.onAdDeleteListener = onAdDeleteListener;
    }

    @NonNull
    @Override
    public AdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout of the ad item
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ad, parent,
                false);
        return new AdViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdViewHolder holder, int position) {
        // Get the ad at the current position
        Ad ad = ads.get(position);

        // Bind the ad to the view holder
        holder.bind(ad);

        // Set the click listener for the ad item and redirect to the
        // ad details activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the ad data
                String adId = ad.getUid();
                String adTitle = ad.getTitle();
                String adDescription = ad.getDescription();
                String adEventDate = ad.getEventDate();
                String adEventHours = Integer.toString(ad.getEventHours());
                String adImage = ad.getImageUrl();
                boolean adIsEvent = ad.getType();

                // Retrieve the author name from the database
                ad.getAuthorName(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String adAuthorName) {
                        // Keep track of the user data in the app
                        Intent intent = ((anuncios) view.getContext()).getIntent();
                        Intent newIntent = new Intent(view.getContext(), AdDetailsActivity.class);
                        newIntent.putExtra("userId", intent.getStringExtra("userId"));
                        newIntent.putExtra("userEmail",
                                intent.getStringExtra("userEmail"));
                        newIntent.putExtra("userName",
                                intent.getStringExtra("userName"));
                        newIntent.putExtra("adId", adId);
                        newIntent.putExtra("adAuthorName", adAuthorName);
                        newIntent.putExtra("adTitle", adTitle);
                        newIntent.putExtra("adIsEvent", adIsEvent);
                        newIntent.putExtra("adDescription", adDescription);
                        newIntent.putExtra("adEventDate", adEventDate);
                        newIntent.putExtra("adImage", adImage);
                        newIntent.putExtra("adEventHours", adEventHours);

                        // Redirect to the ad details activity
                        view.getContext().startActivity(newIntent);
                    }
                // Handle error if the author name could not be retrieved
                }, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Notify user of error
                        Toast.makeText(((anuncios) view.getContext()),
                                "Error al mostrar los detalles del anuncio",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return ads.size();
    }

    /*
     * Interface for the delete event
     */
    public interface OnAdDeleteListener {
        void onAdDelete(int position);
    }

    /**
     * ViewHolder for the ad item
     */
    static class AdViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle;
        TextView textDate;
        Button btnDeleteListAd;

        /**
         * Initialize a view holder for the ad item
         * @param itemView The view of the ad item
         */
        AdViewHolder(@NonNull View itemView) {
            super(itemView);

            // Get elements from the view of the ad item
            textTitle = itemView.findViewById(R.id.textTitle);
            textDate = itemView.findViewById(R.id.textDate);
            btnDeleteListAd = itemView.findViewById(R.id.btnDeleteListAd);
        }

        /**
         * Bind the ad data to the ad item view
         */
        void bind(Ad ad) {
            // Set the ad data to the view elements
            // Ad title (truncated to 20 characters)
            String fullTitle = ad.getTitle();
            String truncatedTitle;
            if (fullTitle.length() > 20) {
                truncatedTitle = fullTitle.substring(0, 17) + "...";
            } else {
                truncatedTitle = fullTitle;
            }
            textTitle.setText(truncatedTitle);
            
            // Ad date
            textDate.setText(ad.getDateString());

            // Set the color of the title depending if the ad is an event
            if (ad.getType()) {
                int colorBlueDark = ContextCompat.getColor(itemView.getContext(),
                        android.R.color.holo_blue_dark);
                textTitle.setTextColor(colorBlueDark);
            }

            // Set the click listener for the delete button
            btnDeleteListAd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    onAdDeleteListener.onAdDelete(position);
                }
            });
        }
    }
}
