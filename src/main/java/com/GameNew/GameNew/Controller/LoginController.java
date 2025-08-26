package com.GameNew.GameNew.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.GameNew.GameNew.Jwt.JwtUtil;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String name = body.get("name");
        String pass = body.get("pass");

        if ("admin".equals(name) && "23".equals(pass)) {
            String token = jwtUtil.generateToken(name);
            return ResponseEntity.ok(Map.of("token", token));
        }
        return ResponseEntity.status(401).body(Map.of("error", "Sai tài khoản hoặc mật khẩu"));
    }
    
    

    @GetMapping("/protected")
    public ResponseEntity<?> protectedApi(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (jwtUtil.validateToken(token)) {
                String user = jwtUtil.extractUsername(token);
                return ResponseEntity.ok(Map.of("message", "Xin chào " + user, "user", user));
            }
        }
        return ResponseEntity.status(403).body(Map.of("error", "Token không hợp lệ"));
    }
}

