package aftnos.aftourismserver.admin.controller;

import aftnos.aftourismserver.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 用于测试操作日志功能的控制器
 */
@Slf4j
@RestController
@RequestMapping("/logtest")
public class LogTestController {
    
    @PostMapping("/add")
    public Result<String> add(@RequestBody String data) {
        log.info("添加数据: {}", data);
        return Result.success("添加成功: " + data);
    }
    
    @PutMapping("/update")
    public Result<String> update(@RequestBody String data) {
        log.info("更新数据: {}", data);
        return Result.success("更新成功: " + data);
    }
    
    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Long id) {
        log.info("删除数据，ID: {}", id);
        return Result.success("删除成功，ID: " + id);
    }
    
    @GetMapping("/query")
    public Result<String> query(@RequestParam String keyword) {
        log.info("查询数据，关键字: {}", keyword);
        return Result.success("查询结果: " + keyword);
    }
}