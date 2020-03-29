package com.daniel.reportes.ui.app.fragment.reportes;

import android.content.Context;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.daniel.reportes.R;
import com.daniel.reportes.data.Reporte;
import com.daniel.reportes.task.reporte.FindPlace;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

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

        try {
            card.getLocation().setText(new FindPlace(new Geocoder(convertView.getContext())).execute(
                    reportes.get(position).getLocation()[0],
                    reportes.get(position).getLocation()[1]).get()
            );
        }
        catch (ExecutionException | InterruptedException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }

        card.getDescription().setText(reportes.get(position).getDescription());

        if (!reportes.get(position).getDescription().equals("Generated")) {
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
