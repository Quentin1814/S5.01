package com.example.deching;

import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.deching.Modele.Modele.Evenement;
import java.util.List;
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private List<Evenement> evenementsList;

    public EventAdapter(List<Evenement> evenementsList) {
        this.evenementsList = evenementsList;
    }

    @NonNull
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Gonfler la mise en page de l'élément de vue
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        // Créer et retourner le ViewHolder
        return new EventViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Evenement evenement = evenementsList.get(position);
        holder.eventName.setText(evenement.getNom());
        holder.eventLocation.setText(evenement.getLieu());
        holder.eventDescription.setText(evenement.getDescription());

        if (evenement.getPhotoBase64() != null && !evenement.getPhotoBase64().isEmpty()) {
            byte[] decodedString = Base64.decode(evenement.getPhotoBase64(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.eventImage.setImageBitmap(decodedByte);
        } else {
            holder.eventImage.setImageResource(R.drawable.empty);
        }
    }

    @Override
    public int getItemCount() {
        return evenementsList.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        ImageView eventImage;
        TextView eventName;
        TextView eventLocation;
        TextView eventDescription;

        EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventImage = itemView.findViewById(R.id.eventImage);
            eventName = itemView.findViewById(R.id.eventName);
            eventLocation = itemView.findViewById(R.id.eventLocation);
            eventDescription = itemView.findViewById(R.id.eventDescription);
        }

    }
}

