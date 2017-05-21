package com.X.xnet;

import com.X.model.CityModel;
import com.X.model.HomeModel;
import com.X.model.UserModel;

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

 String APPUrl = "http://tg01.sssvip.net/mapi/";

    @GET("?ctl=city&act=app_index&r_type=1&isapp=true")
    Observable<HttpResult<CityModel>> city_app_index();

   @GET("?ctl=city&act=city_change&r_type=1&isapp=true")
    Observable<HttpResult<Object>> city_city_change(@Query("city_id") String city_id);

    @GET("?ctl=index&act=index&r_type=1&isapp=true")
    Observable<HttpResult<HomeModel>> app_index(@Query("city_id") String city_id);

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

 @Multipart
 @POST("?ctl=uc_account&act=do_renzheng&r_type=1&isapp=true")//用户认证
 Observable<HttpResult<Object>> user_do_renzheng(@PartMap Map<String, RequestBody> params);

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


