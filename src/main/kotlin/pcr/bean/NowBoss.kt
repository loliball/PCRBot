package pcr.bean

import kotlinx.serialization.Serializable
import utils.xxxx

@Serializable
data class NowBoss(
    val clan_info: ClanInfo,
    val boss_info: NowBossInfo,
    val battle_info: BattleInfo,
    val day_list: List<String>
) {
    fun format(info: BossInfo) = """
            ${clan_info.name}
            当前排名${clan_info.last_ranking}(${clan_info.last_total_ranking})
            ==========
            ${battle_info.name}boss信息
            现在${boss_info.lap_num}周目，${boss_info.name}(${info.name2id(boss_info.name)})
            ${boss_info.current_life.xxxx}/${boss_info.total_life.xxxx}
            ==========
            会战将于${day_list[0]}结束
        """.trimIndent()
}

@Serializable
data class ClanInfo(
    val name: String,
    val last_ranking: Int,
    val last_total_ranking: String
)

@Serializable
data class NowBossInfo(
    val name: String,
    val total_life: Long,
    val current_life: Long,
    val lap_num: Int
)

@Serializable
data class BattleInfo(
    val id: String,
    val name: String
)
