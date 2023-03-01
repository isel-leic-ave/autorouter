package pt.isel.autorouter;

public record ArHttpRoute(String funName, ArVerb method, String path, ArHttpHandler handler) {
}
