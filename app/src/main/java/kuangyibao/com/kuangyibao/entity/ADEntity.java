package kuangyibao.com.kuangyibao.entity;

/**广告实体类
 * Created by apple on 18-4-3.
 */

public class ADEntity extends BaseEntity {
    private String appAdvImg;
    private String appAdvHref;
    private String appAdvType;
    private String ver;
    private String advVer;
    private String token;


    public String getAppAdvImg() {
        return appAdvImg;
    }

    public void setAppAdvImg(String appAdvImg) {
        this.appAdvImg = appAdvImg;
    }

    public String getAppAdvHref() {
        return appAdvHref;
    }

    public void setAppAdvHref(String appAdvHref) {
        this.appAdvHref = appAdvHref;
    }

    public String getAppAdvType() {
        return appAdvType;
    }

    public void setAppAdvType(String appAdvType) {
        this.appAdvType = appAdvType;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAdvVer() {
        return advVer;
    }

    public void setAdvVer(String advVer) {
        this.advVer = advVer;
    }
}
