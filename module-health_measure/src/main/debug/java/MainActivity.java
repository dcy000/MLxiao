import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gcml.health.measure.R;
import com.gcml.health.measure.first_diagnosis.fragment.HealthECGDetectionFragment;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame,new HealthECGDetectionFragment()).commit();
    }
}
