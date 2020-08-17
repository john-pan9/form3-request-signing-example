package uk.co.mettle.ssv.form3.messagesigning

object IntellijHttpClientFormat {

    /**
     * Formatted as an Intellij Idea Http Client request.
     *
     * Warning - Doesn't work, and I haven't figured out why!
     */
    fun format(request: Form3Request): String {
        return """${request.method} https://${request.host}${request.path}
            |Date: ${request.date.form3Format()}
            |Accept: ${request.acceptHeaderValue}
            |Content-Type: ${request.contentTypeHeaderValue}
            |Content-Length: ${request.contentLength()}
            |Digest: ${request.digest()}
            |${request.authorizationHeaderValue()}
            |signature-debug: ${request.base64Signature()}
            |
            |${request.body}
        """.trimMargin()
    }
}

fun main() {
    println(IntellijHttpClientFormat.format(Form3Request()))
}