package com.dnieln7.roadwatchman.ui.app.pages.reports;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dnieln7.roadwatchman.R;
import com.dnieln7.roadwatchman.data.model.Reporte;
import com.dnieln7.roadwatchman.ui.app.AppActivity;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.squareup.picasso.Picasso;

import org.joda.time.LocalDateTime;

import java.util.List;
import java.util.Locale;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private List<Reporte> data;

    public ReportAdapter(List<Reporte> data) {
        this.data = data;
    }

    static class ReportViewHolder extends RecyclerView.ViewHolder {

        private ImageView picture;
        private final TextView date;
        private final MaterialCheckBox fixed;

        ReportViewHolder(@NonNull View itemView) {
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

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_reporte, parent, false);

        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        holder.getFixed().setChecked(data.get(position).isFixed());
        holder.getFixed().setText(holder.getFixed().isChecked()
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

        holder.itemView.setOnClickListener(v -> AppActivity.navTo(v, ReportsDirections.actionReportsToReportDetail(position)));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
