package com.daniel.reportes.task.reporte;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FindPlace extends AsyncTask<Double, Void, String> {

    private Geocoder geocoder;

    public FindPlace(Geocoder geocoder) {
        this.geocoder = geocoder;
    }

    @Override
    protected String doInBackground(Double... latLong) {
        try {
            List<Address> addresses = geocoder.getFromLocation(latLong[0], latLong[1], 1);

            if(addresses.isEmpty()) {
                return "No encontrado!";
            }
            else {
                return addresses.get(0).getAddressLine(0);
            }
        }
        catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }

        return "Error!";
    }
}
