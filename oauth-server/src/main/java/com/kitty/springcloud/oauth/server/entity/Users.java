package com.kitty.springcloud.oauth.server.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author kitty
 * @since 2018-07-02
 */
@TableName("tbl_users")
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 登陆帐户
     */
    private String account;
    /**
     * 密码
     */
    private String password;
    /**
     * 用户类型(1普通用户2管理员3系统用户)
     */
    @TableField("user_type")
    private String userType;
    /**
     * 姓名
     */
    @TableField("user_name")
    private String userName;
    /**
     * 姓名拼音
     */
    @TableField("name_pinyin")
    private String namePinyin;
    /**
     * 性别(0:未知;1:男;2:女)
     */
    private Integer sex;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 电话
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 身份证号码
     */
    @TableField("id_card")
    private String idCard;
    /**
     * 微信
     */
    @TableField("wei_xin")
    private String weiXin;
    /**
     * 微博
     */
    @TableField("wei_bo")
    private String weiBo;
    /**
     * QQ
     */
    private String qq;
    /**
     * 出生日期
     */
    @TableField("birth_day")
    private Date birthDay;
    /**
     * 部门编号
     */
    @TableField("dept_id")
    private Long deptId;
    /**
     * 职位
     */
    private String position;
    /**
     * 详细地址
     */
    private String address;
    /**
     * 工号
     */
    @TableField("staff_no")
    private String staffNo;
    private Integer enable;
    private String remark;
    @TableField("create_time")
    private Date createTime;
    @TableField("create_by")
    private Long createBy;
    @TableField("update_time")
    private Date updateTime;
    @TableField("update_by")
    private Long updateBy;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNamePinyin() {
        return namePinyin;
    }

    public void setNamePinyin(String namePinyin) {
        this.namePinyin = namePinyin;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getWeiXin() {
        return weiXin;
    }

    public void setWeiXin(String weiXin) {
        this.weiXin = weiXin;
    }

    public String getWeiBo() {
        return weiBo;
    }

    public void setWeiBo(String weiBo) {
        this.weiBo = weiBo;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStaffNo() {
        return staffNo;
    }

    public void setStaffNo(String staffNo) {
        this.staffNo = staffNo;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    @Override
    public String toString() {
        return "Users{" +
        ", id=" + id +
        ", account=" + account +
        ", password=" + password +
        ", userType=" + userType +
        ", userName=" + userName +
        ", namePinyin=" + namePinyin +
        ", sex=" + sex +
        ", avatar=" + avatar +
        ", phone=" + phone +
        ", email=" + email +
        ", idCard=" + idCard +
        ", weiXin=" + weiXin +
        ", weiBo=" + weiBo +
        ", qq=" + qq +
        ", birthDay=" + birthDay +
        ", deptId=" + deptId +
        ", position=" + position +
        ", address=" + address +
        ", staffNo=" + staffNo +
        ", enable=" + enable +
        ", remark=" + remark +
        ", createTime=" + createTime +
        ", createBy=" + createBy +
        ", updateTime=" + updateTime +
        ", updateBy=" + updateBy +
        "}";
    }
}
