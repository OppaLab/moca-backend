package com.moca.springboot.service;

import com.moca.springboot.dto.ActivityDTO;
import com.moca.springboot.entity.Activity;
import com.moca.springboot.entity.User;
import com.moca.springboot.repository.ActivityRepository;
import com.moca.springboot.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ActivityService {

    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    CommentRepository commentRepository;

    public Page<ActivityDTO.GetActivitiesResponse> getActivities(long userId, Pageable pageable) {
        Page<Activity> activities = activityRepository.findByToUser(new User(userId), pageable);
        Page<ActivityDTO.GetActivitiesResponse> getActivitiesResponses;

        getActivitiesResponses =
                activities.map(activity -> {
                    ActivityDTO.GetActivitiesResponse getActivitiesResponse = new ActivityDTO.GetActivitiesResponse();
                    getActivitiesResponse.setUserId(activity.getUser().getUserId());
                    getActivitiesResponse.setNickname(activity.getUser().getNickname());
                    getActivitiesResponse.setProfileImageFilePath(activity.getUser().getProfileImageFilePath());
                    if (activity.getPost() != null)
                        getActivitiesResponse.setPostId(activity.getPost().getPostId());
                    if (activity.getReview() != null)
                        getActivitiesResponse.setReviewId(activity.getReview().getReviewId());
                    if (activity.getComment() != null) {
                        getActivitiesResponse.setComment(commentRepository.findById(activity.getComment().getCommentId()).get().getComment());
                    }
                    getActivitiesResponse.setActivity(activity.getActivity());
                    getActivitiesResponse.setCreatedAt((new Date().getTime() - activity.getCreatedAt().getTime()) / 1000);

                    return getActivitiesResponse;
                });

        return getActivitiesResponses;
    }
}
