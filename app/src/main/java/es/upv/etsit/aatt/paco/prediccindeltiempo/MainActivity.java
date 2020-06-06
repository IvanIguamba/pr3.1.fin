package es.upv.etsit.aatt.paco.prediccindeltiempo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.widget.TextView;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    boolean primera_vez = true;
    String TAG= "pepe";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /****/
        TareaAsincrona tarea = new TareaAsincrona();
        tarea.execute("https://opendata.aemet.es/opendata/api/prediccion/especifica/municipio/diaria/49148?api_key=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb3JnZXJvY2hlOThAZ21haWwuY29tIiwianRpIjoiNDE4ZTRkMWMtMjUzNi00MjAzLTg4MzktMzljOGJmMWEyYmIzIiwiaXNzIjoiQUVNRVQiLCJpYXQiOjE1ODc0MTY5MzcsInVzZXJJZCI6IjQxOGU0ZDFjLTI1MzYtNDIwMy04ODM5LTM5YzhiZjFhMmJiMyIsInJvbGUiOiIifQ.TP_iazYAWAJC6pR4ET8B9a50gGJNuwkaoWeW2IDhXww");

        // Creación de tarea asíncrona
        // Ejecución de hilo de tarea asíncrona
    }



    class TareaAsincrona extends AsyncTask <String, String, String> {


        @Override
        protected String doInBackground(String... uri) {
            // Llamada a petición API-REST con la URI o URL indicada en el método
            // .execute. Por último, retorno del string entregado por la llamada
            // a la API-REST
            //

            String respuesta= API_REST(uri[0]);
            //Log.d(TAG, respuesta);
            return respuesta;
        }

        @Override
        protected void onPostExecute(String respuesta) {

            if (respuesta!=null) {
                try {

                    if (primera_vez) {
                        primera_vez = false;

                        // Obtención de la propiedad "datos" del JSON
                        //
                        JSONObject objeto= new JSONObject(respuesta);
                        String datos= objeto.getString("datos");
                        Log.d(TAG, datos);
                        // Creación de una nuevo objeto de TareaAsincrona
                        // Ejecución del hilo correspondiente
                        //

                        //TextView pueblo = (TextView)findViewById(R.id.municipio);
                        //pueblo.setText(datos);

                        TareaAsincrona tarea2 = new TareaAsincrona();
                        tarea2.execute(datos);


                    } else { // segunda vez: recogida de respuesta de la segunda llamada


                        Log.d(TAG, respuesta);
                        JSONArray objeto2= new JSONArray(respuesta);

                        String municipio = objeto2.getJSONObject(0).getString("nombre");
                        int temperatura = objeto2.getJSONObject(0).getJSONObject("prediccion").getJSONArray("dia").getJSONObject(1).getJSONObject("temperatura").getJSONArray("dato").getJSONObject(2).getInt("value");
                        int precipitacion = objeto2.getJSONObject(0).getJSONObject("prediccion").getJSONArray("dia").getJSONObject(1).getJSONArray("probPrecipitacion").getJSONObject(5).getInt("value");
                        int velviento = objeto2.getJSONObject(0).getJSONObject("prediccion").getJSONArray("dia").getJSONObject(1).getJSONArray("viento").getJSONObject(5).getInt("velocidad");
                        String cielo = objeto2.getJSONObject(0).getJSONObject("prediccion").getJSONArray("dia").getJSONObject(1).getJSONArray("estadoCielo").getJSONObject(5).getString("descripcion");

                        TextView pueblo = (TextView)findViewById(R.id.municipio);
                        pueblo.setText(municipio);

                        TextView temp = (TextView)findViewById(R.id.temperatura12_18);
                        temp.setText(String.valueOf(temperatura));

                        TextView precip = (TextView)findViewById(R.id.probPrecipit);
                        precip.setText(String.valueOf(precipitacion) + "%");

                        TextView velvient = (TextView)findViewById(R.id.viento);
                        velvient.setText(String.valueOf(velviento) + " km/h");

                        TextView ciel = (TextView)findViewById(R.id.cielo);
                        ciel.setText(cielo);

                        // Obtencion de las propiedades oportunas del JSON recibido
                        // Aquí ya se puede acceder a la UI, ya que estamos en el hilo
                        // convencional de ejecución, y por tanto ya se puede modificar
                        // el contenido de los TextView que contienen los valores de los datos.

                        /****/


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, "Problemas decodificando JSON");
                }
            }

        } // onPostExecute


    } // TareaAsincrona




    /** La peticion del argumento es recogida y devuelta por el método API_REST.
     Si hay algun problema se retorna null */
    public String API_REST(String uri){

        StringBuffer response = null;

        try {
            url = new URL(uri);
            Log.d(TAG, "URL: " + uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // Detalles de HTTP
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            Log.d(TAG, "Codigo de respuesta: " + responseCode);
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                String output;
                response = new StringBuffer();

                while ((output = in.readLine()) != null) {
                    response.append(output);
                }
                in.close();
            } else {
                Log.d(TAG, "responseCode: " + responseCode);
                return null; // retorna null anticipadamente si hay algun problema
            }
        } catch(Exception e) { // Posibles excepciones: MalformedURLException, IOException y ProtocolException
            e.printStackTrace();
            Log.d(TAG, "Error conexión HTTP:" + e.toString());
            return null; // retorna null anticipadamente si hay algun problema
        }

        return new String(response); // de StringBuffer -response- pasamos a String

    } // API_REST


}
