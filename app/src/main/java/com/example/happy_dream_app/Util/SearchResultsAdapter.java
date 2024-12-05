package com.example.happy_dream_app.Util;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happy_dream_app.DTO.NaverLocalSearchResponseDTO;
import com.example.happy_dream_app.R;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.SearchResultViewHolder> {

    private List<NaverLocalSearchResponseDTO.Item> items = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(NaverLocalSearchResponseDTO.Item item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<NaverLocalSearchResponseDTO.Item> items) {
        if (items != null) {
            this.items = items;
        } else {
            this.items = new ArrayList<>();
        }
        Log.d("SearchResultsAdapter", "Items set. Size: " + this.items.size());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false);
        return new SearchResultViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {
        NaverLocalSearchResponseDTO.Item item = items.get(position);
        holder.tvTitle.setText(Html.fromHtml(item.getTitle(), Html.FROM_HTML_MODE_LEGACY));
        holder.tvAddressNew.setText(Html.fromHtml(item.getRoadAddress(), Html.FROM_HTML_MODE_LEGACY));
        holder.tvAddressOld.setText(Html.fromHtml(item.getAddress(), Html.FROM_HTML_MODE_LEGACY));
    }

    @Override
    public int getItemCount() {
        int count = items != null ? items.size() : 0;
        return count;
    }

    class SearchResultViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvAddressNew, tvAddressOld;

        public SearchResultViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvAddressNew = itemView.findViewById(R.id.tv_address_new);
            tvAddressOld = itemView.findViewById(R.id.tv_address_old);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(items.get(position));
                }
            });
        }
    }
}
