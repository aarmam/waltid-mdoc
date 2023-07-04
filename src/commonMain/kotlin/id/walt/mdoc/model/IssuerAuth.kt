package id.walt.mdoc.model

import cbor.Cbor
import id.walt.mdoc.model.dataelement.*
import id.walt.mdoc.model.mso.MSO
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.math.cos

@Serializable(with = IssuerAuthSerializer::class)
class IssuerAuth(
    val cose_sign1: ListElement
) {
    fun getMSO(): MSO? {
        if(cose_sign1.value.size < 3) throw SerializationException("Invalid COSE_Sign1 array")
        return when(cose_sign1.value[2].type) {
            DEType.nil -> null
            DEType.byteString -> EncodedCBORElement.fromEncodedCBORElementData(
                (cose_sign1.value[2] as ByteStringElement).value
            ).decode<MSO>()
            else -> throw SerializationException("Invalid COSE_Sign1 payload")
        }
    }
}

@Serializer(forClass = IssuerAuth::class)
object IssuerAuthSerializer {
    override fun serialize(encoder: Encoder, value: IssuerAuth) {
        encoder.encodeSerializableValue(DataElementSerializer, value.cose_sign1)
    }

    override fun deserialize(decoder: Decoder): IssuerAuth {
        return IssuerAuth(
            decoder.decodeSerializableValue(DataElementSerializer) as ListElement
        )
    }
}