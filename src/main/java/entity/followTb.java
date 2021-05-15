package entity;

/**
 * 用户关注贴吧的实体类
 */
public class followTb {
    private int id;
    private int uId;
    private int tbId;

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

    public int getTbId() {
        return tbId;
    }

    public void setTbId(int tbId) {
        this.tbId = tbId;
    }

    @Override
    public String toString() {
        return "followedTb{" +
                "id=" + id +
                ", uId=" + uId +
                ", tbId=" + tbId +
                '}';
    }
}
