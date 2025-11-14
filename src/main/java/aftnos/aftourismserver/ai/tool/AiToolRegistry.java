package aftnos.aftourismserver.ai.tool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 工具注册表，负责统一维护可用的工具定义。
 */
public class AiToolRegistry {

    private final Map<String, AiServerTool<?>> tools = new ConcurrentHashMap<>();

    public void register(AiServerTool<?> tool) {
        tools.put(tool.name(), tool);
    }

    public Optional<AiServerTool<?>> find(String name) {
        return Optional.ofNullable(tools.get(name));
    }

    public List<AiServerTool<?>> listAll() {
        return Collections.unmodifiableList(new ArrayList<>(tools.values()));
    }
}
