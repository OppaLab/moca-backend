package com.moca.springboot.service;

import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Entity;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import com.moca.springboot.dto.requestDto.PostDTO;
import com.moca.springboot.entity.Post;
import com.moca.springboot.entity.PostEntity;
import com.moca.springboot.entity.User;
import com.moca.springboot.entity.UserEntity;
import com.moca.springboot.repository.PostEntityRepository;
import com.moca.springboot.repository.PostRepository;
import com.moca.springboot.repository.UserEntityRepository;
import com.moca.springboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class NaturalLanguageApiService {

    @Autowired
    private PostEntityRepository postEntityRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private UserRepository userRepository;

    @Async
    public void naturalLanguageApi(PostDTO postDTO, Post post) throws IOException {
        try (LanguageServiceClient language = LanguageServiceClient.create()) {

            // The text to analyze
            String comment = postDTO.getPostTitle() + "\n" + postDTO.getPostBody();

            Document doc = Document.newBuilder().setContent(comment).setType(Document.Type.PLAIN_TEXT).build();

            // Detects the entities of the text
            List<Entity> entities = language.analyzeEntities(doc).getEntitiesList();
            // Detects the sentiment of the text
            Sentiment sentiment = language.analyzeSentiment(doc).getDocumentSentiment();
//            System.out.println(entities);


            List<String> postEntityNames = new ArrayList<>();

            for (int i = 0; i < entities.size(); i++) {
                if (!postEntityNames.contains(entities.get(i).getName()))
                    postEntityNames.add(entities.get(i).getName());
            }

            int entityCount = 20;
            if (postEntityNames.size() < 20)
                entityCount = postEntityNames.size();

            for (int i = 0; i < entityCount; i++) {
                PostEntity postEntity = new PostEntity();
                postEntity.setPost(post);
                postEntity.setEntity(postEntityNames.get(i));
                postEntityRepository.save(postEntity);
            }

            User user = new User();
            user.setUserId(postDTO.getUserId());

            for (int i = 0; i < entityCount; i++) {
                List<UserEntity> userEntities = userEntityRepository.findByUser(user);
                List<String> userEntityNames = new ArrayList<>();
                for (UserEntity userEntity : userEntities) {
                    userEntityNames.add(userEntity.getEntity());
                }
                UserEntity userEntity = new UserEntity();
                userEntity.setEntity(postEntityNames.get(i));
                userEntity.setUser(user);
                userEntity.setLfuCount(1);
                if (userEntities.size() < 20) {
                    userEntityRepository.save(userEntity);
                } else {
                    if (userEntityNames.contains(postEntityNames.get(i))) {
                        UserEntity existingUserEntity = userEntityRepository.findByUserAndEntity(user, postEntityNames.get(i));
                        System.out.println("count up : " + existingUserEntity);
                        existingUserEntity.setLfuCount(existingUserEntity.getLfuCount() + 1);
                        userEntityRepository.save(existingUserEntity);
                    } else {
                        System.out.println("delete: " + userEntities.stream().
                                min(Comparator.comparing(UserEntity::getLfuCount).
                                        thenComparing(UserEntity::getTimeStamp)).get());

                        userEntityRepository.delete(userEntities.stream().
                                min(Comparator.comparing(UserEntity::getLfuCount).
                                        thenComparing(UserEntity::getTimeStamp)).get());
                        userEntityRepository.save(userEntity);
                    }
                }

            }

            post.setPostSentimentScore(sentiment.getScore());
            postRepository.save(post);
            user = userRepository.findById(postDTO.getUserId()).get();
            user.setUserSentimentScore((user.getUserSentimentScore() + post.getPostSentimentScore()) / 2);
            userRepository.save(user);


        }
    }
}
