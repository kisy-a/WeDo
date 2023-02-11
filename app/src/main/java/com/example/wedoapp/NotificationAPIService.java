package com.example.wedoapp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NotificationAPIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAqSZTQAY:APA91bHvdaQHQDQ9Gd0--w7nhBCsTvnuSiXQ4Qix1yflAE59AlOmyF4OxZ-GiF8Li1upZA8ja2Zn64d0S8LZ4YsC-QmpYipz4Oa13M2SS6epXQ3YY23bVvYcQA9dUT6FupSNeDshbwCt"
            }
    )

    @POST("fcm/send")
    Call<NotificationMyResponse> sendNotification(@Body NotificationSender body);
}


