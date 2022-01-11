package com.pelr.socialnetwork_extins.domain;

import java.util.List;

public class Page {
    private User profileOwner;
    private List<User> friendList;

    public Page(User profileOwner) {
        this.profileOwner = profileOwner;
    }

    public User getProfileOwner() {
        return profileOwner;
    }

    public void setProfileOwner(User profileOwner) {
        this.profileOwner = profileOwner;
    }

    public List<User> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<User> friendList) {
        this.friendList = friendList;
    }
}
