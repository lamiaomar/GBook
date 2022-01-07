package com.example.gbook.ui.util

import java.util.concurrent.atomic.AtomicInteger


object RandomUtil {
    private val seed = AtomicInteger()
    fun getRandomInt() = seed.getAndIncrement() + System.currentTimeMillis().toInt()
}