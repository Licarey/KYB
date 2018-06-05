package kuangyibao.com.kuangyibao.eventMsg;

/**
 * Created by apple on 18-4-24.
 */

public class ZhishuTitleMessage {
    private boolean isShowBack = false;
    private String title;

    public ZhishuTitleMessage(boolean isShowBack , String title){
        this.isShowBack = isShowBack;
        this.title = title;
    }

    public boolean isShowBack() {
        return isShowBack;
    }

    public void setShowBack(boolean showBack) {
        isShowBack = showBack;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
