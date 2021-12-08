package com.dony.dramatest.util

import com.dony.dramatest.data.local.entity.UserEntity
import java.util.*
import kotlin.collections.ArrayList

object TestUtil {

    fun createUser(id: Int) = UserEntity(
        login = "GodDony",
        id = id,
        node_id = "MDQ6VXNlcjQzMTY2Mjky",
        avatar_url = "https://avatars.githubusercontent.com/u/43166292?v=4",
        gravatar_id = "",
        public_repos = 15,
        public_gists = 11,
        followers = 111,
        following = 111,
        url = "https://api.github.com/users/GodDony",
        html_url = "https://github.com/GodDony",
        followers_url = "https://api.github.com/users/GodDony/followers",
        following_url = "https://api.github.com/users/GodDony/following{/other_user}",
        gists_url = "https://api.github.com/users/GodDony/gists{/gist_id}",
        starred_url = "https://api.github.com/users/GodDony/starred{/owner}{/repo}",
        subscriptions_url = "https://api.github.com/users/GodDony/subscriptions",
        organizations_url = "https://api.github.com/users/GodDony/orgs",
        repos_url = "https://api.github.com/users/GodDony/repos",
        events_url = "https://api.github.com/users/GodDony/events{/privacy}",
        received_events_url = "https://api.github.com/users/GodDony/received_events",
        type = "User",
        site_admin = false,
        name = null,
        company = null,
        blog = null,
        location = null,
        email = null,
        hireable = null,
        bio = null,
        twitter_username = null,
        created_at = Date(),
        updated_at = Date()
    )

    fun createUserList(size: Int): ArrayList<UserEntity> {
        val list = ArrayList<UserEntity>()
        for (i in 1..size) {
            list.add(createUser(i))
        }
        return list
    }
}