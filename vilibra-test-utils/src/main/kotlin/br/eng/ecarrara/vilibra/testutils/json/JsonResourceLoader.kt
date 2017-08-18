package br.eng.ecarrara.vilibra.testutils.json

fun jsonFromResource(resource: String, javaClazz: Class<Any>) : String =
        javaClazz.classLoader.getResourceAsStream(resource).bufferedReader().use { it.readText() }

