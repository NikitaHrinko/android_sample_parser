package com.example.webpageparser;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.webpageparser.interaction.MessageDispatcher;
import com.example.webpageparser.interaction.MessageType;
import com.example.webpageparser.parser.DataExtractor;

import java.util.LinkedList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    protected Spinner spinnerDepthSelector;
    protected Button startButton;
    protected EditText sourceLink;
    protected TextView parsingResults;
    protected Thread processingThread;

    protected LinkedList<String> invalids;

    private Handler messageHandler = new Handler() {
        public void handleMessage(Message message) {
            super.handleMessage(message);
            Bundle messageData = message.getData();
            for (MessageType type : MessageType.ERRORS) {
                String messageDataString = messageData.getString(type.toString());
                if (messageDataString != null && !messageDataString.isEmpty()) {
                    invalids.add(messageDataString);
                }
            }

            String emails = messageData.getString("emails");
            if (emails != null && !emails.isEmpty()) {
                parsingResults.setText(emails);
                processingThread = null;
                if (invalids != null && !invalids.isEmpty()) {
                    showAlert("Errors encountered at the following pages",
                            String.join("\n", invalids));
                }
            }
        }
    };

    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(title)
                .setCancelable(false)
                .setNegativeButton("Ok", (dialog, id) -> dialog.cancel());
        builder.setMessage(message);
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerDepthSelector = findViewById(R.id.spinnerDepth);

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.s_depth_input_values, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDepthSelector.setAdapter(arrayAdapter);

        sourceLink = findViewById(R.id.sourceLink);

        parsingResults = findViewById(R.id.textViewParsingResults);

        startButton = findViewById(R.id.buttonStart);

        startButton.setOnClickListener(view -> {
            if (processingThread != null) {
                showAlert("Failed to load requested page",
                        "Parsing is still in progress, please wait till it ends");
            } else {
                int depth = Integer.valueOf(spinnerDepthSelector.getSelectedItem().toString());
                String sourceLinkText = sourceLink.getText().toString();
                processingThread = new Thread() {
                    @Override
                    public void run() {
                        invalids = new LinkedList<>();
                        DataExtractor dataExtractor = new DataExtractor(messageHandler);
                        Set<String> emails = dataExtractor.getEmails(sourceLinkText, depth);

                        MessageDispatcher.dispatch(messageHandler, "emails", String.join("\n", emails));
                    }
                };
                processingThread.start();
            }
        });
    }
}
