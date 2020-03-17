export const constants = {
    highRatingId: 1,
    lowRatingId: 2
};

export const routes = {
    channels: {
        base: "/channels",
        lastSelected: "/channels/last_selected",
        home: (channelUniqueId) => `/channels/${channelUniqueId}`,
        subscription: "/channels/subscription",
        videos: {
            base: (channelUniqueId, sort) => {
                if (sort) {
                    return `/channels/${channelUniqueId}/videos?sort=${sort}`;
                } else {
                    return `/channels/${channelUniqueId}/videos`;
                }
            },
            new: channelId => `/channels/${channelId}/videos/new`,
            upload: channelId => `/channels/${channelId}/videos/upload`
        }
    },
    login: {
        base: "/login"
    },
    register: {
        base: "/register"
    },
    root: {
        base: "/"
    },
    subscribers: {
        base: "/subscribers",
        destroy: id => `/subscribers/${id}`
    },
    users: {
        subscriptionCount: "/users/subscription_count"
    },
    videoCommentRating: {
        base: "/video_comment_rating",
        destroy: id => `/video_comment_rating/${id}`,
        update: id => `/video_comment_rating/${id}`
    },
    videoComments: {
        base: "/video_comments",
        nextComments: "/video_comments/next_comments",
        replies: "/video_comments/replies"
    },
    videoRating: {
        base: "video_rating"
    },
    videos: {
        base: "/videos"
    },
    watch: {
        base: videoUniqueId => `/watch?v=${videoUniqueId}`
    }
};

export const serverUrl = path => {
    const serverOrigin = "http://localhost:6565";
    return path.startsWith("/") ? serverOrigin + path : serverOrigin + "/" + path;
};

export const transition = to => {
    location.href = to;
};

export function uuid() {
    const chars = "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".split("");
    for (let i = 0, len = chars.length; i < len; ++i) {
        switch (chars[i]) {
            case "x":
                chars[i] = Math.floor(Math.random() * 16).toString(16);
                break;
            case "y":
                chars[i] = (Math.floor(Math.random() * 4) + 8).toString(16);
                break;
        }
    }
    return chars.join("");
}