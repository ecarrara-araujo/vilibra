package br.eng.ecarrara.vilibra.service;

import br.eng.ecarrara.vilibra.model.BookVolumeCollection;
import retrofit.http.GET;
import retrofit.http.Query;

public interface GoogleBooksServiceInterface {

    @GET("/volumes")
    BookVolumeCollection searchVolumeData(@Query("q") String query, @Query("key") String key);

}
