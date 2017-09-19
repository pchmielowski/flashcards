package net.chmielowski.fiszki;

import android.util.Log;

import com.annimon.stream.Stream;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface AwsService {
    @GET("prod/first-apex-project_hello/")
    Call<Void> sendWord(@Query("word") String word);
}
/*
TODO:
1. use POST
2. send whole collection
3. auth
 */

final class RestService {
    void send(List<String> basicWords) {
        Log.d("pchm", "send " + basicWords);
        final AwsService service = new Retrofit.Builder()
                .baseUrl("https://19ozjdhydh.execute-api.eu-central-1.amazonaws.com/")
                .build()
                .create(AwsService.class);
        Stream.of(basicWords).forEach((word) -> {
            Log.d("pchm", "enqueue " + word);
            service.sendWord(word.trim()).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Log.d("pchm", "on response");
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.d("pchm", "on Failure " + t);
                }
            });
        });
    }
}