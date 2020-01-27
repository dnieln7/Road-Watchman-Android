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

public class ReportesAdapter extends BaseAdapter {

    private Context context;
    private List<Reporte> reportes;
    private LayoutInflater inflater;

    public ReportesAdapter(Context context, List<Reporte> reportes) {
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

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, parent, false);
        }

        ListItem item = new ListItem(convertView);

      try {
            item.location.setText(new FindPlace(new Geocoder(convertView.getContext())).execute(reportes.get(position).getLocation()[0], reportes.get(position).getLocation()[1]).get());
        }
        catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        item.description.setText(reportes.get(position).getDescription());

        if(reportes.get(position).getPicture() != null && !reportes.get(position).getPicture().equals("")) {
            Picasso.with(context).load(reportes.get(position).getPicture()).placeholder(R.drawable.reportes_logo).into(item.picture);
        }
        else {
            Picasso.with(context).load(R.drawable.reportes_logo).into(item.picture);
        }

        return convertView;
    }
}
