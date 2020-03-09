export const constants = {
    highRatingId: 1,
    lowRatingId: 2
};

export const routes = {
    channels: {
        base: "/channels",
        lastSelected: "/channels/last_selected",
        videos: {
            base: channelId => `/channels/${channelId}/videos`,
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
    videoComments: {
        nextComments: "/video_comments/next_comments"
    },
    videoRating: {
        base: "video_rating"
    },
    watch: {
        base: videoUniqueId => `/watch?v=${videoUniqueId}`
    }
};

export const serverUrl = path => {
    return "http://localhost:6900/" + path;
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