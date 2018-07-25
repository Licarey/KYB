package kuangyibao.com.kuangyibao.entity;

/**版本检测实体类
 * Created by apple on 18-4-3.
 */

public class CheckVerEntity extends BaseEntity {
    private float ver;
    private String url;

    public float getVer() {
        return ver;
    }

    public void setVer(float ver) {
        this.ver = ver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
