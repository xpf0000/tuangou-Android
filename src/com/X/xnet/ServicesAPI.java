package com.X.xnet;

import com.X.model.CouponModel;
import com.X.model.NewsCateModel;
import com.X.model.NewsModel;
import com.X.model.NoticeModel;
import com.X.model.OrderModel;
import com.X.model.PayModel;
import com.X.model.CityModel;
import com.X.model.HomeModel;
import com.X.model.NearbyModel;
import com.X.model.RenzhengModel;
import com.X.model.StoresListModel;
import com.X.model.TuanCateModel;
import com.X.model.TuanModel;
import com.X.model.TuanNavModel;
import com.X.model.TuanQuanModel;
import com.X.model.UserCollectModel;
import com.X.model.UserCommentModel;
import com.X.model.UserFenhongModel;
import com.X.model.UserModel;
import com.X.model.UserUnitsModel;

import java.util.List;
import java.util.Map;
import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by X on 2016/10/1.
 */

public interface ServicesAPI {

 String APPUrl = "http://www.tcbjpt.com/mapi/";

   @GET("?ctl=user&act=loginout&r_type=1&isapp=true")
   Observable<HttpResult<Object>> do_logout();

    @GET("?ctl=city&act=app_index&r_type=1&isapp=true")
    Observable<HttpResult<CityModel>> city_app_index();

   @GET("?ctl=city&act=city_change&r_type=1&isapp=true")
    Observable<HttpResult<Object>> city_city_change(@Query("city_id") String city_id);

    @GET("?ctl=index&act=app_index&r_type=1&isapp=true")
    Observable<HttpResult<HomeModel>> app_index(
            @Query("city_id") String city_id,
            @Query("xpoint") double xpoint,
            @Query("ypoint") double ypoint
    );

    @GET("?ctl=sms&act=send_sms_code&r_type=1&isapp=true&unique=1")
    Observable<HttpResult<Object>> sms_send_code(@Query("mobile") String mobile);

  @GET("?ctl=sms&act=check_sms_code&r_type=1&isapp=true")
  Observable<HttpResult<Object>> check_sms_code(@Query("mobile") String mobile,@Query("code") String code);

 @POST("?ctl=user&act=app_doregister&r_type=1&isapp=true")
 Observable<HttpResult<UserModel>> user_doregist(
         @Query("mobile") String mobile,
         @Query("pass") String pass,
         @Query("code") String code,
         @Query("tj") String tj
 );

 @POST("?ctl=user&act=dologin&r_type=1&isapp=true")
 Observable<HttpResult<UserModel>> user_dologin(
         @Query("user_key") String user_key,
         @Query("user_pwd") String pass
 );

 @POST("?ctl=user&act=getUinfo&r_type=1&isapp=true")
 Observable<HttpResult<UserModel>> user_getUinfo(
         @Query("uid") String uid,
         @Query("uname") String uname
 );

 @POST("?ctl=user&act=getRenzhenginfo&r_type=1&isapp=true")
 Observable<HttpResult<RenzhengModel>> user_getRenzhenginfo(
         @Query("uid") String uid
 );

 @POST("?ctl=user&act=getUnitsInfo&r_type=1&isapp=true")
 Observable<HttpResult<UserUnitsModel>> user_getUnitsInfo(
         @Query("uid") String uid,
         @Query("uname") String uname
 );

 @POST("?ctl=user&act=fenhongCount&r_type=1&isapp=true")
 Observable<HttpResult<UserFenhongModel>> user_fenhongCount(
         @Query("uid") String uid
 );

 @POST("?ctl=uc_collect&act=app_index&r_type=1&isapp=true")
 Observable<HttpResult<UserCollectModel>> user_collectlist(
         @Query("uid") String uid,
         @Query("page") String page
 );

 @POST("?ctl=uc_review&act=app_index&r_type=1&isapp=true")
 Observable<HttpResult<UserCommentModel>> user_commentlist(
         @Query("uid") String uid,
         @Query("page") String page
 );

//附近团购
 @POST("?ctl=tuan&act=app_index&r_type=1&isapp=true")
 Observable<HttpResult<NearbyModel>> tuan_index(
         @Query("page") String page,
         @Query("city_id") String city_id,
         @Query("cate_id") String cate_id,
         @Query("tid") String tid,
         @Query("qid") String qid,
         @Query("order_type") String order_type,
         @Query("xpoint") double xpoint,
         @Query("ypoint") double ypoint
 );


 //商家列表
 @POST("?ctl=stores&act=app_index&r_type=1&isapp=true")
 Observable<HttpResult<StoresListModel>> stores_list(
         @Query("page") String page,
         @Query("city_id") String city_id,
         @Query("cate_id") String cate_id,
         @Query("tid") String tid,
         @Query("qid") String qid,
         @Query("order_type") String order_type,
         @Query("xpoint") double xpoint,
         @Query("ypoint") double ypoint
 );


 //商家搜索
 @POST("?ctl=stores&act=app_index&r_type=1&isapp=true")
 Observable<HttpResult<StoresListModel>> stores_search(
         @Query("page") String page,
         @Query("city_id") String city_id,
         @Query("keyword") String keyword,
         @Query("xpoint") double xpoint,
         @Query("ypoint") double ypoint
 );

 //团购搜索
 @POST("?ctl=tuan&act=app_index&r_type=1&isapp=true")
 Observable<HttpResult<NearbyModel>> tuan_search(
         @Query("page") String page,
         @Query("city_id") String city_id,
         @Query("keyword") String keyword,
         @Query("xpoint") double xpoint,
         @Query("ypoint") double ypoint
 );

 //团购列表排序筛选项
 @POST("?ctl=tuan&act=nav_list&r_type=1&isapp=true")
 Observable<HttpResult<List<TuanNavModel>>> tuan_nav_list();

 //团购列表分类筛选项
 @POST("?ctl=tuan&act=cate_list&r_type=1&isapp=true")
 Observable<HttpResult<List<TuanCateModel>>> tuan_cate_list();

 //团购列表区域筛选项
 @POST("?ctl=tuan&act=quan_list&r_type=1&isapp=true")
 Observable<HttpResult<List<TuanQuanModel>>> tuan_quan_list(
         @Query("city_id") String city_id
 );


 //团购收藏
 @POST("?ctl=deal&act=app_do_collect&r_type=1&isapp=true")
 Observable<HttpResult<Object>> do_collect(
         @Query("uid") String uid,
         @Query("id") String id
 );

 //团购详情
 @POST("?ctl=deal&act=app_info&r_type=1&isapp=true")
 Observable<HttpResult<TuanModel>> deal_info(
         @Query("id") String id
 );


 //获取用户余额
 @POST("?ctl=user&act=money&r_type=1&isapp=true")
 Observable<HttpResult<String>> user_money(
         @Query("uid") String uid,
         @Query("uname") String uname
 );

 //获取团购券码
 @POST("?ctl=uc_coupon&act=info&r_type=1&isapp=true")
 Observable<HttpResult<List<CouponModel>>> uc_coupon_info(
         @Query("uid") String uid,
         @Query("oid") String oid
 );

 //获取团购券码
 @POST("?ctl=uc_order&act=app_do_refund_coupon&r_type=1&isapp=true")
 Observable<HttpResult<Object>> uc_do_refund(
         @Query("uid") String uid,
         @Query("content") String content,
         @Query("item_id") String item_id
 );


 //获取咨询分类
 @GET("?ctl=news&act=all_type&r_type=1&isapp=true")
 Observable<HttpResult<List<NewsCateModel>>> news_all_cate();

 //获取资讯列表
 @POST("?ctl=news&act=getList&r_type=1&isapp=true")
 Observable<HttpResult<List<NewsModel>>> news_list(
         @Query("tid") String tid,
         @Query("page") String page
 );


 //获取公告列表
 @POST("?ctl=notice&act=index&r_type=1&isapp=true")
 Observable<HttpResult<NoticeModel>> notice_list(
         @Query("page") String page
 );

    //订单提交
    @POST("?ctl=cart&act=app_done&r_type=1&isapp=true")
    Observable<HttpResult<PayModel>> do_order_pay(
            @Query("city_id") String city_id,
            @Query("payment") String payment,
            @Query("all_account_money") String all_account_money,
            @Query("did") String did,
            @Query("uid") String uid,
            @Query("uname") String uname,
            @Query("num") String num,
            @Query("tprice") String tprice
    );

 //继续支付
 @POST("?ctl=uc_order&act=do_pay&r_type=1&isapp=true")
 Observable<HttpResult<PayModel>> uc_order_pay(
         @Query("oid") String oid,
         @Query("uid") String uid
 );


//我的订单
 @POST("?ctl=uc_order&act=app_index&r_type=1&isapp=true")
 Observable<HttpResult<OrderModel>> user_orderlist(
         @Query("uid") String uid,
         @Query("status") String status,
         @Query("page") String page
 );

 @Multipart
 @POST("?ctl=dp&act=app_add_dp&r_type=1&isapp=true")//用户提交评论
 Observable<HttpResult<Object>> user_add_dp(@PartMap Map<String, RequestBody> params);

 @Multipart
 @POST("?ctl=uc_account&act=do_renzheng&r_type=1&isapp=true")//用户认证
 Observable<HttpResult<Object>> user_do_renzheng(@PartMap Map<String, RequestBody> params);

 @Multipart
 @POST("?ctl=uc_account&act=app_upload_avatar&r_type=1&isapp=true")//用户上传头像
 Observable<HttpResult<Object>> app_upload_avatar(@PartMap Map<String, RequestBody> params);

 @Multipart
 @POST("?service=User.headEdit")//上传用户头像
 Observable<HttpResult<Object>> userHeadEdit(@PartMap Map<String, RequestBody> params);

 @POST("?service=User.userEdit")  //完善个人信息
 Observable<HttpResult<Object>> userUserEdit(
         @Query("username") String username,
         @Query("nickname") String nickname,
         @Query("sex") String sex,
         @Query("truename") String truename,
         @Query("birthday") String birthday,
         @Query("address") String address,
         @Query("aihao") String aihao,
         @Query("qianming") String qianming
 );

 @GET("?service=User.getOrNickname")  //检测昵称是否重复
 Observable<HttpResult<Object>> userGetOrNickname(@Query("nickname") String nickname);

}


