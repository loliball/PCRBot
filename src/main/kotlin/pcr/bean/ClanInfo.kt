package pcr.bean

import kotlinx.serialization.Serializable
import utils.LOCAL_DATE_TIME
import java.time.LocalDateTime

typealias SearchClanInfoList = List<SearchClanInfo>

fun SearchClanInfoList.format(timeRange: TimeRange? = null): String {
    if (isEmpty()) return "没有结果"
    val prefix = if (timeRange == null) "" else "$timeRange\n"
    return joinToString("\n", prefix, "\n共计${size}个结果") {
        """
            公会：${it.clan_name}
            排名：${it.rank}
            会长：${it.leader_name}
            分数：${it.damage}
            ==========
        """.trimIndent()
    }
}

@Serializable
data class SearchClanInfo(
    val clan_name: String,
    val leader_name: String,
    val damage: Long,
    val rank: Int,
    val delta_damage: Long
)

@Serializable
data class ClanBattle(
    val id: Int,
    val name: String
)

@Serializable
data class TimeRange(
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int,
    val battle_id: Int = 0
) {
    override fun toString(): String {
        val time = LocalDateTime.of(year, month, day, hour, minute)
        return "${time.format(LOCAL_DATE_TIME)}(${battle_id}期)"
    }
}