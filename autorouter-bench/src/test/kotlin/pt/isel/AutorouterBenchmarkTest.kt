package pt.isel

import org.junit.Test
import pt.isel.autorouter.ArHttpRoute
import pt.isel.autorouter.AutoRouterDynamic
import pt.isel.autorouter.AutoRouterReflect
import java.util.Map
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class AutorouterBenchmarkTest {
    private val classrooom = ClassroomControllerEmpty()
    private val dynamicRoutes = AutoRouterDynamic.autorouterDynamic(classrooom).toList()
    private val reflectRoutes = AutoRouterReflect.autorouterReflect(classrooom).toList()

    private var addStudentDynamic =
        dynamicRoutes.first { r: ArHttpRoute -> r.funName == "addStudent" }
    private var addStudentReflect =
        reflectRoutes.first { r: ArHttpRoute -> r.funName == "addStudent" }
    private var removeDynamic =
        dynamicRoutes.first { r: ArHttpRoute -> r.funName == "removeStudent" }
    private var removeReflect =
        reflectRoutes.first { r: ArHttpRoute -> r.funName == "removeStudent" }
    private var searchDynamic =
        dynamicRoutes.first { r: ArHttpRoute -> r.funName == "search" }
    private var searchReflect =
        reflectRoutes.first { r: ArHttpRoute -> r.funName == "search" }


    @Test
    fun dynamic_route_to_add_student() {
        val optional = addStudentDynamic.handler.handle(
            mapOf("classroom" to "i42d", "nr" to "7646775"),
            emptyMap<String, String>(),
            mapOf("name" to "Ze Gato", "group" to "11", "semester" to "3")
        )
        assertEquals(
            Student(7646775, "Ze Gato", 11, 3),
            optional.get()
        )
    }
    @Test
    fun reflect_route_to_add_student() {
        val optional = addStudentReflect.handler.handle(
            mapOf("classroom" to "i42d", "nr" to "7646775"),
            emptyMap<String, String>(),
            mapOf("name" to "Ze Gato", "group" to "11", "semester" to "3")
        )
        assertEquals(
            Student(7646775, "Ze Gato", 11, 3),
            optional.get()
        )
    }
    @Test
    fun dynamic_route_to_remove_student_on_empty() {
        val optional = removeDynamic.handler.handle(
            Map.of("classroom", "i41d", "nr", "7236"),
            emptyMap<String, String>(),
            emptyMap<String, String>()
        )
        assertEquals(
            Student(7236, "Jonas Mancas Lubri", 56, 4),
            optional.get()
        )
    }

    @Test
    fun reflect_route_to_remove_student_on_empty() {
        val optional = removeReflect.handler.handle(
            Map.of("classroom", "i41d", "nr", "7236"),
            emptyMap<String, String>(),
            emptyMap<String, String>()
        )
        assertEquals(
            Student(7236, "Jonas Mancas Lubri", 56, 4),
            optional.get()
        )
    }
    @Test
    fun dynamic_route_to_search_on_empty() {
        val optional = searchDynamic.handler.handle(
            Map.of("classroom", "i41d"),
            emptyMap<String, String>(),
            emptyMap<String, String>()
        )
        assertEquals(
            listOf(Student(7236, "Jonas Mancas Lubri", 56, 4)),
            optional.get()
        )
    }

    @Test
    fun reflect_route_to_search_on_empty() {
        val optional = searchReflect.handler.handle(
            Map.of("classroom", "i41d"),
            emptyMap<String, String>(),
            emptyMap<String, String>()
        )
        assertContentEquals(
            listOf(Student(7236, "Jonas Mancas Lubri", 56, 4)),
            optional.get() as List<Student>
        )
    }
}
