package entity;

/**
 * 用户关注帖子的实体类
 */
public class followTz {
    private int id;
    private int uId;
    private int tzId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }

    public int getTzId() {
        return tzId;
    }

    public void setTzId(int tzId) {
        this.tzId = tzId;
    }

    @Override
    public String toString() {
        return "followedTz{" +
                "id=" + id +
                ", uId=" + uId +
                ", tzId=" + tzId +
                '}';
    }
}
