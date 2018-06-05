package kuangyibao.com.kuangyibao.util;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by apple on 18-4-24.
 */

public class MessageHelper {
    public static void regist(Object s){
        EventBus.getDefault().register(s);
    }

    public static void unRegist(Object s){
        EventBus.getDefault().unregister(s);
    }

    public static void sendMessage(Object s){
        EventBus.getDefault().post(s);
    }
}
