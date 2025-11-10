package aftnos.aftourismserver.admin.controller;

import aftnos.aftourismserver.admin.pojo.OperationLog;
import aftnos.aftourismserver.admin.mapper.OperationLogMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 操作日志功能测试类
 */
@SpringBootTest
public class OperationLogTest {

    @Autowired
    private OperationLogMapper operationLogMapper;

    @Test
    public void testInsertOperationLog() {
        // 创建操作日志对象
        OperationLog operationLog = new OperationLog();
        operationLog.setTraceId("trace-12345");
        operationLog.setOperatorId(1L);
        operationLog.setOperatorType("ADMIN");
        operationLog.setModuleName("测试模块");
        operationLog.setOperationName("测试操作");
        operationLog.setRequestUri("/test");
        operationLog.setRequestMethod("POST");
        operationLog.setClassMethod("TestController.testMethod");
        operationLog.setRequestParams("[\"param1\", \"param2\"]");
        operationLog.setResponseBody("success");
        operationLog.setSuccessFlag(true);
        operationLog.setCostMs(100);
        operationLog.setIpAddress("127.0.0.1");
        operationLog.setUserAgent("TestClient/1.0");
        operationLog.setCreateTime(LocalDateTime.now());
        operationLog.setUpdateTime(LocalDateTime.now());

        // 插入操作日志
        int result = operationLogMapper.insert(operationLog);
        
        // 验证插入结果
        assertEquals(1, result, "应该成功插入1条记录");
        assertNotNull(operationLog.getId(), "ID应该被正确设置");
        
        System.out.println("成功插入操作日志，ID: " + operationLog.getId());
    }

    @Test
    public void testOperationLogEntity() {
        // 测试操作日志实体类的基本功能
        OperationLog operationLog = new OperationLog();
        
        // 设置属性
        operationLog.setTraceId("trace-67890");
        operationLog.setOperatorId(2L);
        operationLog.setOperatorType("USER");
        operationLog.setModuleName("用户模块");
        operationLog.setOperationName("查询操作");
        operationLog.setRequestUri("/user/query");
        operationLog.setRequestMethod("GET");
        operationLog.setClassMethod("UserController.queryUser");
        operationLog.setRequestParams("[\"userId=1\"]");
        operationLog.setResponseBody("{\"id\":1,\"name\":\"test\"}");
        operationLog.setSuccessFlag(true);
        operationLog.setErrorMsg(null);
        operationLog.setCostMs(50);
        operationLog.setIpAddress("192.168.1.100");
        operationLog.setUserAgent("Mozilla/5.0");
        
        // 验证属性
        assertEquals("trace-67890", operationLog.getTraceId());
        assertEquals(2L, operationLog.getOperatorId());
        assertEquals("USER", operationLog.getOperatorType());
        assertEquals("用户模块", operationLog.getModuleName());
        assertEquals("查询操作", operationLog.getOperationName());
        assertEquals("/user/query", operationLog.getRequestUri());
        assertEquals("GET", operationLog.getRequestMethod());
        assertEquals("UserController.queryUser", operationLog.getClassMethod());
        assertEquals("[\"userId=1\"]", operationLog.getRequestParams());
        assertEquals("{\"id\":1,\"name\":\"test\"}", operationLog.getResponseBody());
        assertTrue(operationLog.getSuccessFlag());
        assertNull(operationLog.getErrorMsg());
        assertEquals(50, operationLog.getCostMs());
        assertEquals("192.168.1.100", operationLog.getIpAddress());
        assertEquals("Mozilla/5.0", operationLog.getUserAgent());
        
        System.out.println("操作日志实体类测试通过");
    }
}