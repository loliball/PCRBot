package pcr.bean

import kotlinx.serialization.Serializable

@Serializable
data class ReturnData<T>(
    val code: Int,
    val data: T
) {
    fun invalid() = code != 0
}
