package com.tensquare.friend.service;

import com.tensquare.friend.dao.FriendDao;
import com.tensquare.friend.dao.NoFriendDao;
import com.tensquare.friend.pojo.Friend;
import com.tensquare.friend.pojo.NoFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class FriendService {
    @Autowired
    private FriendDao friendDao;

    @Autowired
    private NoFriendDao nofriendDao;

    public int addFriend(String userid, String friendid) {
            //先判断userid到friendid是否有数据,有就是重复添加好友,返回0
        Friend friend = friendDao.findByUseridAndFriendid(userid, friendid);
        if(friend!=null){
            return 0;
        }
        //直接添加好友,让好友表中userid到friendid方向的type为0
        friend=new Friend();
        friend.setUserid(userid);
        friend.setFriendid(friendid);
        friend.setIslike("0");//单向喜欢,状态为0.
        friendDao.save(friend);
        //判断从friend到userid是否有数据,如果有,把双方的状态改为1
        if(friendDao.findByUseridAndFriendid(userid, friendid)!=null){
            //把双方的islike都改成1
            friendDao.updateIslike("1",userid,friendid);
            friendDao.updateIslike("1",friendid,userid);
        }
        return 1;
    }

    public int addNoFriend(String userid, String friendid) {
        NoFriend nofriend = nofriendDao.findByUseridAndFriendid(userid, friendid);
        if(nofriend!=null){
            return 0;
        }
        //直接添加非好友
        nofriend=new NoFriend();
        nofriend.setUserid(userid);
        nofriend.setFriendid(friendid);
        nofriendDao.save(nofriend);
        return 1;
    }
    public void deleteFriend(String userid, String friendid) {
        //删除好友表中userid到friendid这条数据
        friendDao.deletefriend(userid,friendid);
        //更新friend到userid的islike为0
        friendDao.updateIslike("0",friendid,userid);
        //非好友表中添加数据
        NoFriend nofriend=new NoFriend();
        nofriend.setUserid(userid);
        nofriend.setFriendid(friendid);
        nofriendDao.save(nofriend);
    }

}
