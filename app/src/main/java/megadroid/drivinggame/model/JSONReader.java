package megadroid.drivinggame.model;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by megadroids.
 */

public class JSONReader {

    public JSONArray load(Context context ) throws JSONException {

        JSONArray messages = null;
        try {
            FileInputStream stream = context.openFileInput("UserData.json");
            String content = readFullyAsString(stream, "UTF-8");
            messages = (JSONArray) new JSONTokener(content).nextValue();
        } catch (IOException e) {
            Log.e("JSONReadException",e.getMessage());
        }
        return messages;
    }


    public String readFullyAsString(InputStream inputStream, String encoding) throws IOException {

        return readFully(inputStream).toString(encoding);
    }


    private ByteArrayOutputStream readFully(InputStream inputStream)  throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = inputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        return baos;
    }
}
