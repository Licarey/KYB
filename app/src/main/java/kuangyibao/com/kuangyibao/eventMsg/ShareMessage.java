package kuangyibao.com.kuangyibao.eventMsg;

/**分享消息实体
 * Created by apple on 18-5-8.
 */

public class ShareMessage {
    private String url;
    private String title;
    private String content;
    private String imageUrl;

    public ShareMessage(String url, String title, String content, String imageUrl) {
        this.url = url;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
