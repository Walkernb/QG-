/**
 * 贴吧的实体类
 */

package entity;

public class tiebaEntity {
    private int    id ;
    private String name;        //贴吧名
    private int    number;      //帖子的数量
    private int    uId;
    private String notice;      //贴吧公告
    private String status;      //贴吧的状态

    public tiebaEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "tiebaEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", number=" + number +
                ", uId=" + uId +
                ", notice='" + notice + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
