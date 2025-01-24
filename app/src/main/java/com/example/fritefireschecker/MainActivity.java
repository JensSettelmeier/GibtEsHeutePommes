package com.example.fritefireschecker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView outputTextView;
    private Button checkButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        outputTextView = findViewById(R.id.outputTextView);
        checkButton = findViewById(R.id.checkButton);

        // Set up button click listener
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Replace the following with actual availability logic
                boolean isAvailable = getFriteFriesAvailability();

                if (isAvailable) {
                    outputTextView.setText(R.string.output_yes);
                } else {
                    outputTextView.setText(R.string.output_no);
                }
            }
        });
    }

    /**
     * Dummy method to simulate availability check.
     * Replace this with actual logic to fetch data from the Mensa website.
     */
    private boolean getFriteFriesAvailability() {
        // For demonstration, randomly return true or false
        return Math.random() < 0.5;
    }
}
