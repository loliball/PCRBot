package pcr

import java.text.SimpleDateFormat
import java.util.*

object Bigfuck {

    private fun getArgsAppendUrl(list: List<String>) = buildString {
        append("https://bigfun.bilibili.com/api/feweb?")
        for (i in list.indices) {
            append(list[i])
            if (i < list.size - 1) {
                append("&")
            }
        }
    }

    fun getTimelineUrl(page: Int): String {
        val list = mutableListOf<String>()
        list.add("target=gzlj-clan-day-timeline-report/a")
        list.add("page=$page")
        list.add("size=30")
        return getArgsAppendUrl(list)
    }

    val dayUrl: String
        get() {
            val format = SimpleDateFormat("yyyy-MM-dd")
            val now = Calendar.getInstance()
            if (now[Calendar.HOUR_OF_DAY] < 5) now.add(Calendar.DATE, -1)
            return getDayUrl(format.format(Date(now.timeInMillis)))
        }

    fun getDayUrl(day: String): String {
        val list = mutableListOf<String>()
        list.add("target=gzlj-clan-day-report/a")
        list.add("date=$day")
        list.add("size=30")
        return getArgsAppendUrl(list)
    }

    val nowBOSSInfoUrl: String
        get() {
            val list = mutableListOf<String>()
            list.add("target=gzlj-clan-day-report-collect/a")
            return getArgsAppendUrl(list)
        }

    val bOSSListUrl: String
        get() {
            val list = mutableListOf<String>()
            list.add("target=gzlj-clan-boss-report-collect/a")
            return getArgsAppendUrl(list)
        }

    fun getSearchByNameUrl(name: String): String {
        val list = mutableListOf<String>()
        list.add("target=gzlj-search-clan/b")
        list.add("name=$name")
        return getArgsAppendUrl(list)
    }

    fun getSearchByRankUrl(rank: String): String {
        val list = mutableListOf<String>()
        list.add("target=gzlj-search-clan/b")
        list.add("rank=$rank")
        return getArgsAppendUrl(list)
    }

    val clanBattleListUrl: String
        get() {
            val list = mutableListOf<String>()
            list.add("target=gzlj-clan-battle-list/a")
            return getArgsAppendUrl(list)
        }

    fun getClanBattleRankingTimeRangeUrl(battle: Int): String {
        val list = mutableListOf<String>()
        list.add("target=gzlj-clan-battle-ranking-time-range/a")
        list.add("battle_id=$battle")
        return getArgsAppendUrl(list)
    }

    fun getMyClanRankingUrl(year: Int, month: Int, day: Int, hour: Int, minute: Int, battle: Int): String {
        val list = mutableListOf<String>()
        list.add("target=gzlj-my-clan-ranking/a")
        list.add("year=$year")
        list.add("month=$month")
        list.add("day=$day")
        list.add("hour=$hour")
        list.add("minute=$minute")
        list.add("battle_id=$battle")
        return getArgsAppendUrl(list)
    }
}