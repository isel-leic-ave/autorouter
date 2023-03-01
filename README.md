# autorouter

Automatic HTTP handlers for a controller object with specific annotations.

## Assignments

1. Published 1-3-2023 DEADLINE: 03-4-2023
1. Published 1-3-2023 DEADLINE: 08-5-2023
1. Published 05-5-2023 DEADLINE: 29-5-2023

## Assignment 1 - Types at runtime and Reflection API

The AutoRouter library allows the automatic creation of HTTP handlers for a 
[`pt.isel.autorouter.JsonServer`](autorouter/src/main/java/pt/isel/autorouter/JsonServer.java)
based on a router object with specific annotations, according to the next [example of `ClassroomController`](#classroomController-example).

All methods annotated with `@AutoRoute` and returning an `Optional` are eligible
for HTTP handlers.
To avoid ambiguity with existing types of JDK we choose to prefix annotations with `Ar`, i.e. `ArRoute`,
`ArQuery`, `ArBody`.

For simplicity, [`JsonServer`](autorouter/src/main/java/pt/isel/autorouter/JsonServer.java) 
is only responding with status codes of 200, 404, and 500, depending 
on whether the handler returns a fulfilled `Optional`, an empty `Optional` or an exception.
(you may consider the use of an alternative `Either`, or other type to enhance responses)

1. Implement the Java function `Stream<ArHttpRoute> autorouterReflect(Object controller)`, which builds
a stream of [`ArHttpRoute`](autorouter/src/main/java/pt/isel/autorouter/ArHttpRoute.java)
objects for each eligible method in given `controller` object parameter.

2. Implement another example of a controller object for a different domain, such as playlist,
movies, football teams, basket, moto gp, or any other of your choice. 
Implement the corresponding tests to validate that all routes generated with `autorouterReflect`
for your controller class are correctly invoked for each HTTP request.

The next figure shows the resulting stream of
[`ArHttpRoute`](autorouter/src/main/java/pt/isel/autorouter/ArHttpRoute.java) objects
for the example of a [`ClassroomRouter` instance](#classroomrouter-example).
The `autorouterReflect` can be use in Kotlin through a statement such as:

```kotlin
ClassroomRouter().autorouterReflect().jsonServer().start(4000)
```

<img src="handlers-for-classroom-router.png">

#### ClassroomController example

```kotlin
class ClassroomController {
    /**
     * Example: http://localhost:4000/classroom/i42d?student=jo
     */
    @Synchronized
    @AutoRoute("/classroom/{classroom}")
    fun search(@ArRoute classroom: String, @ArQuery student: String?): Optional<List<Student>> {
        ...
    }
    /**
     * Example:
     *   curl --header "Content-Type: application/json" \
     *     --request PUT \
     *     --data '{"nr": "7777", "name":"Ze Gato","group":"11", "semester":"3"}' \
     *     http://localhost:4000/classroom/i42d/students/7777
     */
    @Synchronized
    @AutoRoute("/classroom/{classroom}/students/{nr}", method = PUT)
    fun addStudent(
        @ArRoute classroom: String,
        @ArRoute nr: Int,
        @ArBody s: Student
    ): Optional<Student> {
        ...
    }
    /**
     * Example:
     *   curl --request DELETE http://localhost:4000/classroom/i42d/students/4536
     */
    @Synchronized
    @AutoRoute("/classroom/{classroom}/students/{nr}", method = DELETE)
    fun removeStudent(@ArRoute classroom: String, @ArRoute nr: Int) : Optional<Student> {
        ...
    }
}
```

## Assignment 2 - Dynamic Code Generator and Performance Evaluation with JMH

In this workout we follow a different approach to invoke the functions of a controller object.
Instead of using Reflection we will generate different implementations of `ArHttpHandler`
for each function in controller object, as denoted in the next figure.
Notice, these implementations (e.g. `ArHttpHandlerSearch`, `ArHttpHandlerAddStudent`, `ArHttpHandlerRemoveStudent`)
do not use reflection to call the methods of `ClassroomController`.

Implement the Java function `Stream<ArHttpRoute> autorouterDynamic(Object controller)`, which builds
a stream of [`ArHttpRoute`](autorouter/src/main/java/pt/isel/autorouter/ArHttpRoute.java)
objects for each eligible method in given `controller` object parameter.

<img src="dynamic-handlers-for-classroom-router.png">

## Usage

To run these benchmarks on you local machine just run:

```
./gradlew jmhJar
```

And then:

```
java -jar autorouter-bench/build/libs/autorouter-bench-jmh.jar -i 4 -wi 4 -f 1 -r 2 -w 2 
```

* `-i`  4 iterations
* `-wi` 4 warmup iterations
* `-f`  1 fork
* `-r`  2 run each iteration for 2 seconds
* `-w`  2 run each warmup iteration for 2 seconds.
