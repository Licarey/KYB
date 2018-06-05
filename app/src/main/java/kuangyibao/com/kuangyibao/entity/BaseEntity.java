package kuangyibao.com.kuangyibao.entity;

import java.io.Serializable;

/**
 * Created by apple on 18-3-13.
 */

public class BaseEntity implements Serializable {
    private String messageId;
    private String messageCont;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageCont() {
        return messageCont;
    }

    public void setMessageCont(String messageCont) {
        this.messageCont = messageCont;
    }
}
