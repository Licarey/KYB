package kuangyibao.com.kuangyibao.eventMsg;

/**
 * Created by apple on 18-5-8.
 */

public class GetTitleMessage {
    private String title;

    public GetTitleMessage(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
