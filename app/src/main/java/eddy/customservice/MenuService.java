package eddy.customservice;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.RelativeLayout;

/**
 * Created by eddy on 6/29/16.
 */

public class MenuService extends Service {

    private RelativeLayout layout;
    private WindowManager windowManager;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layout = new RelativeLayout(this);
        layout.setBackgroundColor(Color.BLUE);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                200,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;
        windowManager.addView(layout, params);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (layout != null) windowManager.removeView(layout);
    }

}