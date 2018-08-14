package debug;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.gcml.module_face_recognition.R;
import com.gcml.module_face_recognition.RegisterHead2XunfeiActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.face_recognition_activity_main);
    }

    public void startActy(View view) {
        RegisterHead2XunfeiActivity.startActivityForResult(this,"",12);
    }

    public void fnishToHome(View view) {
        finish();
    }
}
