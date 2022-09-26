package pcr.bean

import kotlinx.serialization.Serializable

@Serializable
data class BossInfo(
    val name: String,
    val boss_list: List<Boss>
) {
    fun name2id(name: String) = boss_list.indexOfFirst { it.boss_name == name } + 1
    fun id2name(id: Int) = boss_list[id - 1].boss_name
}

@Serializable
data class Boss(
    val id: String,
    val boss_name: String
)