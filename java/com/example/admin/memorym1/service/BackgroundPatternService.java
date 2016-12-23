package com.example.admin.memorym1.service;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.admin.memorym1.data.MemonimoProvider;
import com.example.admin.memorym1.model.BackgroundPattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BackgroundPatternService extends IntentService {

    private final String LOG_TAG = BackgroundPatternService.class.getSimpleName();

    public BackgroundPatternService() {
        super(BackgroundPatternService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        /*HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String randomPatternJsonStr = null;

        String urlColorLovers = "http://www.colourlovers.com/api/patterns?format=json&numResults=4";

       /* try {
            URL url = new URL(urlColorLovers);

            // Ouverture de la connexion
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer stringBuffer = new StringBuffer();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuffer.append(line + "\n");
            }

            randomPatternJsonStr = stringBuffer.toString();

        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
*/
        /*try {
            storePatternDataFromJson(randomPatternJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }*/

    }

    private Void storePatternDataFromJson(String _randomPatternJsonStr) throws JSONException {

        // Déclaration des propriétés Json
        final String PATTERN_IMAGE_URL = "imageUrl";

        JSONArray patternArray = new JSONArray(_randomPatternJsonStr);


        if (patternArray != null && patternArray.length() > 0) {
            // Initialisation du tableau d'images encodées
            List<BackgroundPattern> backgroundPatternList = new ArrayList<BackgroundPattern>();
            // Alimentation du tableau
            for (int i = 0; i < patternArray.length() ; i++) {
                JSONObject patternObject = patternArray.getJSONObject(i);
                String imageUrl = patternObject.getString(PATTERN_IMAGE_URL);
                try {
                    Bitmap bm = BitmapFactory.decodeStream(new URL(imageUrl).openStream());
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    BackgroundPattern backgroundPattern = new BackgroundPattern(baos.toByteArray());
                    backgroundPatternList.add(backgroundPattern);

                } catch (MalformedURLException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                } catch (IOException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                }
            }

            MemonimoProvider.savePatternList(getContentResolver(), backgroundPatternList);
        }

        return null;
    }


}
