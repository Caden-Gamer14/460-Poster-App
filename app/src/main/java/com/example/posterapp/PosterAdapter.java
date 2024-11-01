package com.example.posterapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Allows the user to swipe on the app to view more posters
 */
public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.PosterViewHolder> {

    /**
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return
     * Lets the poster know which part of the data you are trying to view
     */
    @NonNull
    @Override
    public PosterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PosterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_containter_poster, parent, false));
    }

    /**
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     * Binds the poster so everything is in proper position
     */
    @Override
    public void onBindViewHolder(@NonNull PosterViewHolder holder, int position) {

        holder.bindPoster(posterList.get(position));

    }

    /**
     * Determines the size of the poster
     * @return
     */
    @Override
    public int getItemCount() {

        return posterList.size();

    }

    private List<Poster> posterList;

    /**
     * Implements a poster adapted in order to apply attributes to the poster in the other methods
     * @param posterList
     * @param postersListener
     */
    public PosterAdapter(List<Poster> posterList, PostersListener postersListener) {
        this.posterList = posterList;
        this.postersListener = postersListener;
    }

    private PostersListener postersListener;

    /**
     * Adds the poster to the watchlist when selected
     */
    public List<Poster> getSelectedPosters() {

        List<Poster> selectedPosters = new ArrayList<>();

        for (Poster poster : this.posterList) {

            if (poster.isSelected) {

                selectedPosters.add(poster);

            }
        }

        return selectedPosters;

    }

    /**
     * Creates a second class to apply attributes such as the layout of the poster you select
     */
    class PosterViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout layoutPosters;

        View viewBackground;

        TextView textName, textCreatedBy, textStory;

        RatingBar ratingBar;

        ImageView imageSelected;

        RoundedImageView imagePoster;

        /**
         * Connects to the rest of the XML code to apply attributes
         * @param itemView
         */
        public PosterViewHolder(@NonNull View itemView) {

            super(itemView);
            layoutPosters = itemView.findViewById(R.id.layoutPosters);
            viewBackground = itemView.findViewById(R.id.viewBackground);
            imagePoster = itemView.findViewById(R.id.imagePosters);
            textName = itemView.findViewById(R.id.textName);
            textCreatedBy = itemView.findViewById(R.id.textCreateBy);
            textStory = itemView.findViewById(R.id.textStory);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            imageSelected = itemView.findViewById(R.id.imageSelected);

        }

        /**
         * Uses our poster data to fit into the main class to apply each attribute
         * This also applies the visibility of the poster as well
         * @param poster
         */
        void bindPoster(final Poster poster) {

            imagePoster.setImageResource(poster.image);
            textName.setText(poster.name);
            textCreatedBy.setText(poster.createdBy);
            textStory.setText(poster.story);
            ratingBar.setRating(poster.rating);

            if (poster.isSelected) {

                viewBackground.setBackgroundResource(R.drawable.poster_selected_background);
                imageSelected.setVisibility(View.VISIBLE);

            } else {

                viewBackground.setBackgroundResource(R.drawable.poster_background);
                imageSelected.setVisibility(View.GONE);

            }

            /**
             * Selects the poster and performs actions based on the selections to add to the display
             */
            layoutPosters.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (poster.isSelected) {

                        viewBackground.setBackgroundResource(R.drawable.poster_background);
                        imageSelected.setVisibility(View.GONE);
                        poster.isSelected = false;

                        if (getSelectedPosters().isEmpty()) {

                            postersListener.onPosterAction(false);
                        }
                        } else {

                            viewBackground.setBackgroundResource(R.drawable.poster_selected_background);
                            imageSelected.setVisibility(View.VISIBLE);
                            poster.isSelected = true;
                            postersListener.onPosterAction(true);

                        }
                }
            });

        }

    }

}
