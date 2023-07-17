package com.arv.auth.controller;

import com.arv.auth.helper.GlobalCommonService;
import com.arv.auth.model.document.User;
import com.arv.auth.repository.crud.UserRepository;
import com.arv.auth.service.MostRecent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SuppressWarnings("unused")
@RestController
@RequestMapping(value = "/features", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserExperienceController {

    @Value("${recent.views-count}")
    protected int viewsCount;

    @Autowired
    protected GlobalCommonService globalCommonService;
    @Autowired
    protected UserRepository userRepository;

    @PostMapping("/view/{id}")
    public ResponseEntity<?> productView(@PathVariable String id) {
        User user = globalCommonService.getCurrentUser();
        List<String> views = user.getRecentViews();
        MostRecent mostRecent = new MostRecent(views, viewsCount);
        mostRecent.addElement(id);
        user.setRecentViews(mostRecent.getRecentElements());
        userRepository.save(user);
        return globalCommonService.getResponseEntityByMessageAndStatus("product view recorded", HttpStatus.OK);
    }
}
