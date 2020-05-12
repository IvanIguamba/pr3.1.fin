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

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    boolean primera_vez = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /****/
        // Creación de tarea asíncrona
        // Ejecución de hilo de tarea asíncrona
    }



    class TareaAsincrona extends AsyncTask<***,***,***> {


        @Override
        protected String doInBackground(String[] uri) {
            // Llamada a petición API-REST con la URI o URL indicada en el método
            // .execute. Por último, retorno del string entregado por la llamada
            // a la API-REST
            /****/
            return *****;
        }

        @Override
        protected void onPostExecute(String respuesta) {

            if (respuesta!=null) {
                try {

                    if (primera_vez) {
                        primera_vez = false;

                        // Obtención de la propiedad "datos" del JSON
                        /****/

                        // Creación de una nuevo objeto de TareaAsincrona
                        // Ejecución del hilo correspondiente
                        /****/

                    } else { // segunda vez: recogida de respuesta de la segunda llamada

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
