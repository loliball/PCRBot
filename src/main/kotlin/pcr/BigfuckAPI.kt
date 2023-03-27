package pcr

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import pcr.bean.*
import utils.Config
import utils.log

object BigfuckAPI {

    var failedCallback: ((reason: String) -> Unit)? = null

    private val json = Json {
        ignoreUnknownKeys = true
    }

    fun searchByName(name: String) = request<SearchClanInfoList>(Bigfuck.getSearchByNameUrl(name))
    fun searchByRank(rank: String) = request<SearchClanInfoList>(Bigfuck.getSearchByRankUrl(rank))

    val clanBattleList get() = request<List<ClanBattle>>(Bigfuck.clanBattleListUrl)
    fun getTimeRange(battle: Int) = request<List<TimeRange>>(Bigfuck.getClanBattleRankingTimeRangeUrl(battle))
    fun getMyClanRanking(t: TimeRange) = getMyClanRanking(t.year, t.month, t.day, t.hour, t.minute, t.battle_id)
    private fun getMyClanRanking(year: Int, month: Int, day: Int, hour: Int, minute: Int, battle: Int) =
        kotlin.runCatching {
            request<SearchClanInfo>(Bigfuck.getMyClanRankingUrl(year, month, day, hour, minute, battle))
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()

    val bossList get() = request<BossInfo>(Bigfuck.bOSSListUrl)
    val nowBossInfo get() = request<NowBoss>(Bigfuck.nowBOSSInfoUrl)
    val teamWarAllUsers get() = request<MemberInfoList>(Bigfuck.dayUrl)

    fun getTeamWarInfo(page: Int): String? {
        val json = networkRequest(Bigfuck.getTimelineUrl(page))
        println("getTeamWarInfo($page)$json")
        return json
    }

    private val client = OkHttpClient()
    private inline fun <reified R> request(url: String): R? {
        val request = Request.Builder()
            .url(url)
            .headers(headers)
            .build()
        var bodyString: String? = null
        return runCatching {
            bodyString = client.newCall(request).execute().body?.string()
            bodyString?.let { json1 ->
                val returnData = json.decodeFromString<ReturnData<R>>(json1)
                if (returnData.invalid()) {
                    println("url: $url raw: $json1")
                    null
                } else {
                    returnData.data
                }
            }
        }.onFailure { err ->
            err.printStackTrace()
            if (bodyString != null) {
                failedCallback?.invoke(bodyString!!)
            } else {
                err.message?.let { failedCallback?.invoke(it) }
            }
        }.getOrNull()
    }

    private fun networkRequest(url: String): String? {
        val request = Request.Builder()
            .url(url)
            .headers(headers)
            .build()
        return kotlin.runCatching {
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) return null
            if ("OK" != response.message) return null
            response.body?.string()
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
    }

    var cookie = Config["cookie"]

    private val headers: Headers
        get() {
            return Headers.Builder()
                .add("Content-Type", "application/vnd.api+json")
                .add("Accept", "application/vnd.api+json")
                .add("Host", "bigfun.bilibili.com")
                .add("Connection", "Keep-Alive")
                .add("User-Agent", "okhttp/3.12.12")
                .add("Cookie", cookie)
                .build()
        }

}
