package aftnos.aftourismserver.admin.controller;

import aftnos.aftourismserver.common.result.Result;
import aftnos.aftourismserver.common.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/")
public class TestController {
    @Autowired
    private RedisTemplate redisTemplate;
    public void test() {
        redisTemplate.opsForValue().append("test", "test");
    }
    @GetMapping("1")
    public Result test1() {
        log.info("test");
        return Result.success();
    }
    @GetMapping("2")
    public Result<String> test2() {
        log.info("test");
        return Result.success("OK");
    }
    @GetMapping("3")
    public Result test3() {
        log.info("test");
        return Result.error("error");
    }
    @GetMapping("4")
    public Result<Void> test4() {
        log.info("test");
        return Result.error(ResultCode.PARAM_ERROR);
    }
    @GetMapping("5")
    public Result<List<String>> test5() {
        log.info("test");
        return Result.error(ResultCode.PARAM_ERROR);
    }
}
