package app.com.kucingapps.Retrofit;

import android.database.Observable;

import java.util.List;

import javax.xml.transform.Result;

import app.com.kucingapps.Model.Article;
import app.com.kucingapps.Model.Category;
import app.com.kucingapps.Model.Pets;
import app.com.kucingapps.Model.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("login.php")
    Call<User> loginRequest(@Field("email") String email,
                                  @Field("password") String password);

    @FormUrlEncoded
    @POST("register.php")
    Call<ResponseBody> registerRequest(@Field("name") String name,
                                       @Field("email") String email,
                                       @Field("password") String password);

    @POST("get_pets.php")
    Call<List<Pets>> getPets();

    @FormUrlEncoded
    @POST("get_pets_pemilik.php")
    Call<List<Pets>> getPetsPemilik(@Field("uid") int uid);

    @FormUrlEncoded
    @POST("get_user.php")
    Call<User> getUser(@Field("id") int id);

    @FormUrlEncoded
    @POST("add_pet.php")
    Call<Pets> insertPet(
            @Field("key") String key,
            @Field("name") String name,
            @Field("species") String species,
            @Field("breed") String breed,
            @Field("gender") int gender,
            @Field("birth") String birth,
            @Field("picture") String picture,
            @Field("uid") int uid,
            @Field("pid") String pid,
            @Field("riwayat") String riwayat,
            @Field("kesehatan") String kesehatan,
            @Field("vaksinasi") String vaksinasi,
            @Field("pengobatan") String pengobatan);

    @FormUrlEncoded
    @POST("update_pet.php")
    Call<Pets> updatePet(
            @Field("key") String key,
            @Field("id") int id,
            @Field("name") String name,
            @Field("species") String species,
            @Field("breed") String breed,
            @Field("gender") int gender,
            @Field("birth") String birth,
            @Field("picture") String picture,
            @Field("pid") String pid,
            @Field("riwayat") String riwayat,
            @Field("kesehatan") String kesehatan,
            @Field("vaksinasi") String vaksinasi,
            @Field("pengobatan") String pengobatan);

    @FormUrlEncoded
    @POST("delete_pet.php")
    Call<Pets> deletePet(
            @Field("key") String key,
            @Field("id") int id,
            @Field("picture") String picture);

    @FormUrlEncoded
    @POST("get_artikel.php")
    Call<List<Article>> getArticle(@Field("mid") String mID);

    @POST("get_manajemen.php")
    Call<List<Category>> getCategory();

    @FormUrlEncoded
    @POST("update_user.php")
    Call<User> updateUser(
            @Field("key") String key,
            @Field("id") int id,
            @Field("name") String name,
            @Field("email") String email,
            @Field("picture") String picture,
            @Field("address") String address,
            @Field("phone") String phone);
}
