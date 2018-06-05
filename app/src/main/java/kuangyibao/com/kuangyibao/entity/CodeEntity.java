package kuangyibao.com.kuangyibao.entity;

/**广告实体类
 * Created by apple on 18-4-3.
 */

public class CodeEntity extends BaseEntity {
    private String vaild;
    private String smsId;

    public String getVaild() {
        return vaild;
    }

    public void setVaild(String vaild) {
        this.vaild = vaild;
    }

    public String getSmsId() {
        return smsId;
    }

    public void setSmsId(String smsId) {
        this.smsId = smsId;
    }
}
