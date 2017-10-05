package anindya.fb;

import anindya.fb.datamodel.Result;
import anindya.fb.detailmodel.ItemDetail;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by anind on 4/12/2017.
 */

public interface RetrofitInterface {
    @GET("/fbapi.php")
    Call<Result> getData(@Query("keyword") String keyword);

    @GET("/fbapi.php")
    Call<ItemDetail> getDetails(@Query("id") String id);

    @GET("/fbapi.php")
    Call<Result> getLocationData(@Query("keyword") String keyword ,@Query("latitude") double latitude, @Query("longitude") double longitude);
}

