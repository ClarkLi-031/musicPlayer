package xyz.qcbyt.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.qcbyt.entity.User;
import xyz.qcbyt.service.UserService;
import xyz.qcbyt.utils.Result;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@CrossOrigin("*")
@RestController
@RequestMapping("/reg")
public class UserController {
    @Autowired
    private UserService service;
//    @RequiresAuthentication 该注解用于需要 认证之后才能访问
    @PostMapping("/register")
    public Result register( @RequestBody @Valid User user){
        if(service.register(user)!=null){ //如果没注册过，才让注册
            service.createLove(user.getUsername()+"_love");     //根据用户名创建一个 个人喜欢的表
            service.createPlaylist(user.getUsername()+"_playlist"); //根据用户名创建一个 歌单
            return Result.succ(200,"注册成功");
        }
       return Result.fail(400,"注册失败,用户名已存在");
    }
    //用户登录，并生成token
    @PostMapping("/login")
    public Map<String,Object> UserLogin(@RequestBody User user){
        Result userlogin = service.userlogin(user);
        Map<String,Object> map=new HashMap<>();
        if(userlogin!=null){
            map.put("data",userlogin.getData());
            map.put("msg","登录成功");
            map.put("code",200);
            return map;
        }
        map.put("msg","登录失败,用户名或者密码不正确");
        map.put("code",400);
        return map;
    }
    @RequestMapping("/findAllUser")
    public List<User> findallUser(){
        return service.findAllUser();
    }
    //根据一个条件查询一个用户
    @RequestMapping("/findOneUser")
    public Result findOneUser(@RequestParam(value = "id",required = false) Integer id,
                              @RequestParam(value = "username",required = false)String username,
                              @RequestParam(value = "role",required = false)Integer role,
                              @RequestParam(value = "registertime",required = false)String registertime){
        User oneUser = service.findOneUser(id, username, role, registertime);
        if (oneUser!=null){
            return Result.succ(oneUser);
        }
        return Result.fail("无数据记录");
    }
}

