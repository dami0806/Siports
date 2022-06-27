package com.bor2h.siportstest

data class UserProfile(
    var profileImageUrl: String? = null,
    var name: String? = null,
    var nationality: String? = null,
    var gender: String? = null,
    var age: String? = null,
    var event: String? = null,
) { constructor(): this("", "닉네임 입력", "", "", "", "")
}
