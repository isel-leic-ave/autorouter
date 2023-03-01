package pt.isel

import org.junit.jupiter.api.Test
import org.objectweb.asm.ClassReader
import org.objectweb.asm.util.TraceClassVisitor
import pt.isel.autorouter.AutoRouterDynamic
import java.io.PrintWriter

fun printBytecodes(bytes: ByteArray) {
    val reader = ClassReader(bytes)
    val tcv = TraceClassVisitor(PrintWriter(System.out))
    reader.accept(tcv, 0)
}

class AutoRouterDynamicPrint {

    @Test
    fun print_dynamic_handler_to_search() {
        val classMaker = AutoRouterDynamic.buildHandler(
            ClassroomController::class.java,
            ClassroomController::class.java.declaredMethods.first { it.name == "search" }
        )
        printBytecodes(classMaker.finishBytes())
    }

    @Test
    fun print_dynamic_handler_to_addStudent() {
        val classMaker = AutoRouterDynamic.buildHandler(
            ClassroomController::class.java,
            ClassroomController::class.java.declaredMethods.first { it.name == "addStudent" }
        )
        printBytecodes(classMaker.finishBytes())
    }
}