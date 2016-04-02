package speakfree.noobs.com.speakfree;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {
    public static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
                startService(new Intent(getApplication(), ChatHeadService.class));


        finish();
    }
}
