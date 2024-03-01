package com.example.studio;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class python_activity extends AppCompatActivity {
    EditText edtTxt;
    Intent intent;
    Uri mFileUri = null;
    private RequestQueue requestQueue;
    private static final String API_URL = "http://192.168.43.46:3000/compiler"; // Replace with your API URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.python_activity);
        edtTxt = findViewById(R.id.textView);
        requestQueue = Volley.newRequestQueue(this);
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.c_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.create_new) {
            edtTxt.getText().clear();
            return true;
        }
        if (id == R.id.open) {
            filePicker(true);
            return true;
        }
        if (id == R.id.save) {
            filePicker(false);
            return true;
        }
        if (id == R.id.crun) {
            fetchapi();
            return true;
        }
        return true;
    }

    ActivityResultLauncher<Intent> mLaunchFilePicker = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                Intent data = result.getData();
                if (result.getResultCode() == Activity.RESULT_OK && data != null) {
                    handleFileResult(data, true);
                }
            });

    ActivityResultLauncher<Intent> mLaunchFileSaver = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                Intent data = result.getData();
                if (result.getResultCode() == Activity.RESULT_OK && data != null) {
                    handleFileResult(data, false);
                }
            });

    void filePicker(boolean readOrWrite) {
        if (readOrWrite) {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("*/*");
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            mLaunchFilePicker.launch(intent);
        } else {
            intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            intent.setType("text/*");
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            mLaunchFileSaver.launch(intent);
        }
    }

    private void handleFileResult(Intent data, boolean readOrWrite) {
        mFileUri = data.getData();

        if (mFileUri != null) {
            try {
                getContentResolver().takePersistableUriPermission(mFileUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } catch (SecurityException e) {
                e.printStackTrace();
            }

            if (readOrWrite) {
                read();
            } else {
                write();
            }
        }
    }

    void read() {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(getContentResolver().openInputStream(mFileUri));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            StringBuilder builder = new StringBuilder();

            try {
                String fileContentLine;
                while ((fileContentLine = reader.readLine()) != null) {
                    fileContentLine += "\n";
                    builder.append(fileContentLine);
                }
                String fileContent = builder.toString();
                edtTxt.setText(fileContent);
            } finally {
                reader.close();
                inputStreamReader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void write() {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getContentResolver().openOutputStream(mFileUri));
            BufferedWriter writer = new BufferedWriter(outputStreamWriter);
            String textContent = edtTxt.getText().toString();

            try {
                writer.write(textContent);
            } finally {
                writer.close();
                outputStreamWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Modify the fetchapi method to send Python code to the API for execution
    void fetchapi() {
        String code = edtTxt.getText().toString();
        //Toast.makeText(this, code, Toast.LENGTH_SHORT).show();
        String language = "python"; // Modify as needed

        try {
            JSONObject requestData = new JSONObject();
            requestData.put("language", language);
            requestData.put("code", code);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, API_URL, requestData,
                    response -> {
                        try {
                            String output = response.getString("output");
                            // Display the output or perform further actions
                            displayOutput(output);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle API request error
                            handleError(error);
                        }
                    }
            );

            requestQueue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Helper method to display the output in a Toast
    private void displayOutput(String output) {
        //Toast.makeText(this, output, Toast.LENGTH_LONG).show();
        Intent io=new Intent(python_activity.this , output_screen.class);
        io.putExtra("output",output);
        startActivity(io);
    }

    // Helper method to handle API request errors
    private void handleError(VolleyError error) {
        // Handle and display the error, e.g., by showing a Toast
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
    }
}
