package com.ahr.usergithub.model

import com.google.gson.annotations.SerializedName

//{
//    "login": "mojombo",
//    "id": 1,
//    "node_id": "MDQ6VXNlcjE=",
//    "avatar_url": "https://avatars.githubusercontent.com/u/1?v=4",
//    "gravatar_id": "",
//    "url": "https://api.github.com/users/mojombo",
//    "html_url": "https://github.com/mojombo",
//    "followers_url": "https://api.github.com/users/mojombo/followers",
//    "following_url": "https://api.github.com/users/mojombo/following{/other_user}",
//    "gists_url": "https://api.github.com/users/mojombo/gists{/gist_id}",
//    "starred_url": "https://api.github.com/users/mojombo/starred{/owner}{/repo}",
//    "subscriptions_url": "https://api.github.com/users/mojombo/subscriptions",
//    "organizations_url": "https://api.github.com/users/mojombo/orgs",
//    "repos_url": "https://api.github.com/users/mojombo/repos",
//    "events_url": "https://api.github.com/users/mojombo/events{/privacy}",
//    "received_events_url": "https://api.github.com/users/mojombo/received_events",
//    "type": "User",
//    "site_admin": false,
//    "name": "Tom Preston-Werner",
//    "company": "@chatterbugapp, @redwoodjs, @preston-werner-ventures ",
//    "blog": "http://tom.preston-werner.com",
//    "location": "San Francisco",
//    "email": null,
//    "hireable": null,
//    "bio": null,
//    "twitter_username": "mojombo",
//    "public_repos": 62,
//    "public_gists": 62,
//    "followers": 22658,
//    "following": 11,
//    "created_at": "2007-10-20T05:24:19Z",
//    "updated_at": "2021-09-01T22:01:16Z"
//}
data class UserDetail(
    @SerializedName("avatar_url") val avatar: String,
    val name: String,
    @SerializedName("bio") val description: String,
    @SerializedName("public_repos") val repository: Int,
    @SerializedName("followers") val follower: Int,
    @SerializedName("following") val following: Int,
    @SerializedName("public_gists") val gists: Int,
)
