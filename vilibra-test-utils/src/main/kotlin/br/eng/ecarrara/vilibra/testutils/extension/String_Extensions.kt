package br.eng.ecarrara.vilibra.testutils.extension

fun Any.getResource(path: String) = this.javaClass.classLoader
        .getResourceAsStream(path).bufferedReader().use { it.readText() }