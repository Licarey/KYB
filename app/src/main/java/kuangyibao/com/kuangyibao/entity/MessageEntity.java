package kuangyibao.com.kuangyibao.entity;

/**公告实体类
 * Created by apple on 18-4-3.
 */

public class MessageEntity {
    private String noticeTitle;
    private String noticeCont;
    private String noticeId;
    private String noticeTime;

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeCont() {
        return noticeCont;
    }

    public void setNoticeCont(String noticeCont) {
        this.noticeCont = noticeCont;
    }

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public String getNoticeTime() {
        return noticeTime;
    }

    public void setNoticeTime(String noticeTime) {
        this.noticeTime = noticeTime;
    }
}
