package eddy.customservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by Eddy Medvednic on 30/06/2016.
 */
public class BootCompletedIntentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            SharedPreferences users = context.getSharedPreferences("RegisteredUser", Context.MODE_PRIVATE);
            String user = users.getString("emailAddress", "");
            if (!user.equals("")){ //only start service if user is still logged in
                Intent pushIntent = new Intent(context, MenuService.class);
                context.startService(pushIntent);
            }
        }
    }
}
