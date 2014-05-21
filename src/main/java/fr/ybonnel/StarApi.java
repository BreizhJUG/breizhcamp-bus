package fr.ybonnel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import fr.ybonnel.modele.ReponseApi;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StarApi {


    private static class DateDeserialiser implements JsonDeserializer<Date> {
        DateFormat dfm = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss", Locale.FRANCE);

        @Override
        public Date deserialize(JsonElement arg0, Type arg1,
                JsonDeserializationContext arg2)
                throws JsonParseException {
            try {
                return dfm.parse(arg0.getAsJsonPrimitive()
                        .getAsString().split("\\+")[0]);
            } catch (ParseException e) {
                throw new JsonParseException(e);
            }
        }
    }


    public static  ReponseApi getReponseApi(String lineId, Integer macroDirection, String stopId) throws IOException {
        try (Reader reader = new InputStreamReader(openInputStream(lineId,
                macroDirection, stopId))) {

            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Date.class,
                    new DateDeserialiser());
            Gson gson = gsonBuilder.create();
            ReponseApi reponseApi = gson.fromJson(reader, ReponseApi.class);
            return reponseApi;
        }
    }


    private static InputStream openInputStream(String lineId,
            Integer macroDirection, String stopId) throws IOException {
        StringBuilder urlString = new StringBuilder(
                "http://data.keolis-rennes.com/json/?cmd=");
        urlString.append("getbusnextdepartures");
        urlString.append("&version=");
        urlString.append("2.1");
        urlString.append("&key=");
        urlString.append("G7JE45LI1RK3W1P");
        urlString.append("&param[mode]=");
        urlString.append("stopline");
        urlString.append("&param[route][]=");
        urlString.append(lineId);
        urlString.append("&param[direction][]=");
        urlString.append(macroDirection);
        urlString.append("&param[stop][]=");
        urlString.append(stopId);

        URL myUrl = new URL(urlString.toString());
        URLConnection connection = myUrl.openConnection();
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);
        return connection.getInputStream();
    }

}
