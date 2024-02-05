package com.dam.androidfragments.ui.dashboard;

import static java.lang.String.valueOf;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dam.androidfragments.R;
import com.dam.androidfragments.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private SensorManager sensorManager;
    private Sensor sensor;
    SensorEventListener sensorListener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView posX = binding.posX;
        TextView posY = binding.posY;
        TextView posZ = binding.posZ;

        // Per canviar el color de fons del TextView a blanc
        posX.setBackgroundColor(getResources().getColor(android.R.color.white));
        posY.setBackgroundColor(getResources().getColor(android.R.color.white));
        posZ.setBackgroundColor(getResources().getColor(android.R.color.white));

        sensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                // Valors de l'acceleròmetre en m/s^2
                float xAcc = sensorEvent.values[0];
                float yAcc = sensorEvent.values[1];
                float zAcc = sensorEvent.values[2];

                posX.setText(valueOf(xAcc));
                posY.setText(valueOf(yAcc));
                posZ.setText(valueOf(zAcc));

                // Processament o visualització de dades...
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
                // Es pot ignorar aquesta CB de moment
            }
        };

        // Seleccionem el tipus de sensor (veure doc oficial)
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // registrem el Listener per capturar els events del sensor
        if( sensor!=null ) {
            sensorManager.registerListener(sensorListener,sensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}