package com.sendy.sendyLegacyApi.support.util

import com.github.f4b6a3.tsid.TsidCreator

fun getTsid(): Long = TsidCreator.getTsid().toLong()
