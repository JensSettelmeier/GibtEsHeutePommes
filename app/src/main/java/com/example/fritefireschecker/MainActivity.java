package com.example.fritefireschecker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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
                checkMenusAvailability(new AvailabilityCallback() {
                    @Override
                    public void onResult(boolean available) {
                        runOnUiThread(() -> outputTextView.setText(available ? R.string.output_yes : R.string.output_no));
                    }

                    @Override
                    public void onError(String error) {
                        runOnUiThread(() -> outputTextView.setText(error));
                    }
                });
            }
        });
    }

    public interface AvailabilityCallback {
        void onResult(boolean available);
        void onError(String error);
    }

    private void checkMenusAvailability(AvailabilityCallback callback) {
        new Thread(() -> {
            String[] urls = {
                    "https://foodmarket-eth.sv-restaurant.ch/de/menuplaene-foodmarket/green-day/",
                    "https://foodmarket-eth.sv-restaurant.ch/de/menuplaene-foodmarket/grill-bbq/",
                    "https://foodmarket-eth.sv-restaurant.ch/de/menuplaene-foodmarket/pizza-pasta/"
            };
            try {
                for (String url : urls) {
                    Document doc = Jsoup.connect(url).get();
                    Element startElement = doc.getElementById("menu_plan_tab1");
                    Element endElement = doc.getElementById("menu_plan_tab2");
                    if (startElement != null && endElement != null) {
                        String text = extractTextBetweenElements(startElement, endElement).toLowerCase();
                        if (text.contains("pommes") || text.contains("fries")) {
                            callback.onResult(true);
                            return;
                        }
                    }
                }
                callback.onResult(false);
            } catch (Exception e) {
                e.printStackTrace();
                callback.onError("Failed to fetch the page: " + e.getMessage());
            }
        }).start();
    }

    private String extractTextBetweenElements(Element start, Element end) {
        StringBuilder sb = new StringBuilder();
        Element current = start;
        while (current != end && current != null) {
            sb.append(current.text());
            current = current.nextElementSibling();
        }
        return sb.toString();
    }
}
