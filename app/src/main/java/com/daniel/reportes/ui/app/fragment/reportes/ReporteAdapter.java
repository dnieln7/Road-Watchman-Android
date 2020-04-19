package com.daniel.reportes.ui.app.fragment.reportes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.daniel.reportes.R;
import com.daniel.reportes.data.Reporte;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ReporteAdapter extends ArrayAdapter<Reporte> {

    private Context context;
    private List<Reporte> reportes;
    private LayoutInflater inflater;

    public ReporteAdapter(Context context, List<Reporte> reportes) {
        super(context, R.layout.card_reporte, reportes);
        this.context = context;
        this.reportes = reportes;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        View view = convertView;
        ReporteCard card;

        if(view == null) {
            view = inflater.inflate(R.layout.card_reporte, parent, false);
            card = new ReporteCard(view);
            view.setTag(card);
        }
        else {
            card = (ReporteCard) view.getTag();
        }

        Reporte reporte = reportes.get(position);

        card.getLocation().setText(reporte.getLocation_description());
        card.getDescription().setText(reporte.getDescription());
        card.getFixed().setActivated(reporte.isFixed());

        /*if (!reporte.getPicture().equals("")) {
        Picasso.with(context)
                .load(reporte.getPicture())
                .error(R.drawable.reportes)
                .placeholder(R.drawable.reportes)
                .resize(300, 300)
                .onlyScaleDown()
                .into(card.getPicture());
        }*/

        return view;
    }
}
