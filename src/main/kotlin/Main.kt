import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.auth.BotAuthorization
import net.mamoe.mirai.event.events.GroupEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.utils.BotConfiguration
import pcr.BigfuckAPI
import pcr.BossRemHealthCalc
import pcr.bean.TimeRange
import pcr.bean.format
import pcr.bean.ramin
import pcr.bean.report
import utils.Config
import utils.log
import java.time.LocalDateTime
import kotlin.coroutines.EmptyCoroutineContext

suspend fun main(args: Array<String>) {
//    test()
    init()
}

private suspend fun init() {

    val qq = Config["qq.id"].toLong()
    val owner = Config["qq.owner"].toLong()
    val group = Config["qq.group"].toLong()
    val protocolConfig = Config["qq.protocol"]

    val authorization = BotAuthorization.byPassword(Config["qq.pwd"])
//    val authorization = BotAuthorization.byQRCode()
    val bot = BotFactory.newBot(qq, authorization) {
        fileBasedDeviceInfo()
        redirectBotLogToDirectory()
        redirectNetworkLogToDirectory()
        enableContactCache()
        protocol = BotConfiguration.MiraiProtocol.valueOf(protocolConfig)
    }

    bot.login()

    bot.eventChannel.filter { it is GroupEvent && it.group.id == group }.subscribeMessages(
        EmptyCoroutineContext + CoroutineExceptionHandler { _, exception ->
            exception.printStackTrace()
        }) {
        "状态" reply {
            val healthCalc = BossRemHealthCalc(BigfuckAPI.bossList!!)
            BigfuckAPI.nowBossInfo!!.day_list.forEach {
                healthCalc.update(BigfuckAPI.getTeamWarAllUsersAt(it)!!)
            }
            BigfuckAPI.nowBossInfo!!.format(healthCalc.result())
        }
        "状态2" reply { BigfuckAPI.nowBossInfo!!.format(BigfuckAPI.bossList!!) }
        "查刀" reply { BigfuckAPI.teamWarAllUsers!!.format() }
        "排名" reply {
            val id = BigfuckAPI.nowBossInfo!!.battle_info.id.toInt()
            val timeRange = yesterday5am().copy(battle_id = id)
            listOf(BigfuckAPI.getMyClanRanking(timeRange)!!).format(timeRange)
        }
        matching("(尾刀)|(剩余刀)".toRegex()) reply {
            BigfuckAPI.teamWarAllUsers!!.ramin(BigfuckAPI.bossList!!)
        }
        matching("报告 (.+)".toRegex()) {
            reply(BigfuckAPI.teamWarAllUsers!!.report(it.groupValues[1], BigfuckAPI.bossList!!))
        }
        matching("查分 (.+)".toRegex()) {
            reply(BigfuckAPI.searchByRank(it.groupValues[1])!!.format())
        }
        matching("查线 (.+)".toRegex()) {
            reply(BigfuckAPI.searchByName(it.groupValues[1])!!.format())
        }
        matching("/cookie (.+)".toRegex()) {
            val token = it.groupValues[1]
            subject.sendMessage("设置了新的cookie:\n$token")
            BigfuckAPI.cookie = token
        }
        case("/config", true) reply {
            Config.load()
            "重载配置成功"
        }
    }

    BigfuckAPI.failedCallback = {
        CoroutineScope(Dispatchers.Default).launch {
            bot.getGroup(group)?.sendMessage(it)
        }
    }

    bot.join()
}

suspend fun MessageEvent.reply(str: String) = subject.sendMessage(str)

fun yesterday5am(): TimeRange {
    val now = LocalDateTime.now()
    return if (now.hour >= 5) {
        TimeRange(now.year, now.monthValue, now.dayOfMonth, 5, 0)
    } else {
        val yesterday = now.plusDays(-1)
        TimeRange(yesterday.year, yesterday.monthValue, yesterday.dayOfMonth, 5, 0)
    }
}


private suspend fun test() {
    CoroutineScope(Dispatchers.Default).launch {
//        val bossInfo = BigfuckAPI.bossList!!
//        BigfuckAPI.nowBossInfo!!.format(bossInfo).log()
//        BigfuckAPI.teamWarAllUsers!!.format().log()
//        BigfuckAPI.teamWarAllUsers!!.ramin(bossInfo).log()
//        BigfuckAPI.teamWarAllUsers!!.report("n", bossInfo).log()
//        BigfuckAPI.searchByName("水龙敬初恋花园")?.format().log()
//        BigfuckAPI.searchByRank("10000")?.format().log()
//        BigfuckAPI.searchByRank("1000000")?.format().log()
//        BigfuckAPI.clanBattleList.log()
//        BigfuckAPI.getTimeRange(29).log()
//        BigfuckAPI.getMyClanRanking(
//            2022, 9, 25,
//            15, 0, 29
//        ).log()
//        val id = BigfuckAPI.nowBossInfo!!.battle_info.id.toInt()
//        val timeRange = TimeRange(
//            2022, 9, 25,
//            15, 0, 29
//        )
//        val timeRange =  yesterday5am().copy(battle_id = id)
//        listOf(BigfuckAPI.getMyClanRanking(timeRange)!!).format(timeRange).log()

        val healthCalc = BossRemHealthCalc(BigfuckAPI.bossList!!)
        BigfuckAPI.nowBossInfo!!.day_list.forEach {
            healthCalc.update(BigfuckAPI.getTeamWarAllUsersAt(it)!!)
        }
        BigfuckAPI.nowBossInfo!!.format(healthCalc.result()).log()

    }.join()
}
