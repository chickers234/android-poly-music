package api;

import java.util.List;

import model.Song;
import model.SongResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface API {

    @FormUrlEncoded
    @POST("/registerUser")
    Call<ResponseBody> register(@Field("email") String email, @Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("/loginUser")
    Call<ResponseBody> login(@Field("email") String username, @Field("password") String password);

    @GET("/api/songs")
    Call<SongResponse> getSongs();

    @PUT("/api/song/edit/{id}")
    Call<Song> editSong(@Path("id") String id, @Body Song song);

    @DELETE("/api/song/delete/{id}")
    Call<Song> deleteSong(@Path("id") String id);

    @GET("/api/songs/search/{searchItem}")
    Call<List<Song>> search(@Path("searchItem") String searchItem);

    @Streaming
    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlAsync(@Url String fileUrl);
}