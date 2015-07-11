package ubelab.com.mockandroidasynctask;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView statusText;
    private EditText username, password, verifypassword;

    @VisibleForTesting
    protected TaskFactory factory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusText = (TextView)findViewById(R.id.statusText);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        verifypassword = (EditText)findViewById(R.id.verifypassword);

        factory = new TaskFactory();
    }

    //event handler for create account button
    public void createAccountClick(View view) {
        try {
            JSONObject inputJson = new JSONObject();
                inputJson.put("username",username.getText().toString());
                inputJson.put("password",password.getText().toString());
                inputJson.put("verifypassword",verifypassword.getText().toString());

            RegisterTask task = factory.getTask();
            task.setInputJson(inputJson);
            task.execute(new URL("https://myserviceurl/path"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //our real async task
    public class RegisterTask extends AsyncTask<URL, Integer, Integer> {

        JSONObject inputJson;

        public RegisterTask() {}

        public void setInputJson(JSONObject inputJson) {
            this.inputJson = inputJson;
        }

        @Override
        protected Integer doInBackground(URL... params) {
            //Do you server call
            //We simulate network latency
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return Result.SUCCESS;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {}

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MainActivity.this.showProgress();
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            MainActivity.this.hideProgress();

            //if 0 -> registration is ok
            //if 1 -> registration fails
            if(result == Result.SUCCESS) {
                statusText.setText("REGISTRATION SUCCESSFUL");
                statusText.setTextColor(Color.WHITE);
            }else if(result == Result.FAILURE){
                statusText.setText("REGISTRATION FAILED");
                statusText.setTextColor(Color.RED);
            }
        }
    }

    private void showProgress() {
        findViewById(R.id.progress).setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        findViewById(R.id.progress).setVisibility(View.GONE);
    }

    interface TaskFactoryInterface {
        RegisterTask getTask();
    }

    @VisibleForTesting
    protected class TaskFactory implements  TaskFactoryInterface{
        @Override
        public RegisterTask getTask() {
            return new RegisterTask();
        }
    }
}
