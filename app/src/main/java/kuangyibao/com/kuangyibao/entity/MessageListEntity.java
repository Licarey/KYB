package kuangyibao.com.kuangyibao.entity;

import java.util.List;

/**公告实体类
 * Created by apple on 18-4-3.
 */

public class MessageListEntity extends BaseEntity {
    private List<MessageEntity> noticeList;

    public List<MessageEntity> getNoticeList() {
        return noticeList;
    }

    public void setNoticeList(List<MessageEntity> noticeList) {
        this.noticeList = noticeList;
    }
}
