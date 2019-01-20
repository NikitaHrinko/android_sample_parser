package com.example.webpageparser;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
    protected TextView progressStatus;
    protected TextView parsingResults;
    protected Thread processingThread;
    protected ProgressBar progressBar;

    protected LinkedList<String> invalids;

    private Handler messageHandler = new Handler() {
        public void handleMessage(Message message) {
            super.handleMessage(message);
            Bundle messageData = message.getData();
            for (String type : MessageType.ERRORS) {
                String messageDataString = messageData.getString(type);
                if (messageDataString != null && !messageDataString.isEmpty()) {
                    invalids.add(messageDataString);
                }
            }

            final float noProgress = -1.0f;
            float progressPercentage = messageData.getFloat(MessageType.PROGRESS_PERCENTAGE, noProgress);
            if (progressPercentage != noProgress) {
                int maxProgress = progressBar.getMax();
                progressBar.setProgress(((int) (((float) maxProgress) * progressPercentage)));
            }

            String progressStatusText = messageData.getString(MessageType.PROGRESS_STATUS);
            if (progressStatusText != null) {
                progressStatus.setText(progressStatusText);
            }

            String emails = messageData.getString(MessageType.EMAILS);
            if (emails != null && !emails.isEmpty()) {
                parsingResults.setText(emails);
                progressStatus.setText("Finished");
                progressBar.setProgress(progressBar.getMax());
                if (invalids != null && !invalids.isEmpty()) {
                    showAlert("Errors encountered at the following pages",
                            String.join("\n", invalids));
                }
                processingThread = null;
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

        progressStatus = findViewById(R.id.textViewProgressStatus);

        parsingResults = findViewById(R.id.textViewParsingResults);

        startButton = findViewById(R.id.buttonStart);

        progressBar = findViewById(R.id.progressBar);

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
                        MessageDispatcher.dispatch(messageHandler, MessageType.PROGRESS_PERCENTAGE, 0.0f);
                        MessageDispatcher.dispatch(messageHandler, MessageType.PROGRESS_STATUS, "");
                        invalids = new LinkedList<>();
                        DataExtractor dataExtractor = new DataExtractor(messageHandler);
                        Set<String> emails = dataExtractor.getEmails(sourceLinkText, depth);

                        MessageDispatcher.dispatch(messageHandler, MessageType.EMAILS, String.join("\n", emails));
                    }
                };
                processingThread.start();
            }
        });
    }
}
