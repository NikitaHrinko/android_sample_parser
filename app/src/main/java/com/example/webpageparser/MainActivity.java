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

import com.example.webpageparser.exception.InvalidURLException;
import com.example.webpageparser.exception.PageLoadingException;
import com.example.webpageparser.exception.PageReadingException;
import com.example.webpageparser.parser.DataExtractor;

import java.io.IOException;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    protected Spinner spinnerDepthSelector;
    protected Button startButton;
    protected EditText sourceLink;
    protected TextView parsingResults;
    protected Thread processingThread;

    private Handler messageHandler = new Handler() {
        public void handleMessage(Message message) {
            super.handleMessage(message);
            parsingResults.setText(message.getData().getString("emails"));
            processingThread = null;
        }
    };

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
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Failed to load requested page")
                        .setCancelable(false)
                        .setNegativeButton("Ok", (dialog, id) -> dialog.cancel());
                builder.setMessage("Parsing is still in progress, please wait till it ends");
                AlertDialog alert = builder.create();
                alert.show();
                return;
//            } else if (!isConnected()) {
//                builder.setMessage("No internet connection!");
            } else {
                int depth = Integer.valueOf(spinnerDepthSelector.getSelectedItem().toString());
                String sourceLinkText = sourceLink.getText().toString();
                processingThread = new Thread() {
                    @Override
                    public void run() {
                        DataExtractor dataExtractor = new DataExtractor();
                        Set<String> emails = null;
                        try {
                            emails = dataExtractor.getEmails(sourceLinkText, depth);
                        } catch (InvalidURLException e) {
                            e.printStackTrace();
//                            builder.setMessage("The specified link is invalid: " + e.getMessage());
                        } catch (PageLoadingException e) {
                            e.printStackTrace();
//                            builder.setMessage("Something went wrong while loading requested page: " + e.getMessage());
                        } catch (PageReadingException e) {
                            e.printStackTrace();
//                            builder.setMessage("Something went wrong while reading requested page: " + e.getMessage());
                        } catch (IOException e) {
                            e.printStackTrace();
//                            builder.setMessage("Unrecognized error has occurred " + e.getMessage());
                        }
                        if (emails == null) {
//                            AlertDialog alert = builder.create();
//                            alert.show();
                        } else {
                            Message message = Message.obtain();
                            message.what = 1;
                            Bundle bundle = new Bundle();
                            bundle.putString("emails", String.join("\n", emails));
                            message.setData(bundle);
                            messageHandler.sendMessage(message);
                        }
                    }
                };
                processingThread.start();
            }
        });
    }
}
