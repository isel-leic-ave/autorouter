package pt.isel;

import pt.isel.autorouter.ArHttpHandler;
import pt.isel.autorouter.ArHttpRoute;
import pt.isel.autorouter.ArVerb;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;

/**
 * Sample of ArHttpHandler objects for Classroom domain without tricks of autorouter.
 */
public class ClassroomBaselineHandlers {
    public static Stream<ArHttpRoute> routes(ClassroomControllerEmpty router) {
        return Stream.of(
                new ArHttpRoute("search", ArVerb.GET, "/classroom/{classroom}", new ArHttpHandler() {
                    @Override
                    public Optional<?> handle(Map<String, String> routeArgs, Map<String, String> queryArgs, Map<String, String> bodyArgs) {
                        return router.search(routeArgs.get("classroom"), queryArgs.get("student"));
                    }
                }
                ),
                new ArHttpRoute("addStudent", ArVerb.PUT, "/classroom/{classroom}/students/{nr}", new ArHttpHandler() {
                    @Override
                    public Optional<?> handle(Map<String, String> routeArgs, Map<String, String> queryArgs, Map<String, String> bodyArgs) {
                        int nr = parseInt(routeArgs.get("nr"));
                        return router.addStudent(
                                routeArgs.get("classroom"),
                                nr,
                                new Student(
                                        nr,
                                        bodyArgs.get("name"),
                                    parseInt(bodyArgs.get("group")),
                                    parseInt(bodyArgs.get("semester"))));
                    }
                }
                ),
                new ArHttpRoute("removeStudent", ArVerb.DELETE, "/classroom/{classroom}/students/{nr}", new ArHttpHandler() {
                    @Override
                    public Optional<?> handle(Map<String, String> routeArgs, Map<String, String> queryArgs, Map<String, String> bodyArgs) {
                        return router.removeStudent(
                                routeArgs.get("classroom"),
                                parseInt(routeArgs.get("nr")));
                    }
                }
                )
        );
    }

    public static Stream<ArHttpRoute> routes(ClassroomController router) {
        return Stream.of(
                new ArHttpRoute("search", ArVerb.GET, "/classroom/{classroom}", new ArHttpHandler() {
                    @Override
                    public Optional<?> handle(Map<String, String> routeArgs, Map<String, String> queryArgs, Map<String, String> bodyArgs) {
                        return router.search(routeArgs.get("classroom"), queryArgs.get("student"));
                    }
                }
                ),
                new ArHttpRoute("addStudent", ArVerb.PUT, "/classroom/{classroom}/students/{nr}", new ArHttpHandler() {
                    @Override
                    public Optional<?> handle(Map<String, String> routeArgs, Map<String, String> queryArgs, Map<String, String> bodyArgs) {
                        int nr = parseInt(routeArgs.get("nr"));
                        return router.addStudent(
                                routeArgs.get("classroom"),
                                nr,
                                new Student(
                                    nr,
                                    bodyArgs.get("name"),
                                    parseInt(bodyArgs.get("group")),
                                    parseInt(bodyArgs.get("semester"))));
                    }
                }
                ),
                new ArHttpRoute("removeStudent", ArVerb.DELETE, "/classroom/{classroom}/students/{nr}", new ArHttpHandler() {
                    @Override
                    public Optional<?> handle(Map<String, String> routeArgs, Map<String, String> queryArgs, Map<String, String> bodyArgs) {
                        return router.removeStudent(
                                routeArgs.get("classroom"),
                                parseInt(routeArgs.get("nr")));
                    }
                }
                )
        );
    }
}
