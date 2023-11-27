package mx.tec.a01736594;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


/**
 * Adapter for the RecyclerView of dashboard users
 */
public class DashboardUserAdapter extends RecyclerView.Adapter<DashboardUserAdapter.DashboardUserViewHolder> {
    private List<DashboardUser> dashboardUsers;
    private static OnDashboardUserDeleteListener onDashboardUserDeleteListener;

    /**
     * Initialize an DashboardUserAdapter object with parameters
     * @param dashboardUsers List of dashboard users
     * @param onDashboardUserDeleteListener Listener for the delete event
     */
    public DashboardUserAdapter(List<DashboardUser> dashboardUsers, OnDashboardUserDeleteListener onDashboardUserDeleteListener) {
        this.dashboardUsers = dashboardUsers;
        this.onDashboardUserDeleteListener = onDashboardUserDeleteListener;
    }

    @NonNull
    @Override
    public DashboardUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout of the dashboard user item
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dashboard_user, parent,
                false);
        return new DashboardUserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardUserViewHolder holder, int position) {
        // Get the dashboard user at the current position
        DashboardUser dashboardUser = dashboardUsers.get(position);

        // Bind the dashboard user to the view holder
        holder.bind(dashboardUser);

        // Set the click listener for the dashboard user item and redirect to the
        // dashboard user details activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Do nothing
            }
        });
    }

    @Override
    public int getItemCount() {
        return dashboardUsers.size();
    }

    /*
     * Interface for the delete event
     */
    public interface OnDashboardUserDeleteListener {
        void onDashboardUserDelete(int position);
    }

    /**
     * ViewHolder for the dashboard user item
     */
    static class DashboardUserViewHolder extends RecyclerView.ViewHolder {
        TextView tvDashboardUserName;
        TextView tvDashboardUserId;
        TextView tvDashboardUserCumulativeHours;

        /**
         * Initialize a view holder for the dashboard user item
         * @param itemView The view of the dashboard user item
         */
        DashboardUserViewHolder(@NonNull View itemView) {
            super(itemView);

            // Get elements from the view of the dashboard user item
            tvDashboardUserName = itemView.findViewById(R.id.tvDashboardUserName);
            tvDashboardUserId = itemView.findViewById(R.id.tvDashboardUserId);
            tvDashboardUserCumulativeHours = itemView.findViewById(R.id.tvDashboardUserCumulativeHours);
        }

        /**
         * Bind the dashboard user data to the ad item view
         */
        void bind(DashboardUser dashboardUser) {
            // Set the dashboard user data to the view elements
            // Dashboard user name (truncated to 12 characters)
            String fullDashboardUserName = dashboardUser.getName();
            String truncatedName;
            if (fullDashboardUserName.length() > 12) {
                truncatedName = fullDashboardUserName.substring(0, 9) + "...";
            } else {
                truncatedName = fullDashboardUserName;
            }
            tvDashboardUserName.setText(truncatedName);

            // Dashboard user id (truncated to 12 characters)
            String fullDashboardUserId = dashboardUser.getId();
            String truncatedId;
            if (fullDashboardUserId.length() > 12) {
                truncatedId = fullDashboardUserId.substring(0, 9) + "...";
            } else {
                truncatedId = fullDashboardUserId;
            }
            tvDashboardUserId.setText(truncatedId);
            
            // Dashboard user cumulative hours (truncated to 12 characters)
            String fullDashboardUserCumulativeHours = Integer.toString(dashboardUser.getCumulativeHours());
            String truncatedCumulativeHours;
            if (fullDashboardUserCumulativeHours.length() > 12) {
                truncatedCumulativeHours = fullDashboardUserCumulativeHours.substring(0, 9) + "...";
            } else {
                truncatedCumulativeHours = fullDashboardUserCumulativeHours;
            }
            tvDashboardUserCumulativeHours.setText(truncatedCumulativeHours);
        }
    }
}
