package com.example.projecttravel.model

import kotlinx.serialization.Serializable

interface AllBoards {
}

@Serializable
data class Board (
    val articleNo: String,
    val title: String,
    val content: String,
    val writeDate: String,
    val views: String,
    val writeId: String,
    val userId: String,
): AllBoards

@Serializable
data class Company (
    val articleNo: String,
    val title: String,
    val content: String,
    val writeDate: String,
    val views: String,
    val writeId: String,
    val userId: String,
): AllBoards

@Serializable
data class Trade (
    val articleNo: String,
    val title: String,
    val content: String,
    val writeDate: String,
    val views: String,
    val writeId: String,
    val userId: String,
): AllBoards

@Serializable
data class Reply (
    val replyNo: String,
    val boardEntity: String,
    val tradeEntity: String,
    val companyEntity: String,
    val replyContent: String,
    val writeDate: String,
    val writeId: String,
    val userId: String,
)

//----------------------------------
// DataClass for Call
data class BoardList (
    val pages: Int,
    val list: List<BoardEntity>,
    val replyCount: List<ReplyCountList>,
)

@Serializable
data class CompanyList (
    val pages: Int,
    val list: List<CompanyEntity>,
    val replyCount: List<ReplyCountList>,
)

@Serializable
data class TradeList (
    val pages: Int,
    val list: List<TradeEntity>,
    val replyCount: List<ReplyCountList>,
)

@Serializable
data class ReplyCountList (
    val articleNo: Int,
    val replyCount: Int,
)

@Serializable
data class ReplyList (
    val replyNo: String,
    val boardEntity: String,
    val tradeEntity: String,
    val companyEntity: String,
    val replyContent: String,
    val writeDate: String,
    val writeId: String,
    val userId: String,
)

interface AllBoardsEntity {
}

@Serializable
data class BoardEntity (
    val articleNo: Int,
    val title: String,
    val content: String,
    val writeDate: String,
    val views: Int,
    val writeId: String,
    val user: UserDto,
): AllBoardsEntity

@Serializable
data class CompanyEntity (
    val articleNo: Int,
    val title: String,
    val content: String,
    val writeDate: String,
    val views: Int,
    val writeId: String,
    val user: UserDto,
): AllBoardsEntity

@Serializable
data class TradeEntity (
    val articleNo: Int,
    val title: String,
    val content: String,
    val writeDate: String,
    val views: Int,
    val writeId: String,
    val user: UserDto,
): AllBoardsEntity

@Serializable
data class UserDto (
    val id: Int,
    val name: String,
    val email: String,
    val picture: String?,
    val role: String?,
    val type: String?,
    val password: String?,
)

