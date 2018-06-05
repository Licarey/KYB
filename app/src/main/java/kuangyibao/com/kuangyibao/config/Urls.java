package kuangyibao.com.kuangyibao.config;

/**
 * Created by liming on 17/9/30.
email liming@movikr.com
qq 313685617
 */

public class Urls {
    /**
     * 微信appid
     */
    public static final String WX_APPID = "wx70a9fd2324d74c10";
    public static final String WX_SECRET = "ff059abe11f6c9d713e49054a90ef4d6";
    /**
     * 新浪 appid
     */
    public static final String SINA_APPKEY = "2064903752";
    /**
     * umeng
     */
    public static final String UMENG_APPKEY = "2064903752";


    public static final String BASEURL = "http://223.223.195.142:5080/kybapi";//http://223.223.195.142:5080/kybapi/

    //广告
    public static final String POST_AD_URL = BASEURL + "/api/ver.shtml";
    //猜价格
    public static final String POST_GUESSPRICE_URL = BASEURL + "/api/guessPrice.shtml";
    //猜价格状态
    public static final String POST_IFGUESS_URL = BASEURL + "/api/ifGuess.shtml";
    //状态提示
    public static final String POST_STATUSTIP_URL = BASEURL + "/api/statusTip.shtml";
    //注册 验证码
    public static final String POST_SENDVAILD_URL = BASEURL + "/api/sendVaild.shtml";
    //注册第一步
    public static final String POST_REGFIRST_URL = BASEURL + "/api/regFirst.shtml";
    //注册第二步
    public static final String POST_REGSECOND_URL = BASEURL + "/api/regSecond.shtml";
    //会员中心
    public static final String POST_SHOWCLIENT_URL = BASEURL + "/api/showClient.shtml";
    //会员中心修改内容
    public static final String POST_UPDATECLIENT_URL = BASEURL + "/api/updateClient.shtml";
    //会员中心修改密码
    public static final String POST_UPDATEPWD_URL = BASEURL + "/api/updatePwd.shtml";
    //公告详情
    public static final String POST_NOTICEDETAIL_URL = BASEURL + "/api/noticeDetail.shtml";
    //公告列表
    public static final String POST_NOTICELIST_URL = BASEURL + "/api/noticeList.shtml";
    //登陆
    public static final String POST_APILOGIN_URL = BASEURL + "/api/apiLogin.shtml";
    //忘记密码 发送验证码
    public static final String POST_SENDVAILDPASS_URL = BASEURL + "/api/sendVaildPass.shtml";
    //忘记密码 第一步
    public static final String POST_PASSFIRST_URL = BASEURL + "/api/passFirst.shtml";
    //忘记密码 第二步
    public static final String POST_PASSSECOND_URL = BASEURL + "/api/passSecond.shtml";
    //是否登录
    public static final String POST_IFLOGIN_URL = BASEURL + "/api/ifLogin.shtml";
    //登出
    public static final String POST_LOGINOUT_URL = BASEURL + "/api/logout.shtml";
    //修改信息获取
    public static final String POST_CLIENT_URL = BASEURL + "/api/client.shtml";
    //是否签到
    public static final String POST_IFSIGN_URL = BASEURL + "/api/ifSign.shtml";
    //签到
    public static final String POST_SIGN_URL = BASEURL + "/api/sign.shtml";
    //订阅列表
    public static final String POST_SHOWSUBS_URL = BASEURL + "/api/showSubs.shtml";
    //我的订阅信息
    public static final String POST_MYSUBS_URL = BASEURL + "/api/mySubs.shtml";
    //发帖
    public static final String POST_BBS_URL = BASEURL + "/api/postBbs.shtml";
    //微信下单
    public static final String POST_WXPREPAY_URL = BASEURL + "/api/wxPrePay.shtml";



    //快讯h5---------------------------------------html5------------------------------------------------------------
    public static final String HTML5_NEWS_URL = BASEURL + "/wap/news/indexNews.shtml";
    public static final String HTML5_PRICE_URL = BASEURL + "/wap/price/indexPrice.shtml";
    public static final String HTML5_STORE_URL = BASEURL + "/wap/stock/indexStock.shtml";
    public static final String HTML5_ZHISHU_URL = BASEURL + "/wap/theindex/index.shtml";
    public static final String HTML5_INDEX_URL = BASEURL + "/wap/index.shtml";
    public static final String HTML5_MINE_HOME_URL = BASEURL + "/wap/member/indexMember.shtml";//我的主页
    public static final String HTML5_MINE_SUPPLYBUY_URL = BASEURL + "/wap/member/mySupplyBuy.shtml";//我的发布
    public static final String HTML5_MINE_FOLLOW_URL = BASEURL + "/wap/member/myFollow.shtml";//我的关注
    public static final String HTML5_MINE_BBS_URL = BASEURL + "/wap/member/myBbs.shtml";//我的帖子
    public static final String HTML5_MINE_REPLY_URL = BASEURL + "/wap/member/myReply.shtml";//我的留言
    public static final String HTML5_DISCLAIMER_URL = BASEURL + "/wap/disclaimer.html";//免费声明

}