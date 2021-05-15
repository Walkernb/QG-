/**
 * 评论的实体类
 */

package entity;

public class commentEntity {
    private int id;
    private String texts;       //评论内容
    private int uId;
    private int tzId;
    private String photo;       //图片评论

    public commentEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTexts() {
        return texts;
    }

    public void setTexts(String texts) {
        this.texts = texts;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "commentEntity{" +
                "id=" + id +
                ", texts='" + texts + '\'' +
                ", uId=" + uId +
                ", tzId=" + tzId +
                ", photo='" + photo + '\'' +
                '}';
    }
}
