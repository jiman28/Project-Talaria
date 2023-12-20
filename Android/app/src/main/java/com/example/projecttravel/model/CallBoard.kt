package com.example.projecttravel.model

import kotlinx.serialization.Serializable

@Serializable
data class CallBoard (
    val kw: String,
    val page: Int,
    val type: String,
    val email: String,
)

@Serializable
data class CallReply (
    val tabtitle: String,
    val articleNo: String,
)

//==========
@Serializable
data class SendArticle (
    val tabTitle: String,
    val title: String,
    val content: String,
    val email: String,
)

@Serializable
data class RemoveArticle (
    val tabTitle: String,
    val articleNo: String,
)

@Serializable
data class SendComment (
    val tabTitle: String,
    val articleNo: String,
    val replyContent: String,
    val email: String,
)

@Serializable
data class RemoveComment(
    val tabTitle: String,
    val articleNo: String,
    val replyNo: String,
)
