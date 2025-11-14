package aftnos.aftourismserver.ai.context;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * AI 模块对当前登录主体的归一化描述。
 */
public class AiPrincipalProfile {

    private final Long principalId;
    private final AiPrincipalType type;
    private final Set<String> allowPermissions;
    private final Set<String> denyPermissions;
    private final Set<String> roleCodes;

    public AiPrincipalProfile(Long principalId,
                              AiPrincipalType type,
                              Set<String> allowPermissions,
                              Set<String> denyPermissions,
                              Set<String> roleCodes) {
        this.principalId = principalId;
        this.type = Objects.requireNonNull(type, "主体类型不能为空");
        this.allowPermissions = allowPermissions == null
                ? Collections.emptySet()
                : Collections.unmodifiableSet(new HashSet<>(allowPermissions));
        this.denyPermissions = denyPermissions == null
                ? Collections.emptySet()
                : Collections.unmodifiableSet(new HashSet<>(denyPermissions));
        this.roleCodes = roleCodes == null
                ? Collections.emptySet()
                : Collections.unmodifiableSet(new HashSet<>(roleCodes));
    }

    public Long getPrincipalId() {
        return principalId;
    }

    public AiPrincipalType getType() {
        return type;
    }

    public Set<String> getAllowPermissions() {
        return allowPermissions;
    }

    public Set<String> getDenyPermissions() {
        return denyPermissions;
    }

    public Set<String> getRoleCodes() {
        return roleCodes;
    }
}
