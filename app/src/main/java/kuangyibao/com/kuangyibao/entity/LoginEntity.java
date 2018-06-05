package kuangyibao.com.kuangyibao.entity;

/**广告实体类
 * Created by apple on 18-4-3.
 */

public class LoginEntity extends BaseEntity {
    private String token;
    private String uId;
    private String loginId;
    private String loginType;//0表示未付费,1表示付费

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }
}
