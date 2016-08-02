package com.net.wyk.networktraining;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private EditText mUrlEditText;
    //private Button mDownButton;
    private TextView mTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUrlEditText = (EditText) findViewById(R.id.activity_main_url_edit);
        mTextView = (TextView) findViewById(R.id.activity_main_text_view);

    }

    public void myclickHandler(View view) {

        ConnectivityManager activityManage = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = activityManage.getActiveNetworkInfo();
        if (mNetworkInfo != null && mNetworkInfo.isConnected()) {

            //Toast.makeText(MainActivity.this, "is download", Toast.LENGTH_SHORT).show();
            new DownloadWebpageTask().execute(mUrlEditText.getText().toString());
        } else {
            mTextView.setText("is not connect");
        }
    }



   private class DownloadWebpageTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

       @Override
       protected void onPostExecute(String s) {
           //super.onPostExecute(s);
           mTextView.setText(s);
       }

       private String downloadUrl(String myurl) throws IOException {

            InputStream is = null;
            // Only display the first 500 characters of the retrieved
            // web page content.
            int len = 500;
            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                int response = conn.getResponseCode();
                Log.d(TAG, "The response is: " + response);
                is = conn.getInputStream();

                // Convert the InputStream into a string
                String contentAsString = readIt(is, len);
                return contentAsString;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (is != null) {
                    is.close();
                }

            }
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
            return null;

        }

        private String readIt(InputStream is, int len) throws IOException {
            Reader reader = null;
            reader = new InputStreamReader(is, "UTF-8");
            char[] buffer = new char[len];
            reader.read(buffer);
            return new String(buffer);

        }
    }
}
