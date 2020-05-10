package com.daniel.reportes.ui.app.fragment.reportes;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daniel.reportes.R;
import com.daniel.reportes.data.Reporte;
import com.daniel.reportes.ui.app.fragment.reportes.detail.ReporteDetail;
import com.squareup.picasso.Picasso;

import org.joda.time.LocalDateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ReporteAdapter extends RecyclerView.Adapter<ReporteViewHolder> {

    private List<Reporte> data;

    public ReporteAdapter(List<Reporte> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ReporteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_reporte, parent, false);

        return new ReporteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReporteViewHolder holder, int position) {
        holder.getFixed().setActivated(data.get(position).isFixed());
        holder.getFixed().setText(holder.getFixed().isActivated()
                ? holder.itemView.getContext().getString(R.string.reports_fixed)
                : holder.itemView.getContext().getString(R.string.reports_awaiting)
        );
        holder.getDate().setText(
                new LocalDateTime(data.get(position).getDate())
                        .toString("dd MMMM, yyyy", Locale.forLanguageTag("MX"))
        );

        if (!data.get(position).getPicture().equals("")) {
            Picasso.get()
                    .load(data.get(position).getPicture())
                    .error(R.drawable.reportes)
                    .resize(250, 250)
                    .onlyScaleDown()
                    .tag(holder)
                    .into(holder.getPicture());
        }

        holder.itemView.setOnClickListener(v -> {

            Intent detailIntent = new Intent(holder.itemView.getContext(), ReporteDetail.class);
            detailIntent.putExtra("reporte", data.get(position));

            holder.itemView.getContext().startActivity(detailIntent);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
