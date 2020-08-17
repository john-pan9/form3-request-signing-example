package uk.co.mettle.ssv.form3.messagesigning

import uk.co.mettle.ssv.form3.messagesigning.PrivateKeyUtil.form3ExamplePrivateKeyEncoded
import java.security.MessageDigest
import java.security.PrivateKey
import java.security.Signature
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Base64
import java.util.Date

// default values taken from tutorial.
// override the values as you wish!
// See: https://api-docs.form3.tech/tutorial-request-signing-debug.html
data class Form3Request(
    val host: String = "api.staging-form3.tech",
    val path: String = "/v1/http-signatures/debug",
    val method: String = "POST",
    val contentTypeHeaderValue: String = "application/vnd.api+json",
    val acceptHeaderValue: String = "application/vnd.api+json",
    val date: Instant = Instant.now(),
    val body: String = """{"data":{"type":"payments","id":"1234567890","version":0,"organisation_id":"1234567890","attributes":{"amount":"200.00","beneficiary_party":{"account_name":"Mrs Receiving Test","account_number":"71268996","account_number_code":"BBAN","account_with":{"bank_id":"400302","bank_id_code":"GBDSC"}},"currency":"GBP","debtor_party":{"account_name":"Mr Sending Test","account_number":"87654321","account_number_code":"BBAN","account_with":{"bank_id":"1234567890","bank_id_code":"GBDSC"}},"processing_date":"2019-20-5","reference":"Something","payment_scheme":"FPS","scheme_payment_sub_type":"TelephoneBanking","scheme_payment_type":"ImmediatePayment"}}}""",
    val keyId: String = "75a8ba12-fff2-4a52-ad8a-e8b34c5ccec8",
    val privateKey: PrivateKey = PrivateKeyUtil.privateKey(form3ExamplePrivateKeyEncoded)
) {
    fun digest(): String = Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-256").digest(body.toByteArray()))

    fun signature(): String =
        """(request-target): ${method.toLowerCase()} $path
          |host: $host
          |date: ${date.form3Format()}
          |accept: $acceptHeaderValue
          |content-type: $contentTypeHeaderValue
          |content-length: ${contentLength()}
          |digest: SHA-256=${digest()}""".trimMargin()

    fun base64Signature(): String = Base64.getEncoder().encodeToString(signature().toByteArray())

    fun contentLength(): Int = body.toByteArray().size

    fun sign(): String =
        Base64.getEncoder().encodeToString(Signature.getInstance("SHA256withRSA").apply {
            initSign(privateKey)
            update(signature().toByteArray())
        }.sign())

    fun authorizationHeaderValue(): String =
        """Authorization: Signature keyId="$keyId",algorithm="rsa-sha256",headers="(request-target) host date accept content-type content-length digest",signature="${sign()}""""
}

fun Instant.form3Format(): String = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").format(Date.from(this))
