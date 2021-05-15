/**
 * 帖子的实体类
 */

package entity;

public class tieziEntity {
    private int id;
    private String name;        //帖子名
    private String texts;       //帖子内容
    private int number;         //评论数
    private int likes;          //点赞数
    private int uId;
    private int tbId;

    public tieziEntity() {
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

    public String getTexts() {
        return texts;
    }

    public void setTexts(String texts) {
        this.texts = texts;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
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
        return "tieziEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", texts='" + texts + '\'' +
                ", number=" + number +
                ", likes=" + likes +
                ", uId=" + uId +
                ", tbId=" + tbId +
                '}';
    }
}
