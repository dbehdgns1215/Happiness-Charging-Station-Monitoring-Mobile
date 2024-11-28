package com.example.happy_dream_app.Util;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.happy_dream_app.DTO.ChargerDetailDTO;
import com.example.happy_dream_app.R;
import java.util.List;

public class ChargerListAdapter extends RecyclerView.Adapter<ChargerListAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(ChargerDetailDTO charger);
    }

    private List<ChargerDetailDTO> chargers;
    private OnItemClickListener listener;

    public ChargerListAdapter(List<ChargerDetailDTO> chargers, OnItemClickListener listener) {
        this.chargers = chargers;
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView chargerName;
        TextView address;
        TextView addressDetail;
        TextView statusText;
        ImageView statusIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            chargerName = itemView.findViewById(R.id.charger_name);
            address = itemView.findViewById(R.id.address);
            addressDetail = itemView.findViewById(R.id.address_detail);
            statusText = itemView.findViewById(R.id.status_text);
            statusIcon = itemView.findViewById(R.id.status_icon);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                ChargerDetailDTO charger = chargers.get(position);
                listener.onItemClick(charger);
            });
        }
    }

    @Override
    public ChargerListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_charger, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChargerListAdapter.ViewHolder holder, int position) {
        ChargerDetailDTO charger = chargers.get(position);
        holder.chargerName.setText(charger.getName());
        holder.address.setText(charger.getAddressNew());
        holder.addressDetail.setText(charger.getAddressDetail());

        // 상태 설정
        if (charger.getUsingYn() != null && charger.getUsingYn()) {
            // 사용 중인 경우
            holder.statusText.setText("사용 중");
            holder.statusText.setTextColor(Color.parseColor("#FFA500")); // 주황색 텍스트
            holder.statusIcon.setImageResource(R.drawable.circle_in_use); // 주황색 원
        } else if (charger.getBrokenYn() != null && charger.getBrokenYn()) {
            // 고장난 경우
            holder.statusText.setText("고장");
            holder.statusText.setTextColor(Color.parseColor("#FF0000")); // 빨간색 텍스트
            holder.statusIcon.setImageResource(R.drawable.circle_broken); // 빨간색 원
        } else {
            // 사용 가능한 경우
            holder.statusText.setText("사용 가능");
            holder.statusText.setTextColor(Color.parseColor("#008000")); // 초록색 텍스트
            holder.statusIcon.setImageResource(R.drawable.circle_available); // 초록색 원
        }
    }

    @Override
    public int getItemCount() {
        return chargers.size();
    }
}
