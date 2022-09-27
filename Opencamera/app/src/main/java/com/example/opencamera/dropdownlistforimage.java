package com.example.opencamera;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


public class dropdownlistforimage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String dropdownval = "Food";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create()).build();
//        RetClient retClient = retrofit.create(RetClient.class);

        Intent intent = getIntent();
        Bitmap a = (Bitmap) intent.getExtras().get("bitmap");

        setContentView(R.layout.activity_dropdownlistforimage);
        Bitmap bitImage=getIntent().getParcelableExtra("bitmap");
        Spinner spinner = findViewById(R.id.spinner1);
        Button button = findViewById(R.id.buttonupload);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.image_category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String url = "http://10.0.2.2:8080/imageupload?image="+ imageToString(a);
//                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//                StringRequest stringRequest = new StringRequest(Request.Method.POST,url,response -> {
//                    Toast.makeText(getApplicationContext(),response, Toast.LENGTH_SHORT).show();
////                    System.out.println("Success");
//
//                },error -> {
//                    Toast.makeText(getApplicationContext(),error.toString().replace("java.net.ConnectException", ""), Toast.LENGTH_LONG).show();
////                    System.out.println("Failure");
//
//
//                }){
//                    @Nullable
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                        Map< String, String> params = new HashMap<>();
//                        params.put("anup", "boss");
////                        Toast.makeText(getApplicationContext(),"anirudh", Toast.LENGTH_SHORT).show();
//
//                        params.put("image", imageToString(a));
//                        return params;
//                    }
//                };
//                requestQueue.add(stringRequest);

                new UploadTask().execute("");
            }

            //https://stackoverflow.com/questions/34276466/simple-httpurlconnection-post-file-multipart-form-data-from-android-to-google-bl
            class UploadTask extends AsyncTask<String, String, String> {

                @Override
                protected String doInBackground(String... strings) {
                    try {

                        String url = "http://10.0.2.2:8080/imageupload?type="+ dropdownval;
                        String charset = "UTF-8";
                        String path = getApplicationContext().getExternalFilesDir("image").getPath()+"filename.jpeg";
                        try (FileOutputStream out = new FileOutputStream(path)) {
                            a.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        File file = new File(path);
                        String boundary = Long.toHexString(System.currentTimeMillis());
                        String CRLF = "\r\n";

                        URLConnection connection;

                        connection = new URL(url).openConnection();
                        connection.setDoOutput(true);
                        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

                        try (OutputStream output = connection.getOutputStream()) {
                            try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true)) {


                                writer.append("--" + boundary).append(CRLF);
                                writer.append("Content-Disposition: form-data; name=\"myfile\"; filename=\"" + "myfile" + "\"").append(CRLF);
                                writer.append("Content-Type: application/octet-stream; charset=" + charset).append(CRLF);
                                writer.append(CRLF).flush();
                                FileInputStream vf = new FileInputStream(file);
                                try {
                                    byte[] b = new byte[1024];
                                    int bytes = 0;
                                    while ((bytes = vf.read(b, 0, b.length)) >= 0) {
                                        output.write(b, 0, bytes);

                                    }
                                } catch (Exception exception) {

                                    Log.d("Error", String.valueOf(exception));

                                }

                                output.flush();
                                writer.append(CRLF).flush();


                                writer.append("--" + boundary + "--").append(CRLF).flush();
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        int res = ((HttpURLConnection) connection).getResponseCode();
                        //System.out.println(res);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }return "";
                }
            }

        });

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        dropdownval = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(), dropdownval, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}