package cn.com.taiji.mongoweb.controller;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

/**
 * 验证登录
 */
@Controller
public class RedisController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 登录页面
     * @return
     */
    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/code1/{phone}")
    public String  sms(Model model, HttpSession session, @PathVariable String phone){
        String code = RandomStringUtils.randomNumeric(4);
        if (stringRedisTemplate.opsForList().size(phone)<3){
            stringRedisTemplate.opsForList().leftPush(phone, code);
            stringRedisTemplate.expire(phone, 600, TimeUnit.SECONDS);
            System.out.println("您的验证码为："+code);
            return "login";
        }else {
            model.addAttribute("bbb","请求失败，请30分钟之后再次请求！");
            return "login";
        }
    }
}
