package id.walt.mdoc.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.cbor.ByteString
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.encodeToByteArray

@Serializable
data class IssuerSigned(
  @ByteString val nameSpaces: Map<String, List<IssuerSignedItemBytes>>?
  // val issuerAuth: IssuerAuth
) {
  companion object {
    class IssuerSignedBuilder() {
      val nameSpacesMap = mutableMapOf<String, MutableList<IssuerSignedItemBytes>>()
      @OptIn(ExperimentalSerializationApi::class)
      fun addIssuerSignedItem(nameSpace: String, item: IssuerSignedItemBytes): IssuerSignedBuilder {
        nameSpacesMap.getOrPut(nameSpace) { mutableListOf() }.add(item)
        return this
      }

      // add/generate IssuerAuth MSO object

      fun build(): IssuerSigned {
        return IssuerSigned(nameSpacesMap.toMap())
      }
    }
  }
}
