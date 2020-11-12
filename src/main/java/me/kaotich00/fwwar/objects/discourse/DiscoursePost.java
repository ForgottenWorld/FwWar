package me.kaotich00.fwwar.objects.discourse;

public class DiscoursePost {

    long id;
    String name;
    String username;
    String avatarTemplate;
    long createdAt;
    String content;
    long postNumber;
    int postType;
    long updatedAt;
    int replyCount;
    int replyToPostNumber;
    int quoteCount;
    int incomingLinkCount;
    int reads;
    int readers_count;
    float score;
    boolean yours;
    int topicId;
    String topicSlug;
    String displayUsername;
    String primaryGroupName;
    String primaryGroupFlairUrl;
    String primaryGroupFlairBgColor;
    String primaryGroupFlairColor;
    int version;
    boolean canEdit;
    boolean canDelete;
    boolean canRecover;
    boolean canWiki;
    boolean read;
    String userTitle;
    boolean titleIsGroup;
    boolean bookmarked;
    boolean moderator;
    boolean admin;
    boolean staff;
    long userId;
    boolean hidden;
    int trustLevel;
    long deletedAt;
    boolean userDeleted;
    String editReason;
    boolean canViewEditHistory;
    boolean wiki;
    long reviewableId;
    int reviewableScoreCount;
    int reviewableScorePendingCount;
    boolean canAcceptAnswer;
    boolean canUnacceptAnswer;
    boolean acceptedAnswer;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatarTemplate() {
        return avatarTemplate;
    }

    public void setAvatarTemplate(String avatarTemplate) {
        this.avatarTemplate = avatarTemplate;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getPostNumber() {
        return postNumber;
    }

    public void setPostNumber(long postNumber) {
        this.postNumber = postNumber;
    }

    public int getPostType() {
        return postType;
    }

    public void setPostType(int postType) {
        this.postType = postType;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public int getReplyToPostNumber() {
        return replyToPostNumber;
    }

    public void setReplyToPostNumber(int replyToPostNumber) {
        this.replyToPostNumber = replyToPostNumber;
    }

    public int getQuoteCount() {
        return quoteCount;
    }

    public void setQuoteCount(int quoteCount) {
        this.quoteCount = quoteCount;
    }

    public int getIncomingLinkCount() {
        return incomingLinkCount;
    }

    public void setIncomingLinkCount(int incomingLinkCount) {
        this.incomingLinkCount = incomingLinkCount;
    }

    public int getReads() {
        return reads;
    }

    public void setReads(int reads) {
        this.reads = reads;
    }

    public int getReaders_count() {
        return readers_count;
    }

    public void setReaders_count(int readers_count) {
        this.readers_count = readers_count;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public boolean isYours() {
        return yours;
    }

    public void setYours(boolean yours) {
        this.yours = yours;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public String getTopicSlug() {
        return topicSlug;
    }

    public void setTopicSlug(String topicSlug) {
        this.topicSlug = topicSlug;
    }

    public String getDisplayUsername() {
        return displayUsername;
    }

    public void setDisplayUsername(String displayUsername) {
        this.displayUsername = displayUsername;
    }

    public String getPrimaryGroupName() {
        return primaryGroupName;
    }

    public void setPrimaryGroupName(String primaryGroupName) {
        this.primaryGroupName = primaryGroupName;
    }

    public String getPrimaryGroupFlairUrl() {
        return primaryGroupFlairUrl;
    }

    public void setPrimaryGroupFlairUrl(String primaryGroupFlairUrl) {
        this.primaryGroupFlairUrl = primaryGroupFlairUrl;
    }

    public String getPrimaryGroupFlairBgColor() {
        return primaryGroupFlairBgColor;
    }

    public void setPrimaryGroupFlairBgColor(String primaryGroupFlairBgColor) {
        this.primaryGroupFlairBgColor = primaryGroupFlairBgColor;
    }

    public String getPrimaryGroupFlairColor() {
        return primaryGroupFlairColor;
    }

    public void setPrimaryGroupFlairColor(String primaryGroupFlairColor) {
        this.primaryGroupFlairColor = primaryGroupFlairColor;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    public boolean isCanRecover() {
        return canRecover;
    }

    public void setCanRecover(boolean canRecover) {
        this.canRecover = canRecover;
    }

    public boolean isCanWiki() {
        return canWiki;
    }

    public void setCanWiki(boolean canWiki) {
        this.canWiki = canWiki;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getUserTitle() {
        return userTitle;
    }

    public void setUserTitle(String userTitle) {
        this.userTitle = userTitle;
    }

    public boolean isTitleIsGroup() {
        return titleIsGroup;
    }

    public void setTitleIsGroup(boolean titleIsGroup) {
        this.titleIsGroup = titleIsGroup;
    }

    public boolean isBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        this.bookmarked = bookmarked;
    }

    public boolean isModerator() {
        return moderator;
    }

    public void setModerator(boolean moderator) {
        this.moderator = moderator;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isStaff() {
        return staff;
    }

    public void setStaff(boolean staff) {
        this.staff = staff;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public int getTrustLevel() {
        return trustLevel;
    }

    public void setTrustLevel(int trustLevel) {
        this.trustLevel = trustLevel;
    }

    public long getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(long deletedAt) {
        this.deletedAt = deletedAt;
    }

    public boolean isUserDeleted() {
        return userDeleted;
    }

    public void setUserDeleted(boolean userDeleted) {
        this.userDeleted = userDeleted;
    }

    public String getEditReason() {
        return editReason;
    }

    public void setEditReason(String editReason) {
        this.editReason = editReason;
    }

    public boolean isCanViewEditHistory() {
        return canViewEditHistory;
    }

    public void setCanViewEditHistory(boolean canViewEditHistory) {
        this.canViewEditHistory = canViewEditHistory;
    }

    public boolean isWiki() {
        return wiki;
    }

    public void setWiki(boolean wiki) {
        this.wiki = wiki;
    }

    public long getReviewableId() {
        return reviewableId;
    }

    public void setReviewableId(long reviewableId) {
        this.reviewableId = reviewableId;
    }

    public int getReviewableScoreCount() {
        return reviewableScoreCount;
    }

    public void setReviewableScoreCount(int reviewableScoreCount) {
        this.reviewableScoreCount = reviewableScoreCount;
    }

    public int getReviewableScorePendingCount() {
        return reviewableScorePendingCount;
    }

    public void setReviewableScorePendingCount(int reviewableScorePendingCount) {
        this.reviewableScorePendingCount = reviewableScorePendingCount;
    }

    public boolean isCanAcceptAnswer() {
        return canAcceptAnswer;
    }

    public void setCanAcceptAnswer(boolean canAcceptAnswer) {
        this.canAcceptAnswer = canAcceptAnswer;
    }

    public boolean isCanUnacceptAnswer() {
        return canUnacceptAnswer;
    }

    public void setCanUnacceptAnswer(boolean canUnacceptAnswer) {
        this.canUnacceptAnswer = canUnacceptAnswer;
    }

    public boolean isAcceptedAnswer() {
        return acceptedAnswer;
    }

    public void setAcceptedAnswer(boolean acceptedAnswer) {
        this.acceptedAnswer = acceptedAnswer;
    }

}
