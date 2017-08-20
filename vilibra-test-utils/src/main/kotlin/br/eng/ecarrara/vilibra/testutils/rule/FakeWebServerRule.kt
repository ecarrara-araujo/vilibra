package br.eng.ecarrara.vilibra.testutils.rule

import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockWebServer
import org.junit.rules.ExternalResource

class FakeWebServerRule(
        val baseUrl: String = "/"
) : ExternalResource() {

    val server by lazy { MockWebServer() }
    lateinit var url: HttpUrl
        private set

    override fun before() {
        server.start()
        url = server.url(baseUrl)
    }

    override fun after() {
        server.shutdown()
    }

}