package pcr

import pcr.bean.BossInfo
import pcr.bean.MemberInfoList


class BossRemHealthCalc(private val info: BossInfo) {

    private val bossDamageSum = LongArray(5)

    private val bossMaxHealth = listOf(
        BossHealth(0, intArrayOf(600_0000, 600_0000, 700_0000, 1700_0000, 8500_0000)),
        BossHealth(1, intArrayOf(800_0000, 800_0000, 900_0000, 1800_0000, 9000_0000)),
        BossHealth(2, intArrayOf(1000_0000, 1000_0000, 1300_0000, 2000_0000, 9500_0000)),
        BossHealth(3, intArrayOf(1200_0000, 1200_0000, 1500_0000, 2100_0000, 10000_0000)),
        BossHealth(4, intArrayOf(1500_0000, 1500_0000, 2000_0000, 2300_0000, 11000_0000))
    )

    private fun getHealth(bossIndex: Int, lap: Int): Int {
        val index = when (lap) {
            in 1..3 -> 0
            in 4..10 -> 1
            in 11..34 -> 2
            in 35..44 -> 3
            else -> 4
        }
        return bossMaxHealth[bossIndex].health[index]
    }

    fun update(list: MemberInfoList) {
        list.forEach {
            it.damage_list.forEach {
                bossDamageSum[info.name2id(it.boss_name) - 1] += it.damage.toLong()
            }
        }
    }

    fun result(): List<BossResult> {
        val bossResult = mutableListOf<BossResult>()
        var lap = 1
        while (true) {
            bossDamageSum.forEachIndexed { index, dmg ->
                val health = getHealth(index, lap)
                if (dmg >= health) {
                    bossDamageSum[index] -= health.toLong()
                } else {
                    if (bossResult.find { it.bossIndex == index } != null) return@forEachIndexed
                    bossResult += BossResult(index, info.id2name(index + 1), lap, health - dmg.toInt(), health)
                }
            }
            if (bossResult.size == 5) break
            lap++
        }
        return bossResult.sortedBy { it.bossIndex }
    }

    data class BossResult(
        val bossIndex: Int,
        val name: String,
        val lap: Int,
        val rem: Int,
        val max: Int
    )

    data class BossHealth(
        val bossIndex: Int,
        val health: IntArray
    )

}