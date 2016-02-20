package kr.co.bit.osf.projectlab.phonebook;

import retrofit2.Call;
import retrofit2.http.GET;

// https://futurestud.io/blog/retrofit-getting-started-and-android-client
public interface PhoneBookServiceClient {
    @GET("/firstWeb/phonebook/list.jsp")
    Call<PhoneBookListResult> getList();
}
