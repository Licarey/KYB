package kuangyibao.com.kuangyibao.entity;

import java.util.List;

/**订阅产品实体类
 * Created by apple on 18-4-3.
 */

public class SubscribeEntity extends BaseEntity {
    private List<SubscribeEntity.Value> productList;
    private String score = "0";//积分

    public List<SubscribeEntity.Value> getProductList() {
        return productList;
    }

    public void setProductList(List<SubscribeEntity.Value> productList) {
        this.productList = productList;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    /**
     * "productId": 1,
     "productName": "8.88元/每周",
     "productSave": "",
     "productCont": "将订阅 2018-05-09至2018-05-16的内容,订阅后不支持退订和转让"
     */
    public static class Value{
        private String productId;
        private String productName;
        private String productSave;
        private String productCont;
        private String productPrice;//单位分

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProductSave() {
            return productSave;
        }

        public void setProductSave(String productSave) {
            this.productSave = productSave;
        }

        public String getProductCont() {
            return productCont;
        }

        public void setProductCont(String productCont) {
            this.productCont = productCont;
        }

        public String getProductPrice() {
            return productPrice;
        }

        public void setProductPrice(String productPrice) {
            this.productPrice = productPrice;
        }
    }
}
