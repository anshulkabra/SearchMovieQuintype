package anshul.org.quintypetask;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import anshul.org.quintypetask.Utils.Toaster;


public abstract class BlundellActivity extends AppCompatActivity {

    private Toaster toaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toaster = new Toaster(this);
    }

    protected void popBurntToast(String msg) {
        toaster.popBurntToast(msg);
    }

    protected void popToast(String msg) {
        toaster.popToast(msg);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        toaster = null;
    }
}
