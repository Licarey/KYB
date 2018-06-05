package kuangyibao.com.kuangyibao.eventMsg;

/**
 * Created by apple on 18-5-8.
 */

public class GetTitleMessage {
    private String title;
    private String index;

    public GetTitleMessage(String title , String index) {
        this.title = title;
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}
