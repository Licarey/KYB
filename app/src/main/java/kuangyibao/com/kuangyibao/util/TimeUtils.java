package kuangyibao.com.kuangyibao.util;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**时间格式化工具类
 * Created by apple on 18-3-25.
 */

public class TimeUtils {
    /**
      时间戳转换成字符窜
     */
    public static String getDateToString(long time, String format) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat(format);
        return sf.format(d);
    }

    /**
      将字符串转为时间戳
     */
    public static long getStringToDate(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = new Date();
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * 是否去拉取通知消息
     * @return
     */
    public static boolean isStartTip(){
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        int mHour = mCalendar.get(Calendar.HOUR);
        int mMinuts=mCalendar.get(Calendar.MINUTE);
        if((mHour == 11 && mMinuts == 30) || (mHour == 17 && mMinuts == 30)){
            return true;

        }
        return false;
    }

    /**
     * 是否是同一天  用于猜价格
     * @param context
     * @return
     */
    public static boolean isSameDay(Context context){
        if("".equals(SpUtils.getString(context , "guessTime" , ""))){
            return false;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date current = new Date();
        String time1 = simpleDateFormat.format(current);
        long storeTime = Long.valueOf(SpUtils.getString(context , "guessTime" , ""));

        Date d = new Date(storeTime);
        String time2 = simpleDateFormat.format(d);
        if(time1.equals(time2)){
            return true;
        }
        return false;
    }
}
