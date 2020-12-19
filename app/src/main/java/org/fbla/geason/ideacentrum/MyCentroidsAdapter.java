package org.fbla.geason.ideacentrum;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import static org.fbla.geason.ideacentrum.LoginActivity.database;

public class MyCentroidsAdapter extends RecyclerView.Adapter<MyCentroidsAdapter.MyCentroidViewHolder> {
    Context mContext;

    public class MyCentroidViewHolder extends RecyclerView.ViewHolder {
        ImageView centroidImage;
        TextView centroidTitle;
        TextView centroidIdea;
        LinearLayout containerview;
        int centroidId;

        public MyCentroidViewHolder(@NonNull final View itemView) {
            super(itemView);

            centroidImage = itemView.findViewById(R.id.centroid_image);
            centroidTitle = itemView.findViewById(R.id.centroid_title);
            centroidIdea = itemView.findViewById(R.id.centroid_description);
            containerview = itemView.findViewById(R.id.centroid_containerview);

            containerview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toCentroidActivity = new Intent(mContext.getApplicationContext(), CentroidActivity.class);
                    toCentroidActivity.putExtra("centroid id", centroidId);
                    mContext.startActivity(toCentroidActivity);
                }
            });
        }
    }

    public MyCentroidsAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public MyCentroidsAdapter.MyCentroidViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Introduce Centroid as cell of grid
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.centroid, parent, false);

        return new MyCentroidViewHolder(v);
    }

    private List<Centroid> centroidList = new ArrayList<>();

    @Override
    public void onBindViewHolder(@NonNull MyCentroidsAdapter.MyCentroidViewHolder holder, int position) {

        // Populate gridview with images, titles, and ideas
        Centroid current = centroidList.get(position);
        holder.centroidId = current.centroidId;
        if(current.fileUri == null) {
            holder.centroidImage.setVisibility(View.INVISIBLE);
        } else {
            holder.centroidImage.setImageURI(Uri.parse(current.fileUri));
        }

        holder.centroidTitle.setText(current.title);
        holder.centroidIdea.setText(current.idea);
    }

    public void reload(int id) {
        centroidList = database.centroidDao().getCentroids(id);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return centroidList.size();
    }
}
