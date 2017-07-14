package com.suikajy.newsapp.local.table;

import android.os.Parcel;
import android.os.Parcelable;

import com.suikajy.playerview.danmaku.BaseDanmakuData;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by suikajy on 2017/5/2.
 * danmaku table
 */
@Entity
public class DanmakuInfo extends BaseDanmakuData implements Parcelable {

    // 为了数据库字段生成，把基类的字段拷过来，没找到GreenDao生成父类字段的方式
    private int type;
    private String content;
    private long time;
    private float textSize;
    private int textColor;
    // 用户名
    private String userName;
    // 对应一个视频
    @Id
    private String vid;

    @Override
    public int getType() {
        return type;
    }

    @Override
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public long getTime() {
        return time;
    }

    @Override
    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public float getTextSize() {
        return textSize;
    }

    @Override
    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    @Override
    public int getTextColor() {
        return textColor;
    }

    @Override
    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    @Override
    public String toString() {
        return "DanmakuInfo{" +
                "type=" + type +
                ", content='" + content + '\'' +
                ", time=" + time +
                ", textSize=" + textSize +
                ", textColor=" + textColor +
                ", userName='" + userName + '\'' +
                ", vid='" + vid + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeString(this.content);
        dest.writeLong(this.time);
        dest.writeFloat(this.textSize);
        dest.writeInt(this.textColor);
        dest.writeString(this.userName);
        dest.writeString(this.vid);
    }

    public DanmakuInfo() {
    }

    protected DanmakuInfo(Parcel in) {
        this.type = in.readInt();
        this.content = in.readString();
        this.time = in.readLong();
        this.textSize = in.readFloat();
        this.textColor = in.readInt();
        this.userName = in.readString();
        this.vid = in.readString();
    }

    @Generated(hash = 297831740)
    public DanmakuInfo(int type, String content, long time, float textSize, int textColor, String userName,
            String vid) {
        this.type = type;
        this.content = content;
        this.time = time;
        this.textSize = textSize;
        this.textColor = textColor;
        this.userName = userName;
        this.vid = vid;
    }

    public static final Parcelable.Creator<DanmakuInfo> CREATOR = new Parcelable.Creator<DanmakuInfo>() {
        @Override
        public DanmakuInfo createFromParcel(Parcel source) {
            return new DanmakuInfo(source);
        }

        @Override
        public DanmakuInfo[] newArray(int size) {
            return new DanmakuInfo[size];
        }
    };
}
