package com.example.startmining.network.pools

import android.util.Log
import com.example.startmining.network.cruxpool.CruxpoolService
import com.example.startmining.network.etherscan.EtherscanService


/* Pool Order :
1 - Origin
2 - Genesis
3 - NorthPool
4 - Pulse
5 - Horizon
 */

object PoolsService {
    private val origin_address = "bc1qdzcgvennnjzv4jry38s0krjtl3x9n374302c75"
    var origin_pool_id: Int = 1
        private set

    private val genesis_address = "bc1p94llwnug0zv9zvk8lj9g43s6ul5nssf9yl5pn8sdlyqj3rdy90qq34ck00"
    var genesis_pool_id: Int = 2
        private set
    private val northpool_address = "bc1qyjp7kadrtr8j7gvvs9jej9c790jpmal4cwehle"
    var northpool_pool_id: Int = 3
        private set
    private val pulse_address = "bc1qaj5lgj8zmhap0vquttpud3dhat43y8azplgzqf"
    var pulse_pool_id: Int = 4
        private set
    private val horizon_address = "bc1q0tjj47smwcu7fplekr79p63hdky5x7x4y4e54e"
    var horizon_pool_id: Int = 5
        private set

    /**
     * This function updates the pool information for all pools.
     * It fetches the pool information for each pool address and updates the corresponding PoolInfo object.
     * @param userAddress The user's wallet address (ETH).
     */
    suspend fun updatePoolsInfo(userAddress: String): MutableList<PoolInfo> {
        val poolsInfo = mutableListOf<PoolInfo>()

        try {
            poolsInfo.add(updatePoolInfo(userAddress, origin_address, origin_pool_id))
            poolsInfo.add(updatePoolInfo(userAddress, genesis_address, genesis_pool_id))
            poolsInfo.add(updatePoolInfo(userAddress, northpool_address, northpool_pool_id))
            poolsInfo.add(updatePoolInfo(userAddress, pulse_address, pulse_pool_id))
            poolsInfo.add(updatePoolInfo(userAddress, horizon_address, horizon_pool_id))
        } catch (e: Exception) {
            Log.e("PoolsService", "Error updating pool info: ${e.message}")
        }

        return poolsInfo
    }

    /**
     * This function fetches the pool information for a given user address and pool address.
     * @param userAddress The user's wallet address (ETH).
     * @param poolAddress The address of the pool to fetch information from.
     * @param poolId The ID of the pool to fetch information for.
     * @return A PoolInfo object containing the updated pool information.
     */
    private suspend fun updatePoolInfo(
        userAddress: String,
        poolAddress: String,
        poolId: Int
    ): PoolInfo {
        val poolInfo: PoolInfo = PoolInfo(
            address = poolAddress,
            poolId = poolId,
        )
        try {
            val poolsWrapper = CruxpoolService.getPoolsInfo(poolAddress)
            val nbStakedNftUser = EtherscanService.getUserStakedNft(poolId, userAddress)
            val nbStakedNft = EtherscanService.getAllStakedNft(poolId)
            val poolEarnings = poolsWrapper.data.coinPerMins * 60 * 24

            poolInfo.nbStakedNftUser = nbStakedNftUser
            poolInfo.nbStakedNft = nbStakedNft
            poolInfo.userEarnings = poolEarnings / nbStakedNft * nbStakedNftUser
            poolInfo.poolEarnings = poolEarnings
            poolInfo.hashrate = poolsWrapper.data.realtimeHashrate
        } catch (e: Exception) {
            Log.e("SessionManager", "Error fetching pool info: ${e.message}")
        }

        return poolInfo
    }
}