package kuangyibao.com.kuangyibao.entity;

/**用户信息实体类
 * Created by apple on 18-4-3.
 */

public class UserInfoEntity extends BaseEntity {
    private String imgUrl;
    private String uNName;
    private String uSex;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getuNName() {
        return uNName;
    }

    public void setuNName(String uNName) {
        this.uNName = uNName;
    }

    public String getuSex() {
        return uSex;
    }

    public void setuSex(String uSex) {
        this.uSex = uSex;
    }
}
