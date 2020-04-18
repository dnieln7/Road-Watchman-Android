package com.daniel.reportes.ui.app.fragment.reportes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.daniel.reportes.R;
import com.daniel.reportes.data.Reporte;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ReporteAdapter extends BaseAdapter {

    private Context context;
    private List<Reporte> reportes;
    private LayoutInflater inflater;

    public ReporteAdapter(Context context, List<Reporte> reportes) {
        this.context = context;
        this.reportes = reportes;
    }

    @Override
    public int getCount() {
        return reportes.size();
    }

    @Override
    public Object getItem(int position) {
        return reportes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (inflater != null) {
            convertView = inflater.inflate(R.layout.card_reporte, parent, false);
        }

        ReporteCard card = new ReporteCard(convertView);

        card.getLocation().setText(reportes.get(position).getLocation_description());
        card.getDescription().setText(reportes.get(position).getDescription());
        card.getFixed().setActivated(reportes.get(position).isFixed());

        if (!reportes.get(position).getPicture().equals("")) {
            Picasso.with(context)
                    .load(reportes.get(position).getPicture())
                    .placeholder(R.drawable.reportes)
                    .resize(300, 300)
                    .onlyScaleDown()
                    .into(card.getPicture());
        }

        return convertView;
    }
}
