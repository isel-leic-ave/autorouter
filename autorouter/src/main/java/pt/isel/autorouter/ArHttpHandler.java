package pt.isel.autorouter;

import java.util.Map;
import java.util.Optional;

public interface ArHttpHandler {
    Optional<?> handle(
            Map<String, String> routeArgs,
            Map<String, String> queryArgs,
            Map<String, String> bodyArgs
    );
}
