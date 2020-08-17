package uk.co.mettle.ssv.form3.messagesigning

object CurlFormat {

    /**
     * Formatted as a curl request.
     */
    fun format(request: Form3Request): String {
        return """curl -v -X ${request.method} https://${request.host}${request.path} \
            |-H 'Date: ${request.date.form3Format()}' \
            |-H 'Accept: ${request.acceptHeaderValue}' \
            |-H 'Content-Type: ${request.contentTypeHeaderValue}' \
            |-H 'Content-Length: ${request.contentLength()}' \
            |-H 'Digest: ${request.digest()}' \
            |-H '${request.authorizationHeaderValue()}' \
            |-H 'signature-debug: ${request.base64Signature()}' \
            |-d '${request.body}'
        """.trimMargin()
    }
}

fun main() {
    println(CurlFormat.format(Form3Request()))
}