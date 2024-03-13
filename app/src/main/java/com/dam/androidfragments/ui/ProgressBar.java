package com.dam.androidfragments.ui;

import static java.lang.String.valueOf;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dam.androidfragments.R;
import com.dam.androidfragments.databinding.FragmentDashboardBinding;
import com.dam.androidfragments.databinding.FragmentProgressBarBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProgressBar#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProgressBar extends Fragment {

    private FragmentProgressBarBinding binding;

    private SensorManager sensorManager;
    private Sensor sensor;
    SensorEventListener sensorListener;
    private long lastTapTime = 0;

    public ProgressBar() {
        // Required empty public constructor
    }

    public static ProgressBar newInstance(String param1, String param2) {
        ProgressBar fragment = new ProgressBar();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProgressBarBinding.inflate(inflater, container, false);
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

                detectDoubleTap(zAcc);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
                // Es pot ignorar aquesta CB de moment
            }
        };

        // Seleccionem el tipus de sensor (veure doc oficial)
        sensorManager = (SensorManager) requireContext().getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // registrem el Listener per capturar els events del sensor
        if( sensor!=null ) {
            sensorManager.registerListener(sensorListener,sensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
        return root;
    }

    private void detectDoubleTap(float zAcc) {
        long now = SystemClock.uptimeMillis();
        long tapInterval = now - lastTapTime;

        if (tapInterval < 1000 && zAcc < -9.0) { // Check if tap interval is less than 1 second and zAcc indicates a tap
            Toast.makeText(requireContext(), "Double Tap Detected!", Toast.LENGTH_SHORT).show();
            lastTapTime = 0; // Reset last tap time after detecting double tap
        } else if (zAcc < -9.0) { // If a tap is detected, update the last tap time
            lastTapTime = now;
        }
    }
}