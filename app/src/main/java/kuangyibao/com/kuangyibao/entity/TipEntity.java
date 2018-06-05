package kuangyibao.com.kuangyibao.entity;

/**通知实体类
 * Created by apple on 18-4-3.
 *
 * messageCont
 statusVer
 statusTitle
 statusHref
 statusType
 statusCont

 */

public class TipEntity extends BaseEntity {
    private String messageCont;
    private String statusVer;
    private String statusTitle;
    private String statusHref;
    private String statusType;
    private String statusCont;


    @Override
    public String getMessageCont() {
        return messageCont;
    }

    @Override
    public void setMessageCont(String messageCont) {
        this.messageCont = messageCont;
    }

    public String getStatusVer() {
        return statusVer;
    }

    public void setStatusVer(String statusVer) {
        this.statusVer = statusVer;
    }

    public String getStatusTitle() {
        return statusTitle;
    }

    public void setStatusTitle(String statusTitle) {
        this.statusTitle = statusTitle;
    }

    public String getStatusHref() {
        return statusHref;
    }

    public void setStatusHref(String statusHref) {
        this.statusHref = statusHref;
    }

    public String getStatusType() {
        return statusType;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }

    public String getStatusCont() {
        return statusCont;
    }

    public void setStatusCont(String statusCont) {
        this.statusCont = statusCont;
    }
}
