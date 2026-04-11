package com.thomas.androidbase.data

import com.google.gson.annotations.SerializedName

/**
 * Created by thomas on 4/11/2026.
 */

data class ZhihuHot(
    @SerializedName("data")
    val data: List<ZhihuNewsItem>,
    
    @SerializedName("paging")
    val paging: Paging,
    
    @SerializedName("fresh_text")
    val freshText: String
)

data class ZhihuNewsItem(
    @SerializedName("is_recommended")
    val isRecommended: Boolean,
    
    @SerializedName("type")
    val type: String,
    
    @SerializedName("target_type")
    val targetType: String,
    
    @SerializedName("attached_info")
    val attachedInfo: String,
    
    @SerializedName("target")
    val target: Target
)

data class Target(
    @SerializedName("question")
    val question: Question,
    
    @SerializedName("can_comment")
    val canComment: CanComment,
    
    @SerializedName("type")
    val type: String,
    
    @SerializedName("author")
    val author: Author,
    
    @SerializedName("url")
    val url: String,
    
    @SerializedName("comment_permission")
    val commentPermission: String,
    
    @SerializedName("excerpt")
    val excerpt: String,
    
    @SerializedName("preview_type")
    val previewType: String,
    
    @SerializedName("thumbnail")
    val thumbnail: String,
    
    @SerializedName("comment_count")
    val commentCount: Int,
    
    @SerializedName("thanks_count")
    val thanksCount: Int,
    
    @SerializedName("excerpt_new")
    val excerptNew: String,
    
    @SerializedName("created_time")
    val createdTime: Long,
    
    @SerializedName("preview_text")
    val previewText: String,
    
    @SerializedName("updated_time")
    val updatedTime: Long,
    
    @SerializedName("voteup_count")
    val voteupCount: Int,
    
    @SerializedName("id")
    val id: Long,
    
    @SerializedName("is_copyable")
    val isCopyable: Boolean
)

data class Question(
    @SerializedName("bound_topic_ids")
    val boundTopicIds: List<Int>,
    
    @SerializedName("excerpt")
    val excerpt: String,
    
    @SerializedName("answer_count")
    val answerCount: Int,
    
    @SerializedName("is_following")
    val isFollowing: Boolean,
    
    @SerializedName("id")
    val id: Long,
    
    @SerializedName("author")
    val author: Author,
    
    @SerializedName("url")
    val url: String,
    
    @SerializedName("title")
    val title: String,
    
    @SerializedName("created")
    val created: Long,
    
    @SerializedName("comment_count")
    val commentCount: Int,
    
    @SerializedName("follower_count")
    val followerCount: Int,
    
    @SerializedName("type")
    val type: String
)

data class Author(
    @SerializedName("is_followed")
    val isFollowed: Boolean,
    
    @SerializedName("user_type")
    val userType: String,
    
    @SerializedName("badge")
    val badge: List<Any>,
    
    @SerializedName("is_following")
    val isFollowing: Boolean,
    
    @SerializedName("url_token")
    val urlToken: String,
    
    @SerializedName("id")
    val id: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("url")
    val url: String,
    
    @SerializedName("gender")
    val gender: Int,
    
    @SerializedName("vip_info")
    val vipInfo: Map<String, Any>,
    
    @SerializedName("headline")
    val headline: String,
    
    @SerializedName("avatar_url")
    val avatarUrl: String,
    
    @SerializedName("is_org")
    val isOrg: Boolean,
    
    @SerializedName("type")
    val type: String
)

data class CanComment(
    @SerializedName("status")
    val status: Boolean,
    
    @SerializedName("reason")
    val reason: String
)

data class Paging(
    @SerializedName("is_end")
    val isEnd: Boolean,
    
    @SerializedName("next")
    val next: String,
    
    @SerializedName("is_start")
    val isStart: Boolean,
    
    @SerializedName("previous")
    val previous: String
)