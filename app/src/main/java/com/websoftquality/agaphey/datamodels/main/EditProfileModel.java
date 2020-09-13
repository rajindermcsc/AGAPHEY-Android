package com.websoftquality.agaphey.datamodels.main;
/**
 * @package com.websoftquality.agaphey
 * @subpackage datamodels.main
 * @category EditProfileModel
 * @author Trioangle Product Team
 * @version 1.0
 **/

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/*****************************************************************
 Edit Profile Model
 ****************************************************************/
public class EditProfileModel {

    @SerializedName("status_message")
    @Expose
    private String message;
    @SerializedName("status_code")
    @Expose
    private String code;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("image_url")
    @Expose
    private ArrayList<ImageModel> imageList = new ArrayList<>(5);
    @SerializedName("about")
    @Expose
    private String about;
    @SerializedName("job_title")
    @Expose
    private String jobTitle;
    @SerializedName("work")
    @Expose
    private String work;
    @SerializedName("college")
    @Expose
    private String college;
    @SerializedName("instagram_id")
    @Expose
    private String instagramId;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("show_my_age")
    @Expose
    private String showMyAge;
    @SerializedName("distance_invisible")
    @Expose
    private String distanceInvisible;
    @SerializedName("plan_type")
    @Expose
    private String planType;
    @SerializedName("is_order")
    @Expose
    private String isOrder;
    @SerializedName("image_id")
    @Expose
    private String image_id;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJesus_rel() {
        return jesus_rel;
    }

    public void setJesus_rel(String jesus_rel) {
        this.jesus_rel = jesus_rel;
    }

    public String getLifestyle() {
        return lifestyle;
    }

    public void setLifestyle(String lifestyle) {
        this.lifestyle = lifestyle;
    }

    public String getPartner_char() {
        return partner_char;
    }

    public void setPartner_char(String partner_char) {
        this.partner_char = partner_char;
    }

    public String getDeal_breakers() {
        return deal_breakers;
    }

    public void setDeal_breakers(String deal_breakers) {
        this.deal_breakers = deal_breakers;
    }

    public String getVision() {
        return vision;
    }

    public void setVision(String vision) {
        this.vision = vision;
    }

    public String getTrue_abt() {
        return true_abt;
    }

    public void setTrue_abt(String true_abt) {
        this.true_abt = true_abt;
    }

    public String getDrink() {
        return drink;
    }

    public void setDrink(String drink) {
        this.drink = drink;
    }

    public String getChildren() {
        return children;
    }

    public void setChildren(String children) {
        this.children = children;
    }

    public String getRelocate() {
        return relocate;
    }

    public void setRelocate(String relocate) {
        this.relocate = relocate;
    }

    public String getAlive() {
        return alive;
    }

    public void setAlive(String alive) {
        this.alive = alive;
    }

    public String getFun() {
        return fun;
    }

    public void setFun(String fun) {
        this.fun = fun;
    }

    public String getLord_speaking() {
        return lord_speaking;
    }

    public void setLord_speaking(String lord_speaking) {
        this.lord_speaking = lord_speaking;
    }

    public String getWhy_join() {
        return why_join;
    }

    public void setWhy_join(String why_join) {
        this.why_join = why_join;
    }

    public String getPray() {
        return pray;
    }

    public void setPray(String pray) {
        this.pray = pray;
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("jesus_rel")
    @Expose
    private String jesus_rel;

    @SerializedName("lifestyle")
    @Expose
    private String lifestyle;

    @SerializedName("partner_char")
    @Expose
    private String partner_char;

    @SerializedName("deal_breakers")
    @Expose
    private String deal_breakers;

    @SerializedName("vision")
    @Expose
    private String vision;

    @SerializedName("true_abt")
    @Expose
    private String true_abt;

    @SerializedName("drink")
    @Expose
    private String drink;

    @SerializedName("children")
    @Expose
    private String children;

    @SerializedName("relocate")
    @Expose
    private String relocate;

    @SerializedName("alive")
    @Expose
    private String alive;

    @SerializedName("fun")
    @Expose
    private String fun;

    @SerializedName("lord_speaking")
    @Expose
    private String lord_speaking;

    @SerializedName("why_join")
    @Expose
    private String why_join;

    @SerializedName("pray")
    @Expose
    private String pray;

    @SerializedName("additional")
    @Expose
    private String additional;



    @SerializedName("dob")
    @Expose
    private String dob;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public ArrayList<ImageModel> getImageList() {
        return imageList;
    }

    public void setImageList(ArrayList<ImageModel> imageList) {
        this.imageList = imageList;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getInstagramId() {
        return instagramId;
    }

    public void setInstagramId(String instagramId) {
        this.instagramId = instagramId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getShowMyAge() {
        return showMyAge;
    }

    public void setShowMyAge(String showMyAge) {
        this.showMyAge = showMyAge;
    }

    public String getDistanceInvisible() {
        return distanceInvisible;
    }

    public void setDistanceInvisible(String distanceInvisible) {
        this.distanceInvisible = distanceInvisible;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getIsOrder() {
        return isOrder;
    }

    public void setIsOrder(String isOrder) {
        this.isOrder = isOrder;
    }
}
