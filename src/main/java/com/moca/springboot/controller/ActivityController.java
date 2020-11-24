package com.moca.springboot.controller;

import com.moca.springboot.dto.ActivityDTO;
import com.moca.springboot.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ActivityController {

    @Autowired
    ActivityService activityService;

    @GetMapping("/activity")
    public Page<ActivityDTO.GetActivitiesResponse> getActivities(@RequestParam(value = "userId") long userId,
                                                                 @PageableDefault(size = 30, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return activityService.getActivities(userId, pageable);
    }


}
