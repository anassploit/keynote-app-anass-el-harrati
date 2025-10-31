package net.anassploit.conferencesevice.controllers;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/me")
    public Map<String, Object> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("username", jwt.getClaimAsString("preferred_username"));
        userInfo.put("email", jwt.getClaimAsString("email"));
        userInfo.put("name", jwt.getClaimAsString("name"));
        userInfo.put("roles", jwt.getClaimAsMap("realm_access").get("roles"));
        return userInfo;
    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().invalidate(); // Invalidate local session
        String logoutUrl = "http://localhost:8080/realms/keynote-app/protocol/openid-connect/logout?redirect_uri=http://localhost:8888/conference-service/api/conferences";
        response.sendRedirect(logoutUrl);
    }


    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public List<String> getAllUsers() {
        return List.of("user1", "user2", "user3");
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String createUser(@RequestBody Map<String, String> user) {
        return "User created: " + user.get("username");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteUser(@PathVariable String id) {
        return "User deleted: " + id;
    }
}
/*

        ## Step 4: Test the Integration

### 4.1 Start Your Services
1. Start Eureka (Discovery Service) - port 8761
        2. Start Config Service (if you have one)
3. Start Gateway Service - port 8081
        4. Start User Service - port 8082
        5. Start any other services

### 4.2 Test with Browser

1. **Login Flow:**
        - Go to: `http://localhost:8081/api/users/me`
        - You'll be redirected to Keycloak login
        - Login with: `testuser` / `password123`
        - You'll be redirected back and see your user info

        2. **Test Role-Based Access:**
        - `http://localhost:8081/api/users` (needs USER role) ✅
        - `http://localhost:8081/api/users` (POST - needs ADMIN role) ✅

        ### 4.3 Test with Postman

1. **Get Access Token:**
        ```
POST http://localhost:8080/realms/microservices-realm/protocol/openid-connect/token

Body (x-www-form-urlencoded):
        - client_id: gateway-client
   - client_secret: YOUR_CLIENT_SECRET
   - username: testuser
   - password: password123
   - grant_type: password
```

        2. **Use Token:**
        ```
GET http://localhost:8081/api/users/me

Headers:
Authorization: Bearer YOUR_ACCESS_TOKEN
*/
