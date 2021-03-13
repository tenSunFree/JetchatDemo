package com.example.jetchatdemo.home

private val initialMessages = listOf(
    Message(
        "Aaron",
        "外商的薪水真的比台企高，連派遣員工的薪資水平都比台企同樣職位的人高，所以在外商做萬年派遣的人不少。"
    ),
    Message(
        "Blake",
        "名片上掛一個閃閃亮亮舉世知名的外商 Logo，就是高大上啊，好感度與魅力值激升 50%。"
    ),
    Message(
        "Reuben",
        "在職場多打滾幾年，你就會發現，會念書的人不見得會上班。"
    ),
    Message(
        "me",
        "高壓驅動高成長，在競爭激烈的外商，不思進步就要抱對大腿，否則很容易死在沙灘上。"
    ),
    Message(
        "Oren",
        "在外商工作英文一定要好嗎？"
    ),
    Message(
        "me",
        "簡單說，英文好固然有加分效果，但若無法拿出實績，英文再好也無法獲得主管青睞。"
    )
)

val homeState = HomeState(initialMessages = initialMessages)