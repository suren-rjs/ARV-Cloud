package com.arv.auth.helper;

import com.arv.auth.model.document.User;
import com.arv.auth.model.dto.response.ResponseMessage;
import com.arv.auth.repository.crud.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@SuppressWarnings("All")
public class GlobalCommonService {
    public static SimpleDateFormat COMMON_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
    @Autowired
    private UserRepository userRepository;
    @Lazy
    @Autowired
    private RestTemplate restTemplate;

    public static boolean validateDate(Date dueDate) {
        if (dueDate == null) return false;
        return dueDate.before(new Date());
    }

    public static String getConvertedDate(String time) {
        Date date = null;
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(COMMON_DATE_FORMAT.parse(time));
            cal.add(Calendar.HOUR_OF_DAY, 5);
            cal.add(Calendar.MINUTE, 30);
            date = cal.getTime();
        } catch (Exception ignored) {
        }
        return date != null ? COMMON_DATE_FORMAT.format(date.getTime()) : "";
    }

    public static Date getTomorrowDate() {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 1);
        date = c.getTime();
        return date;
    }

    public boolean validateEmail(String email) {
        String regEx = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(email);
        return !matcher.matches();
    }

    public ResponseEntity<ResponseMessage> getResponseEntityByMessageAndStatus(String message, HttpStatus status) {
        return new ResponseEntity<>(new ResponseMessage(message), status);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public ResponseEntity<?> getResponseOfObject(Optional<?> object, HttpStatus status) {
        return new ResponseEntity<>(object, status);
    }

    public User getCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        return userRepository.findOneByUsername(username).orElse(null);
    }

    public File convertMultiPartFileToFile(MultipartFile multipartFile) throws IOException {
        InputStream initialStream = multipartFile.getInputStream();
        byte[] buffer = new byte[initialStream.available()];
        initialStream.read(buffer);

        File targetFile = new File("src/main/resources/tempFile.tmp");

        try (OutputStream outStream = Files.newOutputStream(targetFile.toPath())) {
            outStream.write(buffer);
        }
        return targetFile;
    }

    public List getListOfObjectFromUri(HttpMethod method, String uri, HttpEntity<String> headerEntity, Class<?> requiredClass) {
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);
        restTemplate.setMessageConverters(messageConverters);

        HttpHeaders headers = new HttpHeaders();
        headers.add("user-agent", "Application");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        List list = new ArrayList<>();
        Object[] objects = restTemplate.exchange(uri, method, headerEntity, Object[].class).getBody();
        ObjectMapper mapper = new ObjectMapper();
        if (objects != null)
            list = Arrays.stream(objects).map(object -> mapper.convertValue(object, requiredClass)).collect(Collectors.toList());

        return list;
    }


    public Object getForObject(String uri, Class requestedClass) {
        return restTemplate.getForObject(uri, requestedClass);
    }


    public Object postForObject(String uriWithParams, HttpEntity<String> entity, Class<?> requestedClass) {
        return restTemplate.postForObject(uriWithParams, entity, requestedClass);
    }

    public int getRandomInt(int size) {
        return new Random().nextInt(size);
    }

    public Object convertObject(Object object, Class<?> required) {
        return new ObjectMapper().convertValue(object, required);
    }

    public int getTotalPage(int totalPage) {
        totalPage = totalPage <= 1 ? 0 : (totalPage - 1);
        return totalPage;
    }
}
