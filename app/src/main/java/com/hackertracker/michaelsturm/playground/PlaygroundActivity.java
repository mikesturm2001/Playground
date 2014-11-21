package com.hackertracker.michaelsturm.playground;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.channels.AsynchronousCloseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class PlaygroundActivity extends Activity {

    private static int CONNECTION_TIMEOUT = 60000;
    private static int DATARETRIVAL_TIMEOUT = 60000;
    private List<BeaconInfo> webServiceList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playground);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_playground, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void testMethod(View v) {

        WebServiceTask service = new WebServiceTask();
        service.execute();
        //String data = ("http://marauders-services.herokuapp.com/api/places/");
        try {
            webServiceList = service.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "Fuck you", Toast.LENGTH_SHORT ).show();
    }

    private class WebServiceTask extends AsyncTask<URL, Integer, List<BeaconInfo>> {
        protected List<BeaconInfo> doInBackground(URL... urls) {
            //String data = ("http://marauders-services.herokuapp.com/api/places/");
            String data = "http://marauders-services.herokuapp.com/api/beacons/";
            disableConnectionReuseIfNecessary();

            HttpURLConnection urlConnection = null;
            try {
                //create connection
                URL urlToRequest = new URL(data);
                urlConnection = (HttpURLConnection) urlToRequest.openConnection();
                urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                urlConnection.setReadTimeout(DATARETRIVAL_TIMEOUT);

                //Handle issues
                int statusCode = urlConnection.getResponseCode();
                if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    //handle unauthorized code here
                } else if (statusCode != HttpURLConnection.HTTP_OK) {
                    //handle any other errors like 404, 500...
                }

                //Create JSON object from content
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                //Read the server response and attempt to parse it as JSON
                Reader reader = new InputStreamReader(in);

                //JSONObject x = JSONObject(getResponseText(in));

                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.setDateFormat("M/d/yy hh:mm a");
                Gson gson = gsonBuilder.create();
                List<BeaconInfo> posts = new ArrayList<BeaconInfo>();
                posts = Arrays.asList(gson.fromJson(reader, BeaconInfo[].class));
                in.close();


                return posts;

            } catch (MalformedURLException e){
                //URL is invalid
            } catch (SocketTimeoutException e) {
                //data retrieval or connection timed out
            } catch (IOException e) {
                //could not read response body
                //could not create input stream
            } finally {
                if(urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return null;

        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute() {
            Toast.makeText(PlaygroundActivity.this, "Complete", Toast.LENGTH_SHORT ).show();
        }
    }

    /**
     * required in order to prevent issues in earlier android version.
     */
    private static void disableConnectionReuseIfNecessary() {
        //see HttpURLConnection API doc
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    private static String getResponseText(InputStream inStream) {
        return new Scanner(inStream).useDelimiter("\\A").next();
    }
}
