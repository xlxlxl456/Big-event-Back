package com.xl.controller;

import com.xl.pojo.Result;
import com.xl.pojo.User;
import com.xl.service.UserService;
import com.xl.utils.JwtUtil;
import com.xl.utils.Md5Util;
import com.xl.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{5,16}$") String username,@Pattern(regexp = "^\\S{5,16}$") String password) {

       User u = userService.findByUserName(username);
       if(u == null){
           userService.register(username,password);
           return Result.success();
       }else{
           return Result.error("This username is exist");
       }
    }

    @PostMapping("/login")
    public Result<String> login(@Pattern(regexp = "^\\S{5,16}$") String username,@Pattern(regexp = "^\\S{5,16}$") String password){
        User loginUser = userService.findByUserName(username);

        if(loginUser == null){
            return Result.error("UserName error");
        }

        if(Md5Util.getMD5String(password).equals(loginUser.getPassword())){
            Map<String,Object> claims = new HashMap<>();
            claims.put("id",loginUser.getId());
            claims.put("username",loginUser.getUsername());
            String token = JwtUtil.genToken(claims);

            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.set(token,token,1, TimeUnit.HOURS);
            return Result.success(token);
        }

        return Result.error("Password error");
    }

    @GetMapping("/userInfo")
    public Result<User> userInfo(){
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = map.get("username").toString();

        User user = userService.findByUserName(username);
        return Result.success(user);
    }

    @PutMapping("/update")
    public Result<User> update(@RequestBody @Validated User user){
        userService.update(user);
        return Result.success();
    }

    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam("avatarUrl") @URL String avatarUrl){
        userService.updateAvatar(avatarUrl);
        return Result.success();
    }

    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody Map<String,String> params,@RequestHeader("Authorization") String token){
        String oldPwd = params.get("oldPassword");
        String newPwd = params.get("newPassword");
        String rePwd = params.get("reNewPassword");

        if(!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)){
            return Result.error("Lack paramerater");
        }

        Map<String,Object> map = ThreadLocalUtil.get();
        User loginUser = userService.findByUserName(map.get("username").toString());

        if(!loginUser.getPassword().equals(Md5Util.getMD5String(oldPwd))){
            return Result.error("Old password error");
        }

        if(!newPwd.equals(rePwd)){
            return Result.error("New password not equal to RePassword");
        }

        userService.updatePwd(newPwd);

        ValueOperations<String,String> ops = stringRedisTemplate.opsForValue();
        ops.getOperations().delete(token);
        return Result.success();
    }
}
