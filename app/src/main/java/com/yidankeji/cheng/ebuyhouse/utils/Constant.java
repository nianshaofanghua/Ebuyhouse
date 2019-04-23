package com.yidankeji.cheng.ebuyhouse.utils;

/**
 * Created by 刘灿成 on 2017\11\29 0029.
 * 接口文档
 */

public class Constant {
   public static final String BASEPATH = "http://47.254.45.106";
    public static String PATH = BASEPATH + "/app";
    //搜索的联想词
    public static String fcitystate = PATH + "/o/h/fcitystate?";
    //根据城市名字得到城市的边缘经纬度
    public static String shape = PATH + "/o/gm/shape?";
    //输入手机号或email验证是否注册，进行登录或注册
    public static String vaccount = PATH + "/o/c/vaccount?";

    //发送邮箱验证码
    public static String emailcode = PATH + "/o/email/scode?";

    //获取随机串
    public static String getrandomcode = PATH + "/o/sms/";

    //发送密码，完成注册，成功返回token
    public static String submitRegistration = PATH + "/o/c/reg?";

    //验证短信验证码
    public static String yanzhengCode = PATH + "/o/sms/vcode?";

    //验证邮箱验证码
    public static String yanzhengEmail = PATH + "/o/email/vcode?";

    //登录
    public static String login = PATH + "/o/c/login?";

    //获取当前登录用户的信息
    public static String myinfo = PATH + "/c/finfo?";

    //更新昵称
    public static String unick = PATH + "/c/unick?";

    //更新头像
    public static String uheadiag = PATH + "/c/uhead?";

    //修改用户信息
    public static String uinfo = PATH + "/c/uinfo?";

    //用户通用信息修改
    public static String editMyInfo = PATH + "/c/edit?";

    //忘记密码
    public static String forgetPW = PATH + "/o/sms/forgetpsd?";

    //根据城市id进行查询
    public static String fbcity = PATH + "/o/h/fbcity?";

    //根据州id进行查询
    public static String fbstate = PATH + "/o/h/fbstate?";

    //上传文件
    public static String uhead = PATH + "/file/up/";

    //收藏/取消收藏
    public static String collect = PATH + "/c/collect?";

    //查询房屋类型列表
    public static String category = PATH + "/o/h/category?";

    //筛选房屋基本参数信息
    public static String filterArg = PATH + "/o/h/filterArg?module=sale";

    //筛选房屋数据，以下数据如果是范围数据，用'-'分割：100-200，如果只传100,表示>=100
    public static String filter = PATH + "/o/h/filter?";

    //输入地址字符串，返回自动补全信息
    public static String address = PATH + "/o/gm/address?";

    // 收藏列表
    public static String shoucanglist = PATH + "/c/list?";

    //输入城市字符串，返回自动补全信息
    public static String getCityFormKeyword = PATH + "/o/gm/city?";

    //获取所有的州数据
    public static String getStateFormHttp = PATH + "/o/gm/state?";

    //添加房屋信息
    public static String addHouseMessage = PATH + "/h/add?";

    //我的房屋信息列表
    public static String myhouselist = PATH + "/h/my";
    // 我的房屋信息列表 新加sold out
    public static String myhouselistsoldout = PATH + "/h/myBuy";

    //按主键查询房屋所有信息
    public static String fid = PATH + "/o/h/fid?";

    //按主键查询房屋详情页的feature和building
    public static String hinfo = PATH + "/o/h/hinfo?";

    //房屋上下架
    public static String sale = PATH + "/h/sale?";

    //消息列表
    public static String mymessagelist = PATH + "/c/msg/list";

    //追加消息
    public static String addmymessage = PATH + "/c/msg/add?";

    //添加消息
    public static String mymessage = PATH + "/c/msg/addn?";

    //查询所有服务分类
    public static String myServiceType = PATH + "/o/server/list?";

    //查询所有服务--过滤筛选
    public static String servicelist = PATH + "/o/server/flist?";

    //服务商评价列表
    public static String evaluates = PATH + "/o/server/evaluates?";

    //offer统一入口
    public static String offerEnter = PATH + "/offerEnter?";


    //添加服务评价
    public static String addevaluate = PATH + "/c/addevaluate?";

    //上传资金证明
    public static String addfund = PATH + "/h/addfund?";

    //查询是否已经成功上传凭证，未审核及未审核通过视为未上传
    public static String isupfunds = PATH + "/h/isupfunds?";

    //查询我的资金凭证
    public static String myfund = PATH + "/h/myfund";

    //添加数据判断某类型数据是否存在
    public static String isExist = PATH + "/h/isExist?";

    //返回默认国际区号编码 编码显示前面添加 '+' 号
    public static String getQuHao = PATH.replace("/app", "") + "/rest/areacode/def?";

    //查询所有区号编码 搜索又客户端完成，排序依照返回结果排序 编码显示前面添加 '+' 号
    public static String areacode = PATH.replace("/app", "") + "/rest/areacode?";

    //提交offer页面的验证,返回状态2:房屋不存在，3:自己的房屋，4:房屋正在被购买或已被购买，5:资金凭证不符合标准
    public static String aaverify = PATH + "/offerEnter/verify?";

    //获取当前登录用户映射极光别名
    public static String getJiGuang = PATH + "/jpush/954895/alias?";

    //查询全部州
    public static String getAllState = PATH.replace("/app", "") + "/rest/region/state?";

    //校验区域地址输入是否正确
    public static String valid = PATH.replace("/app", "") + "/rest/region/valid?";

    //系统推送消息列表
    public static String pages = PATH + "/radio/pages?";

    //默认配置
    public static String configure = PATH.replace("/app", "") + "/rest/configure?";

    // 消息数量
    public static String messageNum = PATH + "/c/msg/unread";


    // officeList消息数量

    public static String officeListMessage = PATH + "/c/msg/offerunread";
    // 登录传国家城市州
    public static String login_address = PATH + "/o/c/loginLog";

    //public static String login_address = "http://192.168.0.211:8080/app/o/c/loginLog";


    // 唤醒时候使用来判断是否被顶掉
    public static String validateLogin = PATH + "/c/msg/validateLogin";

    public static String facebooklogin = PATH + "/authorization/facebook";

    public static String googlelogin = PATH + "/authorization/google";


    //系统推送消息跳转
    public static String contract_intent = PATH + "/radio/pagesrio";


    //发布房屋条件跳转
    public static String postroomfilter = PATH + "/o/h/extattr";


    // 查阅查看记录
    public static String look_history_log = PATH + "/o/h/qhis";


    // 删除查看记录
    public static String delete_history_log = PATH + "/o/h/delhis";


    // 添加查看记录
    public static String add_history_log = PATH + "/o/h/addhis";

    // offer页面跳转信息
    public static String contract_message = PATH + "/c/msg/offer/offerid/tomsg";

// 极光推送跳转message详情页面

    public static String jpush_message = PATH + "/c/msg/bycode/message_code";

//   public  void main(String[] args) {
//      System.out.println(HttpUtils.httpGetString(null, "http://192.168.0.202:8080/oss/video/house/9ea1cffcca1ecd720a9633d0e52a54ab.mp4"));
//   }

    public static String about_ebuyhouse = PATH + "/o/about";

    public static String moneypic = PATH + "/c/msg/message_code/capitals";

    // 分享facebook
    public static String share = PATH + "/o/s/houseId";


    // 登录后立刻调用这个接口
    public static String login_device = PATH + "/c/authenticationDevice";

    // 升级
    public static String update = BASEPATH + "/o/version/userOrMech/scan/useragent";

    //搜索社交圈
    public static String search_social = PATH + "/cm/search";
    // 社交圈详情
    public static String social_detail = PATH + "/cm/details/communityId";

    // 申请加入社交圈
    public static String apply_social = PATH + "/cm/apply";


    // 获取好友列表
    public static String all_friend = PATH + "/fri/list";


    // 某社区圈好友列表
    public static String socail_friend = PATH + "/cm/customer/communityId";

    // 退出社交圈
    public static String exit_social_group = PATH + "/cm/out/communityId";

    //  某社交圈下所有兴趣圈列表
    public static String exit_social_interest_group = PATH + "/it/list/communityId";


    // 兴趣圈详情
    public static String interset_group_detail = PATH + "/it/details/integerId";


    // 某兴趣圈下所有的人
    public static String interset_group_peopele = PATH + "/it/customer/integerId";


    // 添加兴趣圈
    public static String add_interest = PATH + "/it/add";

    //  标签列表
    public static String label_list = PATH + "/it/labellist/communityId";


    //  添加标签
    public static String add_label = PATH + "/it/addlabel";


    //  删除标签
    public static String delete_label = PATH + "/it/delete/labelId";


    //  修改兴趣圈信息
    public static String revise_label = PATH + "/it/uinfo/integerId";

    //  普通群员退出兴趣圈
    public static String exit_interest = PATH + "/it/out/integerId";

    //  普通群员退出兴趣圈
    public static String dissmiss_interest = PATH + "/it/dismiss/integerId";

    //  转让兴趣圈
    public static String transfer_interest = PATH + "/it/turn/integerId";


    //  兴趣圈朋友列表
    public static String friend_list = PATH + "/fri/list";


    //  申请信息列表
    public static String friend_apply_list = PATH + "/fri/applylist";


    // 同意加入兴趣圈
    public static String agree_add_interest = PATH + "/it/agreeInvite/applyId";

    // 拒绝加入兴趣圈
    public static String refuse_add_interest = PATH + "/it/agreeInvite/applyId";


    // 同意 决绝 删除  好友申请
    public static String deal_apply_friend = PATH + "/fri/doapply/friendApplyId/status";

    // 查看好友信息
    public static String get_apply_friendmessage = PATH + "/fri/friend/friendId";


    // 将某用户踢出用户组
    public static String getout_interestgroup = PATH + "/it/off/integerId";


    // 提交好友申请
    public static String send_apply_friend = PATH + "/fri/apply";

    // 申请加入兴趣圈
    public static String send_apply_interest = PATH + "/it/apply";


    // 加入的社区圈列表
    public static String socail_list = PATH + "/cm/list";

    // 某个人下所有的兴趣圈列表
    public static String interest_list = PATH + "/it/myList";

    // 删除好友
    public static String delete_friend = PATH + "/fri/del";

    // 举报好友
    public static String report_friend = PATH + "/fri/report";


    // 邀请某人加入兴趣圈
    public static String invite_friend = PATH + "/it/invite/integerId";

    // 将某人踢出兴趣圈
    public static String goout_friend = PATH + "/it/off/integerId";

    // 是否可以催促加入社区圈申请
    public static String isurge = PATH + "/cm/isurge/communityId";
    // 催促审核
    public static String urge = PATH + "/cm/urge/communityId";

    // 某社区下且不在兴趣圈下的好友列表
    public static String not_circle_friend = PATH + "/it/friend/integerId";

    // 某社区下且不在兴趣圈下的好友列表
    public static String clear_message = PATH + "/radio/clemsg/message_type";


    // 某社区下且不在兴趣圈下的好友列表
    public static String clearone_message = PATH + "/radio/delmsg/message_type/message_id";

    public static String update_voice = PATH + "/file/up/chat/mp4";
    public static String get_room = PATH + "/c/msg/getroomid/";

}

