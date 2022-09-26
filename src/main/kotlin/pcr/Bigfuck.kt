package pcr

import utils.Config
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*

object Bigfuck {
    //bigfun(cn.bigfun) var3.7.1(10048)
    private var access_token = Config["token"]
    fun setToken(token: String) {
        access_token = token
    }

    //static value, you can get it use HttpCanary or read it in
    // /data/data/cn.bigfun/shared_prefs/BF_DATE.xml
    private const val device_number = "07ad5bda108ebef67fa65c21d55accfd20210406101847b7c7b85dddab1b763b"
    const val BF_Client_Data =
        "Wm1wa1pEUjZjR1JsZGpJNFlYWnBaMjVLYUVwVlowdDJaa3BEYnpVMWVHbGFla3d6ZVdjeVltVjVUa05UYmpVeFFVWm5WR05GUTBObll6bFdUREJPVlhaRFkySnRaMlJJVFhoeGVtTlZOM1JCV1RCQ2FqWkViM2d6Y1haakswcGpkbFpWVTI4dmNrVTJNWFYxU2xkMU9FWlpWbGN3VFVKalR6WmhSblkxZG5KTk4xRlVNM0EyTTNsRVpqWm1LMFowTWtSMFZVbFVSazlrTm1kNVVXSkZiMXB5T0c1Q01sSmxRbXhVVVZGNlZsZHJhMDh5VjNsSmF5OVJWRFUwZGpKVFdIWTRkRlp1V25oelRETkVORWhRT0ZKM1JFRkJNR3hQTld4NlVFWmFRVzFwY0RCTFl6TlljRGxzVFdKU2FsaGhTVEEzWmpNMGRIZHpla2s5"

    //cn.bigfun.utils.r/d()Long
    private fun rid(): Long {
        return ((Math.random() * 900000.0).toInt() + 100000).toLong()
    }

    //cn.bigfun.utils.BigfunMake/b(String)String
    private fun sign(str: String) = buildString {
        //libbigfunmake.so native makeCode()String
        //this method always return WKO-2k_03jisxgH6
        for (b in MessageDigest.getInstance("MD5").digest((str + "WKO-2k_03jisxgH6").toByteArray())) {
            var hexString = Integer.toHexString(b.toInt() and 255)
            if (hexString.length == 1) {
                hexString = "0$hexString"
            }
            append(hexString)
        }
    }

    //cn.bigfun.utils.r/a(List<String>, long, long)String
    private fun sign(list: MutableList<String>, ts: Long, rid: Long): String {
        list.add("access_token=$access_token")
        list.add("ts=$ts")
        list.add("rid=$rid")
        list.sort()
        return sign(buildString {
            for (i in list.indices) {
                append(list[i])
                if (i < list.size - 1) {
                    append("&")
                }
            }
        })
    }

    private fun addGlobalArgs(list: MutableList<String>) {
        val time = System.currentTimeMillis()
        val ts = time / 1000
        val rid = ts + rid()
        list.add("device_number=$device_number")
        list.add("sign=" + sign(list, ts, rid))
    }

    private fun getArgsAppendUrl(list: List<String>) = buildString {
        append("https://api.bigfun.cn/webview/android?")
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
        addGlobalArgs(list)
        return getArgsAppendUrl(list)
    }

    val dayUrl: String
        get() {
            val format = SimpleDateFormat("yyyy-MM-dd")
            val now = Calendar.getInstance()
            if (now[Calendar.HOUR_OF_DAY] < 5) now.add(Calendar.DATE, -1)
            return getDayUrl(format.format(Date(now.timeInMillis)))
        }

    private fun getDayUrl(day: String): String {
        val list = mutableListOf<String>()
        list.add("target=gzlj-clan-day-report/a")
        list.add("date=$day")
        list.add("size=30")
        addGlobalArgs(list)
        return getArgsAppendUrl(list)
    }

    val nowBOSSInfoUrl: String
        get() {
            val list = mutableListOf<String>()
            list.add("target=gzlj-clan-day-report-collect/a")
            addGlobalArgs(list)
            return getArgsAppendUrl(list)
        }

    val bOSSListUrl: String
        get() {
            val list = mutableListOf<String>()
            list.add("target=gzlj-clan-boss-report-collect/a")
            addGlobalArgs(list)
            return getArgsAppendUrl(list)
        }

    fun getSearchByNameUrl(name: String): String {
        val list = mutableListOf<String>()
        list.add("target=gzlj-search-clan/b")
        list.add("name=$name")
        addGlobalArgs(list)
        return getArgsAppendUrl(list)
    }

    fun getSearchByRankUrl(rank: String): String {
        val list = mutableListOf<String>()
        list.add("target=gzlj-search-clan/b")
        list.add("rank=$rank")
        addGlobalArgs(list)
        return getArgsAppendUrl(list)
    }

    val clanBattleListUrl: String
        get() {
            val list = mutableListOf<String>()
            list.add("target=gzlj-clan-battle-list/a")
            addGlobalArgs(list)
            return getArgsAppendUrl(list)
        }

    fun getClanBattleRankingTimeRangeUrl(battle: Int): String {
        val list = mutableListOf<String>()
        list.add("target=gzlj-clan-battle-ranking-time-range/a")
        list.add("battle_id=$battle")
        addGlobalArgs(list)
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
        addGlobalArgs(list)
        return getArgsAppendUrl(list)
    }
}