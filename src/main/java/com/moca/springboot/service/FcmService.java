package com.moca.springboot.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import com.moca.springboot.entity.Activity;
import com.moca.springboot.entity.Post;
import com.moca.springboot.entity.User;
import com.moca.springboot.repository.ActivityRepository;
import com.moca.springboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
public class FcmService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Value("${fcm.service.account.key}")
    private String serviceAccountKeyFilePath;

    @PostConstruct
    public void initialize() {
        try {
            FileInputStream serviceAccount =
                    new FileInputStream(serviceAccountKeyFilePath);

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://moca-a7445.firebaseio.com")
                    .build();
            FirebaseApp.initializeApp(options);

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @Async
    public void sendToRandomUser(long userId, Post newPost, long numberOfRandomUserPushNotification) throws FirebaseMessagingException {
        User user = userRepository.findById(userId).get();
        List<User> usersWhoSubscribedToPushNotification = userRepository.findBySubscribeToPushNotificationAndUserIdNot(true, userId);
        Collections.shuffle(usersWhoSubscribedToPushNotification, new Random(System.currentTimeMillis()));
        List<User> randomUsers = new ArrayList<>();
        if (usersWhoSubscribedToPushNotification.size() < numberOfRandomUserPushNotification)
            numberOfRandomUserPushNotification = usersWhoSubscribedToPushNotification.size();
        for (int i = 0; i < numberOfRandomUserPushNotification; i++)
            randomUsers.add(usersWhoSubscribedToPushNotification.get(i));
        List<String> registrationTokens = new ArrayList<String>();
        for (User randomUser : randomUsers) {
            Activity activity = new Activity();
            activity.setUser(user);
            activity.setToUser(randomUser);
            activity.setActivity("random");
            activity.setPost(newPost);
            activityRepository.save(activity);
            registrationTokens.add(randomUser.getRegistrationToken());
        }
        MulticastMessage message = MulticastMessage.builder()
                .setNotification(Notification.builder()
                        .setTitle(user.getNickname() + "님이 고민글을 무작위로 전달하셨습니다")
                        .setBody(newPost.getPostTitle() + "\n\n" + newPost.getPostBody())
                        .build())
                .addAllTokens(registrationTokens)
                .build();
        BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
// See the BatchResponse reference documentation
// for the contents of response.
        System.out.println(response.getSuccessCount() + " messages were sent successfully");

    }
}
