package com.example.startmining.network.etherscan

import kotlinx.serialization.Serializable

@Serializable
data class NbStakedNftWrapper(
    var result: String
)