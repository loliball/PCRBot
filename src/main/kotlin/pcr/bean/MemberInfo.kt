package pcr.bean

import kotlinx.serialization.Serializable

typealias MemberInfoList = List<MemberInfo>

fun MemberInfoList.format(): String {
    val damageCount = sumOf { it.number.toDouble() }
    val finishCount = filter { it.number == 3.0f }.size
    val data = filter { it.number != 3.0f }
        .sortedBy { it.number }
        .joinToString("\n") { "剩${3.0f - it.number}刀，${it.name}" }
    return """
今日余刀：
$data
==========
总出刀：$damageCount/${size * 3}(剩${size * 3 - damageCount}刀)
总人数：$finishCount/$size(剩${size - finishCount}人)
""".trimIndent()
}

fun MemberInfoList.ramin(info: BossInfo): String {
    val data = filter { it.number != it.number.toInt().toFloat() }
        .joinToString("\n") {
            val last = it.damage_list.last()
            "${info.name2id(last.boss_name)}王${last.damage / 10000}万，${it.name}"
        }
    return """
剩余刀：
$data
==========
总剩余刀数：${filter { it.number != it.number.toInt().toFloat() }.size}
""".trimIndent()
}

fun MemberInfoList.report(search: String, info: BossInfo) = buildString {
    appendLine("查询到以下信息")
    this@report.filter { it.name.contains(search, true) }.forEach {
        appendLine("==========")
        appendLine("${it.name}(${it.number}刀)")
        it.damage_list.forEach {
            appendLine("对${info.name2id(it.boss_name)}王造成${it.damage / 10000}万伤害(${it.killType.count}刀)")
        }
        if (it.damage_list.isEmpty()) appendLine("尚未出刀")
    }
}.trimEnd()


@Serializable
data class MemberInfo(
    val name: String,
    val number: Float,
    val damage: Int,
    val score: Int,
    val damage_list: List<Damage>
)

@Serializable
data class Damage(
    val datetime: Int,
    val boss_name: String,
    val lap_num: Int,
    val damage: Int,
    val kill: Int,
    val reimburse: Int
) {
    val killType: KillType
        get() = if (kill == 1 && reimburse == 0) KillType.KILL
        else if (kill == 0 && reimburse == 1) KillType.REMAIN
        else if (kill == 0 && reimburse == 0) KillType.NORMAL
        else KillType.OTHER
}

enum class KillType(
    val kill: String,
    val count: Float
) {
    KILL("收尾刀", 0.5f),
    REMAIN("剩余刀", 0.5f),
    NORMAL("完整刀", 1f),
    OTHER("断尾刀", 0.5f);
}
