package pt.isel

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import pt.isel.autorouter.*
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import java.util.stream.Stream
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JsonServerTestForClassroom {

    private var server: JsonServer? = null
    private val mapper = ObjectMapper()


    fun makeJsonServer(): Stream<JsonServer> = Stream.of(
        ClassroomController().autorouterReflect().jsonServer(),
        ClassroomController().autorouterDynamic().jsonServer(),
    )

    fun setup(jsonServer: JsonServer) = runBlocking {
        suspendCoroutine { cont ->
            server = jsonServer.apply {
                javalin().events {
                    it.serverStarted { cont.resume(Unit) }
                    it.serverStartFailed { cont.resume(Unit) }
                }
                start(4000)
            }
        }
    }

    fun teardown() = runBlocking {
        suspendCoroutine { cont ->
            server?.javalin()?.events {
                it.serverStopped { cont.resume(Unit) }
                it.serverStopFailed { cont.resume(Unit) }
            }
            server?.close()
            server = null
        }
    }

    fun runJsonServer(jsonServer: JsonServer, block: () -> Unit) {
        try {
            setup(jsonServer)
            block()
        } finally {
            teardown()
        }
    }

    @ParameterizedTest
    @MethodSource("makeJsonServer")
    fun get_all_students(jsonServer: JsonServer) = runJsonServer(jsonServer) {
        val json = URL("http://localhost:4000/classroom/i42d")
            .openStream()
            .bufferedReader()
            .readText()
        val actual = mapper.readValue(json, object : TypeReference<List<Student>>() {})
        assertContentEquals(
            listOf(
                Student(9876, "Ole Super", 7, 5),
                Student(4536, "Isel Maior", 7, 5),
                Student(5689, "Ever Sad", 7, 3),
            ),
            actual)
    }

    @ParameterizedTest
    @MethodSource("makeJsonServer")
    fun get_students_with_name_containing_word(jsonServer: JsonServer) = runJsonServer(jsonServer) {
        val json = URL("http://localhost:4000/classroom/i42d?student=maior")
            .openStream()
            .bufferedReader()
            .readText()
        val actual = mapper.readValue(json, object : TypeReference<List<Student>>() {})
        assertContentEquals(
            listOf(Student(4536, "Isel Maior", 7, 5)),
            actual)
    }

    @ParameterizedTest
    @MethodSource("makeJsonServer")
    fun insert_student_in_classroom(jsonServer: JsonServer) = runJsonServer(jsonServer) {
        val json = URL("http://localhost:4000/classroom/i42d/students/7777")
            .put("""{"nr": "7777", "name":"Ze Gato","group":"11", "semester":"3"}""")
        val actual = mapper.readValue(json, Student::class.java)
        assertEquals(
            Student(7777, "Ze Gato", 11, 3),
            actual)
    }

    @ParameterizedTest
    @MethodSource("makeJsonServer")
    fun remove_student_from_classroom(jsonServer: JsonServer) = runJsonServer(jsonServer) {
        val json = URL("http://localhost:4000/classroom/i42d/students/4536")
            .delete()
        val actual = mapper.readValue(json, Student::class.java)
        assertEquals(
            Student(4536, "Isel Maior", 7, 5),
            actual)
    }

    fun URL.put(json: String): String {
        val client = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder()
            .uri(URI.create(this.toString()))
            .header("Content-Type", "application/json; charset=UTF-8")
            .PUT(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        return response.body()
    }

    fun URL.delete(): String = (this.openConnection() as HttpURLConnection).run {
        requestMethod = "DELETE"
        doOutput = true
        connect()
        this.inputStream.bufferedReader().readText()
    }
}