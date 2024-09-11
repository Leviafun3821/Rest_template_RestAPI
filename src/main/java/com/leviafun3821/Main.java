package com.leviafun3821;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;




public class Main {

    private static final String BASE_URL = "http://94.198.50.185:7081/api/users";
    private static RestTemplate restTemplate = new RestTemplate();
    private static String sessionId;

    public static void main(String[] args) {

        // 1. Получить список всех пользователей и извлечь sessionId
        ResponseEntity<String> getUsersResponse = restTemplate.getForEntity(BASE_URL, String.class);
        System.out.println("Get Users Response: " + getUsersResponse.getBody());

        // Извлечение sessionId из заголовков ответа
        sessionId = getUsersResponse.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        if (sessionId != null) {
            sessionId = sessionId.split(";")[0];
        }

        System.out.println("Extracted Session ID: " + sessionId);

        // Настройка заголовков с кукисом
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (sessionId != null) {
            headers.add(HttpHeaders.COOKIE, sessionId);
        }

        // 2. Добавить пользователя
        User newUser = new User();
        newUser.setId(3L);
        newUser.setName("James");
        newUser.setLastName("Brown");
        newUser.setAge((byte) 30);

        HttpEntity<User> addUserRequest = new HttpEntity<>(newUser, headers);
        ResponseEntity<String> addUserResponse = restTemplate.exchange(BASE_URL, HttpMethod.POST, addUserRequest, String.class);
        System.out.println("Add User Response: " + addUserResponse.getBody());

        // 3. Изменить пользователя
        newUser.setName("Thomas");
        newUser.setLastName("Shelby");
        HttpEntity<User> updateEntity = new HttpEntity<>(newUser, headers);

        ResponseEntity<String> updateUserResponse = restTemplate.exchange(BASE_URL, HttpMethod.POST, updateEntity, String.class); // Используем POST для обновления
        System.out.println("Update User Response: " + updateUserResponse.getBody());

        // 4. Удалить пользователя
        HttpEntity<Void> deleteRequest = new HttpEntity<>(headers);
        ResponseEntity<String> deleteUserResponse = restTemplate.exchange(BASE_URL + "/3", HttpMethod.DELETE, deleteRequest, String.class);
        System.out.println("Delete User Response: " + deleteUserResponse.getBody());


        String generalResponse = addUserResponse.getBody() + updateUserResponse.getBody() + deleteUserResponse.getBody();
        System.out.println("General response is: " + generalResponse);
    }

}