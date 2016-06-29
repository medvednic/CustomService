package eddy.customservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Created by Eddy Medvednic on 6/29/16.
 */

public class MenuService extends Service {

    private RelativeLayout view;
    private Context appContext;
    private WindowManager windowManager;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        //initialize layout parameters
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                200,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 0;
        params.y = 100;
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate view of the side menu
        view = (RelativeLayout) inflater.inflate(R.layout.side_menu, null);
        windowManager.addView(view, params);
        //add click functionality to menu's button
        Button bt = (Button) view.findViewById(R.id.btTest);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(appContext, "Clicked!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        //remove the view if service is stopped
        super.onDestroy();
        if (view != null) windowManager.removeView(view);
    }
}