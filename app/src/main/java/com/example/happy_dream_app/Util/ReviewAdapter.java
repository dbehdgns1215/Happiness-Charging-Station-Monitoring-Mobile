package com.example.happy_dream_app.Util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.happy_dream_app.DTO.ReviewListDTO;
import com.example.happy_dream_app.R;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private List<ReviewListDTO> reviews;

    public ReviewAdapter(List<ReviewListDTO> reviews) {
        this.reviews = reviews;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvUsername;
        public RatingBar ratingBar;
        public TextView tvReviewContent;
        public TextView tvReviewDate;

        public ViewHolder(View view) {
            super(view);
            ratingBar = view.findViewById(R.id.rating_bar);
            tvReviewContent = view.findViewById(R.id.tv_review_content);
            tvReviewDate = view.findViewById(R.id.tv_review_date);
        }
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {
        ReviewListDTO review = reviews.get(position);
        holder.ratingBar.setRating(review.getRating());
        holder.tvReviewContent.setText(review.getReviewContent());
        holder.tvReviewDate.setText(review.getFormattedDate());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }
}
