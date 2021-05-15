package service.tieba;

import dao.FollowDao;
import dao.TiebaDao;
import entity.followTb;
import entity.tiebaEntity;
import util.JDBCutils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class tiebaService {

    /**
     * 根据id，返回用户关注的贴吧信息
     * @param uId
     * @return
     */
    public List<tiebaEntity> getById(int uId){
        boolean flag = true;
        List<tiebaEntity> tbList = new ArrayList<>();
        List<followTb> ftbList = new FollowDao().getByUid_Tb(uId);
        if(ftbList!=null){
            for (followTb ftb : ftbList) {
                tiebaEntity tb = new TiebaDao().getById(ftb.getTbId());
                tbList.add(tb);
            }
        }else{
            tbList = null;
        }
        return tbList;
    }


}
