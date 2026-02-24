package com.example.startmining.network.pools

import com.example.startmining.R
import android.content.Context
import android.util.Log
import com.example.startmining.network.cruxpool.CruxpoolService
import com.example.startmining.network.etherscan.EtherscanService
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock


/* Pool Order :
1 - Origin
2 - Genesis
3 - NorthPool
4 - Pulse
5 - Horizon
 */

/**
 * PoolsService is responsible for managing the pool information for all pools.
 * It provides functions to initialize the pool addresses and update the pool information by fetching it from the Cruxpool API and the Etherscan API.
 */
object PoolsService {
    private lateinit var poolAddresses: List<String>
    private val poolIds = listOf(1, 2, 3, 4, 5)
    private val mutex = Mutex()

    fun initialize(context: Context) {
        poolAddresses = listOf(
            context.getString(R.string.origin_address),
            context.getString(R.string.genesis_address),
            context.getString(R.string.northpool_address),
            context.getString(R.string.pulse_address),
            context.getString(R.string.horizon_address)
        )
    }

    /**
     * This function updates the pool information for all pools.
     * It fetches the pool information for each pool address and updates the corresponding PoolInfo object.
     * @param userAddress The user's wallet address (ETH).
     */
    suspend fun updatePoolsInfo(userAddress: String): MutableList<PoolInfo> {
        val poolsInfo = mutableListOf<PoolInfo>()

        poolAddresses.zip(poolIds).forEach { (address, id) ->
            try {
                val poolInfo = updatePoolInfo(userAddress, address, id)
                mutex.withLock {
                    poolsInfo.add(poolInfo)
                }
            } catch (e: Exception) {
                Log.e("PoolsService", "Error updating pool info for pool ID $id: ${e.message}")
            }
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
        val poolInfo = PoolInfo(
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