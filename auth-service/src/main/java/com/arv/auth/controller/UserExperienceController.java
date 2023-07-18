package com.arv.auth.controller;

import com.arv.auth.helper.GlobalCommonService;
import com.arv.auth.model.document.User;
import com.arv.auth.model.rest.ProductList;
import com.arv.auth.model.rest.Products;
import com.arv.auth.repository.crud.UserRepository;
import com.arv.auth.service.MostRecent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@RestController
@RequestMapping(value = "/features", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserExperienceController {

    @Value("${recent.views-count}")
    protected int viewsCount;
    @Value("${gateway.url}")
    private String gatewayUrl;

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

    @PostMapping("/recent")
    public ResponseEntity<?> getRecent(@RequestParam(required = false, defaultValue = "true") Boolean isRecentViews) throws JsonProcessingException {
        User user = globalCommonService.getCurrentUser();
        String[] productIds = (isRecentViews ? user.getRecentViews() : user.getRecentWishLists()).toArray(new String[0]);

        String uriWithParams = gatewayUrl + "/public/list?page=0";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Set the request body
        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(new Products(productIds));
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        ProductList productList = (ProductList) globalCommonService.postForObject(uriWithParams, entity, ProductList.class);

        return globalCommonService.getResponseOfObject(Optional.of(productList), HttpStatus.OK);
    }
}
