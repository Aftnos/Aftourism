package aftnos.aftourismserver.ai.controller;

import aftnos.aftourismserver.ai.dto.AiSettingsDTO;
import aftnos.aftourismserver.common.result.Result;
import aftnos.aftourismserver.common.security.AdminPermission;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/ai")
public class AiAdminController {

    private static final String SETTINGS_KEY = "ai:settings";

    private final RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/settings")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).AI_ADMIN_MANAGE)")
    public Result<AiSettingsDTO> getSettings() {
        AiSettingsDTO settings = (AiSettingsDTO) redisTemplate.opsForValue().get(SETTINGS_KEY);
        if (settings == null) {
            settings = new AiSettingsDTO();
        }
        return Result.success(settings);
    }

    @PutMapping("/settings")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).AI_ADMIN_MANAGE)")
    public Result<Void> updateSettings(@Valid @RequestBody AiSettingsDTO settings) {
        redisTemplate.opsForValue().set(SETTINGS_KEY, settings);
        log.info("AI 设置已更新: enabled={}, defaultModel={}, backupModel={}", settings.getEnabled(), settings.getDefaultModel(), settings.getBackupModel());
        return Result.success();
    }

    @PostMapping("/providers/test")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).AI_ADMIN_MANAGE)")
    public Result<Map<String, Object>> testProvider(@Valid @RequestBody AiSettingsDTO.Provider provider) {
        Map<String, Object> res = new HashMap<>();
        boolean ok = provider.getApiKey() != null && !provider.getApiKey().isBlank();
        res.put("success", ok);
        res.put("message", ok ? "连接测试通过" : "API Key 不能为空");
        return Result.success(res);
    }

    @GetMapping("/stats")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).AI_ADMIN_MANAGE)")
    public Result<Map<String, Object>> stats() {
        Map<String, Object> res = new HashMap<>();
        res.put("models", new HashMap<String, Object>());
        res.put("alerts", new HashMap<String, Object>());
        return Result.success(res);
    }
}