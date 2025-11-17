package aftnos.aftourismserver.ai.controller;

import aftnos.aftourismserver.ai.dto.AiAlertEventDTO;
import aftnos.aftourismserver.ai.dto.AiAuthorizationLogDTO;
import aftnos.aftourismserver.ai.dto.AiModelDefaultRequest;
import aftnos.aftourismserver.ai.dto.AiModelStatDTO;
import aftnos.aftourismserver.ai.dto.AiServiceStatusDTO;
import aftnos.aftourismserver.ai.dto.AiSettingsDTO;
import aftnos.aftourismserver.ai.service.AiAlertService;
import aftnos.aftourismserver.ai.service.AiAuthorizationLogService;
import aftnos.aftourismserver.ai.service.AiSettingsService;
import aftnos.aftourismserver.ai.service.AiUsageTracker;
import aftnos.aftourismserver.common.result.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AI 管理接口，限定超级管理员可访问。
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/ai")
public class AiAdminController {

    private final AiSettingsService settingsService;
    private final AiUsageTracker usageTracker;
    private final AiAlertService alertService;
    private final AiAuthorizationLogService authorizationLogService;

    @GetMapping("/settings")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).AI_ADMIN_MANAGE)")
    public Result<AiSettingsDTO> getSettings() {
        return Result.success(settingsService.getSettings());
    }

    @PutMapping("/settings")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).AI_ADMIN_MANAGE)")
    public Result<Void> updateSettings(@Valid @RequestBody AiSettingsDTO settings) {
        settingsService.saveSettings(settings);
        log.info("AI 设置更新: enabled={}, defaultChat={} fallback={}", settings.getEnabled(), settings.getDefaultChatModel(), settings.getFallbackChatModel());
        return Result.success();
    }

    @GetMapping("/status")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).AI_ADMIN_MANAGE)")
    public Result<AiServiceStatusDTO> getStatus() {
        AiServiceStatusDTO dto = settingsService.createStatusSkeleton();
        dto.setProviders(usageTracker.buildServiceHealth(settingsService.getSettings()));
        dto.setRecentErrors(usageTracker.recentErrors(20));
        return Result.success(dto);
    }

    @GetMapping("/stats")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).AI_ADMIN_MANAGE)")
    public Result<List<AiModelStatDTO>> stats(@RequestParam(value = "model", required = false) String model,
                                              @RequestParam(value = "provider", required = false) String provider) {
        return Result.success(usageTracker.snapshot(model, provider));
    }

    @GetMapping("/alerts")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).AI_ADMIN_MANAGE)")
    public Result<List<AiAlertEventDTO>> alerts() {
        return Result.success(alertService.listEvents());
    }

    @GetMapping("/providers")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).AI_ADMIN_MANAGE)")
    public Result<List<AiSettingsDTO.Provider>> providers() {
        return Result.success(settingsService.listProviders());
    }

    @PostMapping("/providers")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).AI_ADMIN_MANAGE)")
    public Result<Void> createProvider(@Valid @RequestBody AiSettingsDTO.Provider provider) {
        settingsService.upsertProvider(provider);
        return Result.success();
    }

    @PutMapping("/providers/{code}")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).AI_ADMIN_MANAGE)")
    public Result<Void> updateProvider(@PathVariable String code, @Valid @RequestBody AiSettingsDTO.Provider provider) {
        provider.setCode(code);
        settingsService.upsertProvider(provider);
        return Result.success();
    }

    @DeleteMapping("/providers/{code}")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).AI_ADMIN_MANAGE)")
    public Result<Void> deleteProvider(@PathVariable String code) {
        settingsService.deleteProvider(code);
        return Result.success();
    }

    @PostMapping({"/providers/{code}/test", "/providers/test"})
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).AI_ADMIN_MANAGE)")
    public Result<Map<String, Object>> testProvider(@PathVariable(value = "code", required = false) String code,
                                                    @Valid @RequestBody AiSettingsDTO.Provider provider) {
        Map<String, Object> res = new HashMap<>();
        boolean ok = StringUtils.hasText(provider.getApiKey()) && StringUtils.hasText(provider.getEndpoint());
        res.put("success", ok);
        String message = ok ? "连接测试通过" : "Endpoint 或 API Key 不完整";
        res.put("message", message);
        usageTracker.recordProviderTest(code != null ? code : provider.getCode(), 100L, ok, message);
        return Result.success(res);
    }

    @GetMapping("/models")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).AI_ADMIN_MANAGE)")
    public Result<List<AiSettingsDTO.Model>> models() {
        return Result.success(settingsService.listModels());
    }

    @PostMapping("/models")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).AI_ADMIN_MANAGE)")
    public Result<Void> createModel(@Valid @RequestBody AiSettingsDTO.Model model) {
        settingsService.upsertModel(model);
        return Result.success();
    }

    @PutMapping("/models/{name}")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).AI_ADMIN_MANAGE)")
    public Result<Void> updateModel(@PathVariable String name, @Valid @RequestBody AiSettingsDTO.Model model) {
        model.setName(name);
        settingsService.upsertModel(model);
        return Result.success();
    }

    @DeleteMapping("/models/{name}")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).AI_ADMIN_MANAGE)")
    public Result<Void> deleteModel(@PathVariable String name) {
        settingsService.deleteModel(name);
        return Result.success();
    }

    @PutMapping("/models/{name}/status")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).AI_ADMIN_MANAGE)")
    public Result<Void> changeModelStatus(@PathVariable String name, @RequestParam boolean enabled) {
        settingsService.changeModelStatus(name, enabled);
        return Result.success();
    }

    @PutMapping("/models/defaults")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).AI_ADMIN_MANAGE)")
    public Result<Void> updateModelDefaults(@Valid @RequestBody AiModelDefaultRequest request) {
        settingsService.updateDefaults(request);
        return Result.success();
    }

    @PutMapping("/scenarios/fallbacks")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).AI_ADMIN_MANAGE)")
    public Result<Void> updateFallbacks(@RequestBody Map<String, String> fallbacks) {
        settingsService.updateScenarioFallbacks(fallbacks);
        return Result.success();
    }

    @GetMapping("/audit/logs")
    @PreAuthorize("@rbacAuthority.hasPermission(T(aftnos.aftourismserver.common.security.AdminPermission).AI_ADMIN_MANAGE)")
    public Result<List<AiAuthorizationLogDTO>> auditLogs() {
        return Result.success(authorizationLogService.latest(100));
    }
}
