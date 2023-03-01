package pt.isel.autorouter

import java.util.stream.Stream

fun Stream<ArHttpRoute>.jsonServer() = JsonServer(this)

fun Any.autorouterReflect() = AutoRouterReflect.autorouterReflect(this)

fun Any.autorouterDynamic() = AutoRouterDynamic.autorouterDynamic(this)