package com.daniel.reportes.ui.app.fragment.reportes;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daniel.reportes.R;
import com.google.android.material.checkbox.MaterialCheckBox;

class ReporteViewHolder extends RecyclerView.ViewHolder {

    private ImageView picture;
    private final TextView date;
    private final MaterialCheckBox fixed;

    ReporteViewHolder(@NonNull View itemView) {
        super(itemView);
        this.picture = itemView.findViewById(R.id.card_picture);
        this.date = itemView.findViewById(R.id.card_date);
        this.fixed = itemView.findViewById(R.id.card_fixed);
    }

    ImageView getPicture() {
        return picture;
    }

    TextView getDate() {
        return date;
    }

    MaterialCheckBox getFixed() {
        return fixed;
    }
}
