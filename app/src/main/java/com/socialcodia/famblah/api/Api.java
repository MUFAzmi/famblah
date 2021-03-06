package com.socialcodia.famblah.api;


import com.socialcodia.famblah.pojo.ResponseComment;
import com.socialcodia.famblah.pojo.ResponseComments;
import com.socialcodia.famblah.pojo.ResponseDefault;
import com.socialcodia.famblah.pojo.ResponseFeed;
import com.socialcodia.famblah.pojo.ResponseFeeds;
import com.socialcodia.famblah.pojo.ResponseFriends;
import com.socialcodia.famblah.pojo.ResponseLogin;
import com.socialcodia.famblah.pojo.ResponseNotification;
import com.socialcodia.famblah.pojo.ResponseNotificationsCount;
import com.socialcodia.famblah.pojo.ResponseUser;
import com.socialcodia.famblah.pojo.ResponseUsers;
import com.socialcodia.famblah.storage.Constants;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface Api
{
    @FormUrlEncoded
    @POST("login")
    Call<ResponseLogin> login(
            @Field(Constants.USER_EMAIL) String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("register")
    Call<ResponseDefault> register(
            @Field(Constants.USER_NAME) String name,
            @Field(Constants.USER_USERNAME) String username,
            @Field(Constants.USER_EMAIL) String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("forgotPassword")
    Call<ResponseDefault> forgotPassword(@Field(Constants.USER_EMAIL) String email);

    @FormUrlEncoded
    @POST("resetPassword")
    Call<ResponseDefault> resetPassword(
            @Field(Constants.USER_EMAIL) String email,
            @Field("otp") String otp,
            @Field("newPassword") String password
    );

    @GET("users")
    Call<ResponseUsers> getUsers(
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("updatePassword")
    Call<ResponseDefault> updatePassword(
            @Header("token") String token,
            @Field("password") String password,
            @Field("newpassword") String newPassword
    );

    @FormUrlEncoded
    @POST("sendEmailVerfication")
    Call<ResponseDefault> sendEmailVerfication(
            @Field("email") String email
    );

    @GET("feeds")
    Call<ResponseFeeds> getFeeds(
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("feed/like")
    Call<ResponseFeed>  doLike(
            @Header("token")  String token,
            @Field("feedId") int feedId
    );

    @FormUrlEncoded
    @POST("comment/like")
    Call<ResponseComment>  likeFeedComment(
            @Header("token")  String token,
            @Field("commentId") int feedId
    );

    @FormUrlEncoded
    @POST("comment/unlike")
    Call<ResponseComment>  unlikeFeedComment(
            @Header("token")  String token,
            @Field("commentId") int commentId
    );

    @FormUrlEncoded
    @POST("feed/unlike")
    Call<ResponseFeed> doDislike(
            @Header("token") String token,
            @Field("feedId") int feedId
    );

    @FormUrlEncoded
    @POST("feed/delete")
    Call<ResponseDefault> deleteFeed(
            @Header("token") String token,
            @Field("id") String feedId
    );

    @GET("user/{username}/feeds")
    Call<ResponseFeeds> getUserFeeds(
            @Path("username") String username,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("user/update")
    Call<ResponseUser> updateUser(
            @Header("token") String token,
            @Field("name") String name,
            @Field("username") String username,
            @Field("bio") String bio
//            @Field("image") String image
    );

    @Multipart
    @POST("user/update")
    Call<ResponseUser> updateUserWithImage(
            @Header("token") String token,
            @PartMap Map<String,RequestBody> param,
            @Part MultipartBody.Part image
    );

    @FormUrlEncoded
    @POST("feed/post")
    Call<ResponseDefault> postFeed(
            @Header("token") String token,
            @Field("content") String content
    );

    @Multipart
    @POST("feed/post")
    Call<ResponseDefault> postFeedWithImage(
            @Header("token") String token,
            @PartMap Map<String, RequestBody> map,
            @Part MultipartBody.Part file
    );

    @Multipart
    @POST("/requests/post")
    Call<ResponseDefault> postVerificationRequest(
            @Header("token") String token,
            @PartMap Map<String, RequestBody> map,
            @Part MultipartBody.Part image
    );

    @FormUrlEncoded
    @POST("/contacts/post")
    Call<ResponseDefault> postContactUs(
            @Header("token") String token,
            @Field("name") String name,
            @Field("email") String email,
            @Field("message") String message
    );

    @Multipart
    @POST("feed/update")
    Call<ResponseDefault> updateFeedWithImage(
            @Header("token") String token,
            @PartMap Map<String, RequestBody> map,
            @Part MultipartBody.Part image
    );

    @FormUrlEncoded
    @POST("feed/update")
    Call<ResponseDefault> updateFeed(
            @Header("token") String token,
            @Field("feedId") String feedId,
            @Field("content") String content
    );

    @GET("user/{username}")
    Call<ResponseUser> getUserByUsername(
            @Header("token") String token,
            @Path("username") String username
    );

    @GET("user")
    Call<ResponseUser> getMyProfile(
            @Header("token") String token
    );

    @GET("notifications")
    Call<ResponseNotification> getNotifications(
            @Header("token") String token
    );

    @GET("notifications/Count")
    Call<ResponseNotificationsCount> getNotificationsCount(
            @Header("token") String token
    );

    @GET("notifications/Seened")
    Call<ResponseDefault> setNotificationsSeened(
            @Header("token") String token
    );

    @GET("feed/{id}")
    Call<ResponseFeed> getFeedById(
            @Header("token") String token,
            @Path("id") String id
    );


    @FormUrlEncoded
    @POST("comment/post")
    Call<ResponseComment> postFeedComment(
            @Header("token") String token,
            @Field("feedId") String feedId,
            @Field("comment") String comment
    );

    @GET("feed/{feedId}/comments")
    Call<ResponseComments> getComments(
            @Header("token") String token,
            @Path("feedId") String feedId
    );

    @GET("user/{username}/friends")
    Call<ResponseFriends> getFriends(
            @Header("token") String token,
            @Path("username") String username
    );

    @FormUrlEncoded
    @POST("sendFriendRequest")
    Call<ResponseDefault> sendFriendRequest(
            @Header("token") String token,
            @Field("userId") int userId
    );

    @FormUrlEncoded
    @POST("feed/report")
    Call<ResponseDefault> reportFeed(
            @Header("token") String token,
            @Field("feedId") int feedId
    );

    @FormUrlEncoded
    @POST("acceptFriendRequest")
    Call<ResponseDefault> acceptFriendRequest(
            @Header("token") String token,
            @Field("userId") int userId
    );

    @FormUrlEncoded
    @POST("cancelFriendRequest")
    Call<ResponseDefault> cancelFriendRequest(
            @Header("token") String token,
            @Field("userId") int userId
    );

    @FormUrlEncoded
    @POST("deleteFriend")
    Call<ResponseDefault> deleteFriend(
            @Header("token") String token,
            @Field("userId") int userId
    );

    @FormUrlEncoded
    @POST("comment/delete")
    Call<ResponseDefault> deleteFeedComment(
            @Header("token") String token,
            @Field("userId") int id
    );

}
