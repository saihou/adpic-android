package com.adpic.adpic;

/**
 * Created by saihou on 2/19/16.
 */
public class ChallengeCardData {
    String merchantName;
    String challengeDuration;
    String challengeRestaurant;
    String challengeDistance;
    String caption;
    String picture;
    String participants;

    public ChallengeCardData(String username, String time, String challengeRestaurant, String challengeDistance, String caption) {
        this.merchantName = username;
        this.challengeDuration = time;
        this.challengeRestaurant = challengeRestaurant;
        this.challengeDistance = challengeDistance;
        this.caption = caption;
    }

    public ChallengeCardData(String username, String time, String challengeRestaurant, String challengeDistance, String caption, String picture) {
        this.merchantName = username;
        this.challengeDuration = time;
        this.challengeRestaurant = challengeRestaurant;
        this.challengeDistance = challengeDistance;
        this.caption = caption;
        this.picture = picture;
    }

    public ChallengeCardData(String username, String time, String challengeRestaurant, String challengeDistance, String caption, String picture, String participants) {
        this.merchantName = username;
        this.challengeDuration = time;
        this.challengeRestaurant = challengeRestaurant;
        this.challengeDistance = challengeDistance;
        this.caption = caption;
        this.picture = picture;
        this.participants = participants;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPicture() {
        return picture;
    }
    public String getMerchantName() {
        return merchantName;
    }
    public String getTime() {
        return challengeDuration;
    }
    public String getCaption() {
        return caption;
    }
    public String getChallengeDistance() {
        return challengeDistance;
    }
    public String getChallengeRestaurant() {
        return challengeRestaurant;
    }
    public String getParticipants() {
        return participants;
    }
}
