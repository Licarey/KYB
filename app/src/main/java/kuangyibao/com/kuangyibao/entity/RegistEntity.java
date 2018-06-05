package kuangyibao.com.kuangyibao.entity;

/**广告实体类
 * Created by apple on 18-4-3.
 */

public class RegistEntity extends BaseEntity {
    private String token;
    private String uId;

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
}
