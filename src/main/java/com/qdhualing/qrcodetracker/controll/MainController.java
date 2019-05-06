package com.qdhualing.qrcodetracker.controll;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import com.qdhualing.qrcodetracker.bean.*;
import com.qdhualing.qrcodetracker.model.NotificationType;
import com.qdhualing.qrcodetracker.model.User;
import com.qdhualing.qrcodetracker.service.MainService;
import com.qdhualing.qrcodetracker.service.UserService;
import com.qdhualing.qrcodetracker.utils.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.tools.jar.Main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by Administrator on 2018/1/26.
 */
@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    private MainService mainService;

    @Autowired
    private UserService userService;

    //创建入库单表头信息
    @RequestMapping(value = "/createWL_RKD", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult createWL_RKD(String json) {
        CreateWLRKDParam rkdpParams = ParamsUtils.handleParams(json, CreateWLRKDParam.class);
        ActionResult<WLRKDResult> result = new ActionResult<WLRKDResult>();
        if (rkdpParams != null) {
            Date data = new Date();
            long indhL = data.getTime();
            String indh = String.valueOf(indhL) + RandomUtil.getRandomLong();
            rkdpParams.setInDh(indh);
            try {
                int c = mainService.getCreateRKDParamByInDh(rkdpParams.getInDh());
                if (c == 0) {
                    int a = mainService.createWL_RKD(rkdpParams);
                    int b = mainService.createWLWT_RKD(rkdpParams);
                    if (a <= 0 || b <= 0) {
                        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_LOGIC_ERROR, "入库单不正确");
                    } else if (a == 1 && b == 1) {
                        WLRKDResult rdk = new WLRKDResult();
                        rdk.setInDh(indh);
                        result.setResult(rdk);
                        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "入库单保存成功");
                    }
                }
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "入库单已存在");
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "入库单不正确");
    }

    //扫码录入
    @RequestMapping(value = "/createWLIn_M", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult createWLIn_M(String json) {
        WLINParam wlinParam = ParamsUtils.handleParams(json, WLINParam.class);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        wlinParam.setlLTime(df.format(System.currentTimeMillis()));
        wlinParam.setBz(1);
        ActionResult<DataResult> result = new ActionResult<DataResult>();
        if (wlinParam != null && wlinParam.getInDh() != null) {
            try {
//                int a = mainService.createWLIN_M(wlinParam);
                int ts = wlinParam.gettS();
                int a = mainService.updateWLIN_M(wlinParam);
                if (a <= 0) {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_LOGIC_ERROR, "扫描产品不存在");
                } else {
                    String errorTipMsg="入库记录表插入记录成功,已经录入"+a+"条";
                    if(a<ts){
                        errorTipMsg+="，二维码数量不足";
                    }
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, errorTipMsg);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "入库数据不正确");
    }

    /**
     * 用户注册
     * @param json
     * @return
     */
    @RequestMapping(value = "/registerUser", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult registerUser(String json){
        PersonParam personParam = ParamsUtils.handleParams(json, PersonParam.class);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sfId = personParam.getShenFen();
        if(!"ptyg".equals(sfId)){//根据员工身份，为其设置特殊权限（质检或审核）
            String allQxId = personParam.getCheckQXGroup();
            allQxId+=","+sfId;
            personParam.setCheckQXGroup(allQxId);
        }
        personParam.setRegTime(df.format(System.currentTimeMillis()));
        ActionResult<DataResult> result = new ActionResult<DataResult>();
        if(personParam!=null) {
            try {
                int a = mainService.registerUser(personParam);
                if(a>0)
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "员工注册成功");
                else
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "账号已存在");
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "员工数据不正确");
    }

    /**
     * 用户信息修改
     * @param json
     * @return
     */
    @RequestMapping(value = "/updateUserData", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult updateUserData(String json){
        PersonResult personResult = ParamsUtils.handleParams(json, PersonResult.class);
        String sfId = personResult.getShenFen();
        if(!"ptyg".equals(sfId)){//根据员工身份，为其设置特殊权限（质检或审核）
            String allQxId = personResult.getCheckQXGroup();
            allQxId+=","+sfId;
            personResult.setCheckQXGroup(allQxId);
        }
        ActionResult<DataResult> result = new ActionResult<DataResult>();
        if(personResult!=null){
            try {
                int a = mainService.updateUserData(personResult);
                if(a>0)
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "员工修改成功");
                else
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "员工账号已存在");
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "员工数据不正确");
    }

    /**
     * 删除员工
     * @param json
     * @return
     */
    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult deleteUser(String json){
        PersonParam personParam = ParamsUtils.handleParams(json, PersonParam.class);
        ActionResult<DataResult> result = new ActionResult<DataResult>();
        if(personParam!=null) {
            try {
                int a = mainService.deleteUser(personParam.getUserId());
                if(a>0)
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "员工删除成功");
                else
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "员工删除失败");
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "员工删除失败");
    }

    @RequestMapping(value = "/delWLIN_M", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult delWLWTAndWLINAndWLAndWLS(String json) {
        WLINParam wlinParam = ParamsUtils.handleParams(json, WLINParam.class);
        ActionResult<DataResult> result = new ActionResult<DataResult>();
        if (wlinParam != null && wlinParam.getInDh() != null) {
            try {
                List<WLINParam> list = mainService.getWLINParamListByInDh(wlinParam.getInDh());
                if (list.size() <= 0) {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "入库单号不正确");
                }
                for (WLINParam even : list) {
                    WLINParam wls = mainService.getWLSParamByQRCode(even.getqRCodeID());
                    wls.setpCZL(wls.getpCZL() - even.getpCZL());
                    mainService.updataWLINParamByQRCode(wls.getpCZL(), wls.getqRCodeID());
                }
            } catch (Exception e) {
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "入库数据不正确");
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 获取物料二级分类（即类别）
     */
    @RequestMapping(value = "/getPdtSort", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult getPdtSort() {
        ActionResult<PdtSortResult> result = new ActionResult<PdtSortResult>();
        try {
            PdtSortResult pdtSortResult = mainService.getPdtSort();
            if (pdtSortResult.getSortBeans() == null || pdtSortResult.getSortBeans().size() <= 0) {
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_MESSAGE_ERROR, "无分类数据");
            }
            result.setResult(pdtSortResult);
            return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "获取类别成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
        }
    }


    /**
     * @return
     * @author 马鹏昊
     * @desc 获取物料分类（含物料编码）
     */
    @RequestMapping(value = "/getHlSort", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult getHlSort() {
        ActionResult<HlSortResult> result = new ActionResult<HlSortResult>();
        try {
            HlSortResult hlSortResult = mainService.getHlSort();
            if (hlSortResult.getHlSortBeans() == null || hlSortResult.getHlSortBeans().size() <= 0) {
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_MESSAGE_ERROR, "无物料类别数据");
            }
            result.setResult(hlSortResult);
            return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "获取类别成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
        }
    }

    /**
     * 逄坤
     * 获取产品信息
     * */
    @RequestMapping(value = "/getHlProduct", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult getHlProduct(String json) {
        HlProductParam hlProductParam = ParamsUtils.handleParams(json, HlProductParam.class);
        ActionResult<HlProductResult> result = new ActionResult<HlProductResult>();
        try {
            HlProductResult hlProductResult = mainService.getHlProduct(hlProductParam.getSortID());
            if (hlProductResult.getHlProductBeans() == null || hlProductResult.getHlProductBeans().size() <= 0) {
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_MESSAGE_ERROR, "无产品数据");
            }
            result.setResult(hlProductResult);
            return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "获取产品数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
        }
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 生成物料出库单
     */
    @RequestMapping(value = "/createWL_CKD", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult createWL_CKD(String json) {
        CreateWLCKDParam ckdParam = ParamsUtils.handleParams(json, CreateWLCKDParam.class);
        ActionResult<WLCKDResult> result = new ActionResult<WLCKDResult>();
        if (ckdParam != null) {
            Date data = new Date();
            long time = data.getTime();
            String outDh = String.valueOf(time) + RandomUtil.getRandomLong();
            ckdParam.setOutDh(outDh);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ckdParam.setLhRq(sdf.format(data));
            try {
                int a = mainService.createWL_CKD(ckdParam);
                int b = mainService.createWLWT_CKD(ckdParam);
                if (a <= 0 || b <= 0) {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_LOGIC_ERROR, "出库单不正确");
                } else {
                    WLCKDResult ckd = new WLCKDResult();
                    ckd.setOutdh(outDh);
                    result.setResult(ckd);
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "出库单创建成功");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "出库单不正确");
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 物料出库界面显示数据获取
     */
    @RequestMapping(value = "/getWlOutShowData", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult getWlOutShowData(String json) {
        WLOutGetShowDataParam param = ParamsUtils.handleParams(json, WLOutGetShowDataParam.class);
        ActionResult<WLOutShowDataResult> result = new ActionResult<WLOutShowDataResult>();
        if (param != null) {
            try {
                WLOutShowDataResult showDataResult = mainService.getWLSData(param.getQrcodeId());
                if (showDataResult == null) {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_LOGIC_ERROR, "获取基本信息失败,请重新扫码");
                }
                else if(showDataResult.getShl()==0&&showDataResult.getDwzl()==0){
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_LOGIC_ERROR, "已经没有库存了,请重新扫码");
                }
                else{
                    result.setResult(showDataResult);
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "获取基本信息失败,请重新扫码");
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 物料出库
     */
    @RequestMapping(value = "/wlOut", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult wlOut(String json) {
        WLOutParam wlOutParam = ParamsUtils.handleParams(json, WLOutParam.class);
        ActionResult<ActionResult> result = new ActionResult<ActionResult>();
        if (wlOutParam != null) {
            Date data = new Date();
            long time = data.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            wlOutParam.setTime(sdf.format(data));
            try {
                CKDWLBean ckdwlBean = mainService.findWL_CKD(wlOutParam.getOutDh());
                if (ckdwlBean == null) {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_LOGIC_ERROR, "出库单不存在");
                }
                wlOutParam.setFlr(ckdwlBean.getFhR());
                wlOutParam.setLlr(ckdwlBean.getLhR());
                wlOutParam.setLlbm(ckdwlBean.getLhDw());
                WLSBean wlsBean = mainService.findWLS(wlOutParam.getQrCodeId());
                if (wlsBean == null)
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_LOGIC_ERROR, "没找到该物料，请先入库");
                wlOutParam.setProductName(wlsBean.getProductName());
                wlOutParam.setWlCode(wlsBean.getWLCode());
                wlOutParam.setDw(wlsBean.getDW());
                wlOutParam.setPczl(wlsBean.getPCZL());
                wlOutParam.setGg(wlsBean.getGG());
                wlOutParam.setSortId(wlsBean.getSortID());
                wlOutParam.setYlpc(wlsBean.getYLPC());
                wlOutParam.setChd(wlsBean.getCHD());
                int b = mainService.insertWLOUT(wlOutParam);
                if (b <= 0) {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_LOGIC_ERROR, "生成出库记录失败");
                } else {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "失败");
    }


    /**
     * @return
     * @author 马鹏昊
     * @desc 获取部门数据
     */
    @RequestMapping(value = "/getDepartmentData", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult getDepartmentData() {
        ActionResult<UserGroupResult> result = new ActionResult<UserGroupResult>();
        try {
            UserGroupResult userGroupResult = mainService.getUserGroupData();
            if (userGroupResult.getGroupBeanList() == null || userGroupResult.getGroupBeanList().size() <= 0) {
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_MESSAGE_ERROR, "无部门数据");
            }
            result.setResult(userGroupResult);
            return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "获取部门数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
        }
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 生成物料退库单
     */
    @RequestMapping(value = "/createWL_TKD", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult createWL_TKD(String json) {
        CreateWLTKDParam tkdParam = ParamsUtils.handleParams(json, CreateWLTKDParam.class);
        ActionResult<WLTKDResult> result = new ActionResult<WLTKDResult>();
        if (tkdParam != null) {
            Date data = new Date();
            long time = data.getTime();
            String backDh = String.valueOf(time) + RandomUtil.getRandomLong();
            tkdParam.setBackDh(backDh);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            tkdParam.setThRq(sdf.format(data));
            try {
                int a = mainService.createWL_TKD(tkdParam);
                if (a <= 0) {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_LOGIC_ERROR, "出错");
                } else {
                    WLTKDResult tkd = new WLTKDResult();
                    tkd.setBackDh(backDh);
                    result.setResult(tkd);
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "退库单创建成功");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "出错");
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 物料退库界面显示数据获取
     */
    @RequestMapping(value = "/getWlTKShowData", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult getWlTKShowData(String json) {
        WLTKGetShowDataParam param = ParamsUtils.handleParams(json, WLTKGetShowDataParam.class);
        ActionResult<WLTKShowDataResult> result = new ActionResult<WLTKShowDataResult>();
        if (param != null) {
            try {
                WLTKShowDataResult showDataResult = mainService.getWLTempSData(param.getQrcodeId());
                if (showDataResult == null) {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_LOGIC_ERROR, "获取基本信息失败,请重新扫码");
                }
                else {
                    result.setResult(showDataResult);
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "获取基本信息失败,请重新扫码");
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 物料退库
     */
    @RequestMapping(value = "/wlTk", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult wlTK(String json) {
        WLTKParam wlTKParam = ParamsUtils.handleParams(json, WLTKParam.class);
        ActionResult<ActionResult> result = new ActionResult<ActionResult>();
        if (wlTKParam != null) {
            Date data = new Date();
            long time = data.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            wlTKParam.setTime(sdf.format(data));
            try {
                //首先查找退库单信息
                TKDWLBean tkdwlBean = mainService.findWL_TKD(wlTKParam.getOutDh());
                if (tkdwlBean == null) {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_LOGIC_ERROR, "退库单不存在");
                }
                wlTKParam.setFlr(tkdwlBean.getThR());
                wlTKParam.setLlr(tkdwlBean.getShR());
                wlTKParam.setTkbm(tkdwlBean.getThDw());
                //查找临时库存表信息
                WLTempSBean wlTempSBean = mainService.getWLTempS(wlTKParam.getQrCodeId());
                if (wlTempSBean == null)
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_LOGIC_ERROR, "没找到该物料，请先出库");
                wlTKParam.setProductName(wlTempSBean.getProductName());
                wlTKParam.setWlCode(wlTempSBean.getWLCode());
                wlTKParam.setDw(wlTempSBean.getDW());
                //wlTKParam.setDwzl(wlTempSBean.getDWZL());
                wlTKParam.setGg(wlTempSBean.getGG());
                wlTKParam.setSortId(wlTempSBean.getSortID());
                wlTKParam.setYlpc(wlTempSBean.getYLPC());
                wlTKParam.setChd(wlTempSBean.getCHD());
                //生成退库记录
                int b = mainService.insertWLBk(wlTKParam);
                if (b <= 0) {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_LOGIC_ERROR, "生成退库记录失败");
                } else {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "失败");
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 获取物料投料显示数据
     */
    @RequestMapping(value = "/getWlTLShowData", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult getWlTLShowData(String json) {
        WLThrowGetShowDataParam param = ParamsUtils.handleParams(json, WLThrowGetShowDataParam.class);
        ActionResult<WLThrowShowDataResult> result = new ActionResult<WLThrowShowDataResult>();
        if (param != null) {
            try {
                WLTKShowDataResult showDataResult = mainService.getWLTempSData(param.getQrcodeId());
                if (showDataResult == null) {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_LOGIC_ERROR, "获取基本信息失败,请重新扫码");
                } else {
                    WLThrowShowDataResult wlThrowShowDataResult = new WLThrowShowDataResult();
                    wlThrowShowDataResult.setProductName(showDataResult.getProductName());
                    wlThrowShowDataResult.setChd(showDataResult.getChd());
                    wlThrowShowDataResult.setDw(showDataResult.getDw());
                    wlThrowShowDataResult.setDwzl(showDataResult.getDwzl());
                    wlThrowShowDataResult.setGg(showDataResult.getGg());
                    wlThrowShowDataResult.setShl(showDataResult.getShl());
                    wlThrowShowDataResult.setSortName(showDataResult.getSortName());
                    result.setResult(wlThrowShowDataResult);
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "获取基本信息失败,请重新扫码");
    }


    /**
     * @return
     * @author 马鹏昊
     * @desc 物料投料操作
     */
    @RequestMapping(value = "/wlThrow", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult wlThrow(String json) {
        WLThrowParam wlTLParam = ParamsUtils.handleParams(json, WLThrowParam.class);
        ActionResult<ActionResult> result = new ActionResult<ActionResult>();
        if (wlTLParam != null) {
            Date data = new Date();
            long time = data.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            wlTLParam.setTlTime(sdf.format(data));
            try {
                //查找临时库存表信息
                WLTempSBean wlTempSBean = mainService.getWLTempS(wlTLParam.getQrcodeId());
                if (wlTempSBean == null)
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_LOGIC_ERROR, "没找到该物料，请先出库");
                wlTLParam.setProductName(wlTempSBean.getProductName());
                wlTLParam.setWlCode(wlTempSBean.getWLCode());
                wlTLParam.setDw(wlTempSBean.getDW());
                wlTLParam.setPczl(wlTempSBean.getPCZL());
                wlTLParam.setGg(wlTempSBean.getGG());
                wlTLParam.setSortId(wlTempSBean.getSortID());
                wlTLParam.setYlpc(wlTempSBean.getYLPC());
                int b = mainService.getWLTLDataCount(wlTLParam.getQrcodeId());
                if (b <= 0) {
                    //生成物料投料记录
                    b = mainService.insertWLTl(wlTLParam);
                } else if (b > 1) {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_LOGIC_ERROR, "物料投料表数据不唯一");
                } else {
                    //更新物料投料记录
                    b = mainService.updateWLTl(wlTLParam);
                }
                if (b <= 0) {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_LOGIC_ERROR, "生成物料投料记录失败");
                } else {
                    //临时库存表中数据减去或者删除
                    if (wlTLParam.getTlShl() >= wlTempSBean.getSHL()&&wlTLParam.getDwzl() >= wlTempSBean.getDWZL()) {
                        b = mainService.deleteFromWLTempS(wlTLParam.getQrcodeId());
                    } else {
                        b = mainService.updateWLTempSByTl(wlTLParam);
                    }
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "失败");
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 获取车间数据
     */
    @RequestMapping(value = "/getCJ", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult getCJData() {
        ActionResult<CJResult> result = new ActionResult<CJResult>();
        try {
            CJResult cjResult = mainService.getCJData();
            if (cjResult.getCjBeans() == null || cjResult.getCjBeans().size() <= 0) {
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_MESSAGE_ERROR, "无车间数据");
            }
            result.setResult(cjResult);
            return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "获取车间数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
        }
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 获取工序数据
     */
    @RequestMapping(value = "/getGX", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult getGXData(String json) {
        GetGXParam gxParam = ParamsUtils.handleParams(json, GetGXParam.class);
        String cjGXIds = gxParam.getCjGXIds();
        String[] cjIdArray = cjGXIds.split(",");
        ActionResult<GXResult> result = new ActionResult<GXResult>();
        try {
            GXResult gxResult = mainService.getGXData(cjIdArray);
            if (gxResult.getGxBeans() == null || gxResult.getGxBeans().size() <= 0) {
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_MESSAGE_ERROR, "该车间无工序数据");
            }
            result.setResult(gxResult);
            return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "获取工序数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
        }
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 生成半成品/成品入库单
     */
    @RequestMapping(value = "/createBcpRkd", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult createBcpRkd(String json) {
        CreateBCPRKDParam rkdParam = ParamsUtils.handleParams(json, CreateBCPRKDParam.class);
        ActionResult<BCPRKDResult> result = new ActionResult<BCPRKDResult>();
        if (rkdParam != null) {
            Date data = new Date();
            long time = data.getTime();
            String inDh = String.valueOf(time) + RandomUtil.getRandomLong();
            rkdParam.setInDh(inDh);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            rkdParam.setShRq(sdf.format(data));
            try {
                int a = mainService.createBCP_RKD(rkdParam);
                if (a <= 0) {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_LOGIC_ERROR, "出错");
                } else {
                    BCPRKDResult rkd = new BCPRKDResult();
                    rkd.setIndh(inDh);
                    result.setResult(rkd);
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "半成品入库单创建成功");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "出错");
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 获取所需原料（物料和半成品投料表）
     */
    @RequestMapping(value = "/getTLYL", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult getTLYL(String json) {
        GetSXYLParam gxParam = ParamsUtils.handleParams(json, GetSXYLParam.class);
        int gxId = gxParam.getGxId();
        ActionResult<SXYLResult> result = new ActionResult<SXYLResult>();
        try {
            SXYLResult sxylResult = mainService.getSXYLData(gxId);
            if (sxylResult.getTlylList() == null || sxylResult.getTlylList().size() <= 0) {
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_MESSAGE_ERROR, "无数据,请先进行投料");
            }
            result.setResult(sxylResult);
            return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "获取工序数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
        }
    }

    /**
     * 逄坤
     * 获取所有权限
     * @param json
     * @return
     */
    @RequestMapping(value = "/getXZQX", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult getXZQX(String json) {
        PersonParam param = ParamsUtils.handleParams(json, PersonParam.class);
        ActionResult<Module2Result> result = new ActionResult<Module2Result>();
        try {
            Module2Result module2Result = mainService.getXZQXDataByShenFen(param);
            if (module2Result.getBeans() == null || module2Result.getBeans().size() <= 0) {
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_MESSAGE_ERROR, "无数据,请先添加权限");
            }
            result.setResult(module2Result);
            return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "获取权限数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
        }
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 半成品入库（临时库存，即车间）
     */
    @RequestMapping(value = "/bcpIn", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult bcpIn(String json) {
        BCPINParam bcpInParam = ParamsUtils.handleParams(json, BCPINParam.class);
        ActionResult<ActionResult> result = new ActionResult<ActionResult>();
        try {
//            int b = mainService.insertBCPIn(bcpInParam);
            float ts = bcpInParam.gettS();
            int b = mainService.updateBcpIn(bcpInParam);
            float ts1 = b;
            if (b <= 0) {
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_MESSAGE_ERROR, "插入BCPIn失败");
            }
            //查询临时库存表中是否有数据
            int count=b;
            int insertCount=0;
            String existQrCodeID="";
            String qRCodeID = bcpInParam.getQrCodeId();
            String typeNum = qRCodeID.substring(0, 9);
            int num = Integer.valueOf(qRCodeID.substring(9));
            num++;
            num-=ts1;
            for (int i=0;i<ts1;i++){
                bcpInParam.setQrCodeId(typeNum+num);//批量录入时，设置下一个二维码编号
                num++;
                b = mainService.findBCPTempS(bcpInParam.getQrCodeId());
                if (b <= 0) {
                    insertCount+=mainService.insertBCPTempS(bcpInParam);
                }
                else if (b >= 1){
                    existQrCodeID+=","+bcpInParam.getQrCodeId();
                }
            }
            if(insertCount==ts1){
                String errorTipMsg="半成品入库成功";
                if(count<ts)
                    errorTipMsg+=(",已经录入"+count+"条，还有"+((int)ts-count)+"条没有录入");
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, errorTipMsg);
            }
            else{
                existQrCodeID=existQrCodeID.substring(1);
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "BCPTempS表中二维码id"+existQrCodeID+"记录已存在，其他记录插入成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
        }
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 获取半成品投料显示数据
     */
    @RequestMapping(value = "/getBcpTLShowData", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult getBcpTLShowData(String json) {
        BcpThrowGetShowDataParam param = ParamsUtils.handleParams(json, BcpThrowGetShowDataParam.class);
        ActionResult<BcpThrowShowDataResult> result = new ActionResult<BcpThrowShowDataResult>();
        if (param != null) {
            try {
                BcpThrowShowDataResult showDataResult = mainService.getBcpTempSData(param.getQrcodeId());
                if (showDataResult == null) {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_LOGIC_ERROR, "没找到该半成品,请先入库");
                } else {
                    result.setResult(showDataResult);
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "获取基本信息失败,请重新扫码");
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 半成品投料操作
     */
    @RequestMapping(value = "/bcpThrow", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult bcpThrow(String json) {
        BcpThrowParam bcpTLParam = ParamsUtils.handleParams(json, BcpThrowParam.class);
        ActionResult<ActionResult> result = new ActionResult<ActionResult>();
        if (bcpTLParam != null) {
            Date data = new Date();
            long time = data.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            bcpTLParam.setTlTime(sdf.format(data));
            try {
                //查找临时库存表信息
                BCPTempSBean bcpTempSBean = mainService.getBcpTempS(bcpTLParam.getQrcodeId());
                if (bcpTempSBean == null)
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_LOGIC_ERROR, "没找到该半成品，请先入库");
                bcpTLParam.setProductName(bcpTempSBean.getProductName());
                bcpTLParam.setBcpCode(bcpTempSBean.getBcpCode());
                bcpTLParam.setDw(bcpTempSBean.getDw());
                bcpTLParam.setRkzl(bcpTempSBean.getRkzl());
                bcpTLParam.setGg(bcpTempSBean.getGg());
                bcpTLParam.setSortId(bcpTempSBean.getSortID());
                bcpTLParam.setYlpc(bcpTempSBean.getYlpc());
                int b = mainService.getBcpTLDataCount(bcpTLParam.getQrcodeId());
                if (b <= 0) {
                    //生成半成品投料记录
                    b = mainService.insertBcpTl(bcpTLParam);
                } else if (b > 1) {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_LOGIC_ERROR, "半成品投料表数据不唯一");
                } else {
                    //更新半成品投料记录
                    b = mainService.updateBcpTl(bcpTLParam);
                }
                if (b <= 0) {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_LOGIC_ERROR, "生成半成品投料记录失败");
                } else {
                    //临时库存表中数据减去或者删除
                    if (bcpTLParam.getTlShl() >= bcpTempSBean.getShl()&&bcpTLParam.getDwzl() >= bcpTempSBean.getDwzl()) {
                        b = mainService.deleteFromBcpTempS(bcpTLParam.getQrcodeId());
                    } else {
                        b = mainService.updateBcpTempSByTl(bcpTLParam);
                    }
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "失败");
    }


    /**
     * @return
     * @author 马鹏昊
     * @desc 生成半成品退库单
     */
    @RequestMapping(value = "/createBCP_TKD", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult createBCP_TKD(String json) {
        CreateBCPTKDParam param = ParamsUtils.handleParams(json, CreateBCPTKDParam.class);
        ActionResult<BCPTKDResult> result = new ActionResult<BCPTKDResult>();
        if (param != null) {
            Date data = new Date();
            long time = data.getTime();
            String dh = String.valueOf(time) + RandomUtil.getRandomLong();
            param.setBackDh(dh);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            param.setThRq(sdf.format(data));
            try {
                int a = mainService.createBCP_TKD(param);
                if (a <= 0) {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_LOGIC_ERROR, "出错");
                } else {
                    BCPTKDResult tkd = new BCPTKDResult();
                    tkd.setBackDh(dh);
                    result.setResult(tkd);
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "半成品入库（退库）单创建成功");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "出错");
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 获取半成品退库显示数据
     */
    @RequestMapping(value = "/getBcpTkShowData", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult getBcpTkShowData(String json) {
        BCPTKGetShowDataParam param = ParamsUtils.handleParams(json, BCPTKGetShowDataParam.class);
        ActionResult<BCPTKShowDataResult> result = new ActionResult<BCPTKShowDataResult>();
        if (param != null) {
            try {
                BCPTKShowDataResult showDataResult = mainService.getBCPTKShowData(param.getQrcodeId());
                if (showDataResult == null) {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_LOGIC_ERROR, "获取基本信息失败,请重新扫码");
                } else {
                    result.setResult(showDataResult);
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "获取基本信息失败,请重新扫码");
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 半成品退库
     */
    @RequestMapping(value = "/bcpTK", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult bcpTK(String json) {
        BCPTKParam param = ParamsUtils.handleParams(json, BCPTKParam.class);
        ActionResult<ActionResult> result = new ActionResult<ActionResult>();
        if (param != null) {
            Date data = new Date();
            long time = data.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            param.setTkTime(sdf.format(data));
            try {
                //首先查找退库单信息
                TKDBCPBean tkdbcpBean = mainService.getTKDBCPBean(param.getBackDh());
                if (tkdbcpBean == null) {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_LOGIC_ERROR, "退库单不存在");
                }
                param.setThr(tkdbcpBean.getThr());
                param.setShrr(tkdbcpBean.getShr());
                param.setThDw(tkdbcpBean.getThDw());
                //查找临时库存表信息
                BCPTempSBean bcpTempSBean = mainService.getBcpTempS(param.getQrCodeId());
                if (bcpTempSBean == null)
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_LOGIC_ERROR, "没找到该物料，请先出库");
                param.setProductName(bcpTempSBean.getProductName());
                param.setBcpCode(bcpTempSBean.getBcpCode());
                param.setRkzl(bcpTempSBean.getRkzl());
                param.setSortID(bcpTempSBean.getSortID());
                param.setYlpc(bcpTempSBean.getYlpc());
                param.setScpc(bcpTempSBean.getScpc());
                param.setScTime(bcpTempSBean.getScTime());
                param.setKsTime(bcpTempSBean.getKsTime());
                param.setWcTime(bcpTempSBean.getWcTime());
                param.setZjy(bcpTempSBean.getZjy());
                param.setJyzt(bcpTempSBean.getJyzt());
                param.setCheJian(bcpTempSBean.getCheJian());
                param.setGx(bcpTempSBean.getGx());
                param.setYl1(bcpTempSBean.getYl1());
                param.setYl2(bcpTempSBean.getYl2());
                param.setYl3(bcpTempSBean.getYl3());
                param.setYl4(bcpTempSBean.getYl4());
                param.setYl5(bcpTempSBean.getYl5());
                param.setYl6(bcpTempSBean.getYl6());
                param.setYl7(bcpTempSBean.getYl7());
                param.setYl8(bcpTempSBean.getYl8());
                param.setYl9(bcpTempSBean.getYl9());
                param.setYl10(bcpTempSBean.getYl10());
                //生成退库记录
                int b = mainService.insertBCPBk(param);
                if (b <= 0) {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_LOGIC_ERROR, "生成退库记录失败");
                } else {
                    //更新仓库半成品库存表数量（退库的数量加上）
                    b = mainService.getBCPSCount(param.getQrCodeId());
                    if (b <= 0) {
                        b = mainService.insertBCPS(param);
                        if (b <= 0) {
                            return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_LOGIC_ERROR, "插入半成品库存表数据失败");
                        }
                    } else if (b > 1) {
                        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_LOGIC_ERROR, "半成品库存表记录不唯一");
                    } else {
                        b = mainService.updateBCPSByTk(param);
                        if (b <= 0) {
                            return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_LOGIC_ERROR, "修改半成品库存表数据失败");
                        }
                    }
                    //临时库存表中数据减去或者删除
                    if (param.getShl() >= bcpTempSBean.getShl()&&param.getDwzl() >= bcpTempSBean.getDwzl()) {
                        b = mainService.deleteFromBcpTempS(param.getQrCodeId());
                        if (b <= 0) {
                            return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_LOGIC_ERROR, "删除半成品临时库存表数据失败");
                        }
                    } else {
                        b = mainService.updateBCPTempSByBCPTk(param);
                        if (b <= 0) {
                            return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_LOGIC_ERROR, "修改半成品临时库存表数据失败");
                        }
                    }
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "失败");
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 生成半成品/成品出库单
     */
    @RequestMapping(value = "/createBCP_CKD", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult createBCP_CKD(String json) {
        CreateBCPCKDParam param = ParamsUtils.handleParams(json, CreateBCPCKDParam.class);
        ActionResult<BCPCKDResult> result = new ActionResult<BCPCKDResult>();
        if (param != null) {
            Date data = new Date();
            long time = data.getTime();
            String dh = String.valueOf(time) + RandomUtil.getRandomLong();
            param.setOutDh(dh);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            param.setLhRq(sdf.format(data));
            try {
                int a = mainService.createBCP_CKD(param);
                if (a <= 0) {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_LOGIC_ERROR, "出错");
                } else {
                    BCPCKDResult ckd = new BCPCKDResult();
                    ckd.setOutDh(dh);
                    result.setResult(ckd);
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "半成品退库单创建成功");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "出错");
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 获取类别数据
     */
    @RequestMapping(value = "/getLeiBie", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult getLeiBie(String json) {
        ActionResult<PdtSortResult> result = new ActionResult<PdtSortResult>();
        try {
            PdtSortResult lbResult = mainService.getPdtSort();
            if (lbResult.getSortBeans() == null || lbResult.getSortBeans().size() <= 0) {
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_MESSAGE_ERROR, "无类别数据");
            }
            result.setResult(lbResult);
            return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "获取类别数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
        }
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 成品大包装入库
     */
    @RequestMapping(value = "/bigCpIn", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult bigCpIn(String json) {
        BigCPINParam inParam = ParamsUtils.handleParams(json, BigCPINParam.class);
        ActionResult<ActionResult> result = new ActionResult<ActionResult>();
        try {
            //查询大包装库存表中是否有数据
            int ts = inParam.gettS();
            int b = mainService.updateCPIn2ByParam(inParam);
            if(b<=0){
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "扫描产品不存在");
            }
            else{
                String errorTipMsg="成品大包装入库成功,已经录入"+b+"条";
                if(b<ts){
                    errorTipMsg+="，二维码数量不足";
                }
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, errorTipMsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
        }
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 获取大包装数据
     */
    @RequestMapping(value = "/getBigCpData", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult getBigCpData(String json) {
        ActionResult<BigCpResult> result = new ActionResult<BigCpResult>();
        try {
            BigCpResult bigCpResult = mainService.getBigCpData();
            if (bigCpResult.getBeans() == null || bigCpResult.getBeans().size() <= 0) {
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_MESSAGE_ERROR, "无大包装数据");
            }
            result.setResult(bigCpResult);
            return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "获取大包装数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
        }
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 成品小包装入库
     */
    @RequestMapping(value = "/smallCpIn", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult smallCpIn(String json) {
        SmallCPINParam inParam = ParamsUtils.handleParams(json, SmallCPINParam.class);
        ActionResult<ActionResult> result = new ActionResult<ActionResult>();
        try {
            //查询小包装库存表中是否有数据
            int b = mainService.findCPS(inParam.getQrCodeId());
            if (b > 0) {
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_MESSAGE_ERROR, "该成品早已入库，换一个吧");
            }
//            b = mainService.insertCPIn(inParam);
            float shl = inParam.getShl();
            b = mainService.updateCPIn(inParam);
            //int ts1=b;
            if (b <= 0) {
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_MESSAGE_ERROR, "录入CPIn失败");
            }
            else{
                //根据小包装二维码还原相关大包装入库单状态
                mainService.updateBcpRkdStatusByQRCodeID(inParam.getQrCodeId());
            }

            /*
            //根据录入数量把最后一个二维码编号还原为第一个二维码编号
            String qRCodeID = inParam.getQrCodeId();
            String typeNum = qRCodeID.substring(0, 9);
            int num = Integer.valueOf(qRCodeID.substring(9));
            num++;
            num-=ts1;
            inParam.setQrCodeId(typeNum+num);
            //

            String startQrCodeId = inParam.getQrCodeId();
            Long nextQrCodeId = Long.parseLong(startQrCodeId);
            //int size = (int) inParam.getShl();
            int size = b;
            BigCpBean bigCpBean = null;
            int nowIndex = 0;
            if (!TextUtils.isEmpty(inParam.getcPS2QRCode())) {
                bigCpBean = mainService.getCPS2(inParam.getcPS2QRCode());
                nowIndex = bigCpBean.getNowNum();
            }
            for (int i = 0; i < size; i++) {
                //插入小包装库存表（车间）
                b = mainService.insertCPS(inParam);
                if (b <= 0) {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_MESSAGE_ERROR, "录入CPS失败");
                }
                //如果是需要关联大包装的小包装则需要以下操作
                if (!TextUtils.isEmpty(inParam.getcPS2QRCode())) {
                    bigCpBean = ProjectUtil.getUpdateCPS2Data(bigCpBean, nowIndex + 1, nextQrCodeId);
                    b = mainService.updateCPS2(bigCpBean);
                    b = mainService.updateCPIn2(bigCpBean);
                }
                nextQrCodeId += 1;
                nowIndex++;
                inParam.setQrCodeId(String.valueOf(nextQrCodeId));
            }
            */

            String errorTipMsg="成品小包装入库成功,已经录入"+b+"条";
            if(b<shl)
                errorTipMsg+=("，二维码数量不足");
            return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, errorTipMsg);
        } catch (Exception e) {
            e.printStackTrace();
            return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
        }
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 大包装出库
     */
    @RequestMapping(value = "/bigCpOut", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult bigCpOut(String json) {
        BigCpOutParam param = ParamsUtils.handleParams(json, BigCpOutParam.class);
        ActionResult<ActionResult> result = new ActionResult<ActionResult>();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        param.setFhDate(sdf.format(date));
        try {
            BigCpBean bigCpBean = mainService.getCPS2(param.getQrCodeId());
            if (bigCpBean==null) {
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_MESSAGE_ERROR, "无此大包装");
            }
            param.setCpName(bigCpBean.getcPName());
            param.setCpCode(bigCpBean.getcPCode());
            param.setDwzl(bigCpBean.getDwzl());
            param.setScpc(bigCpBean.getScpc());
            param.setYlpc(bigCpBean.getYlpc());
            param.setSortId(bigCpBean.getSortID());
            int b = mainService.insertCPOut(param);
            if (b <= 0) {
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_MESSAGE_ERROR, "出库记录生成失败");
            }
            /*
            b = mainService.deleteCPS2ByQrId(param.getQrCodeId());
            if (b <= 0) {
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_MESSAGE_ERROR, "删除大包装记录失败");
            }
            b = mainService.deleteCPSByCps2QrId(param.getQrCodeId());
//            if (b <= 0) {
//                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_MESSAGE_ERROR, "删除大包装关联的小包装记录失败");
//            }
*/
            return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "大包装出库成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
        }
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 获取大包装出库显示数据
     */
    @RequestMapping(value = "/getBigCpOutData", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult getBigCpOutData(String json) {
        BigCpOutGetDataParam param = ParamsUtils.handleParams(json, BigCpOutGetDataParam.class);
        ActionResult<BigCpOutGetDataResult> result = new ActionResult<BigCpOutGetDataResult>();
        if (param != null) {
            try {
                BigCpOutGetDataResult showDataResult = mainService.getCP2ShowData(param.getQrCodeId());
                if (showDataResult == null) {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_LOGIC_ERROR, "获取基本信息失败,请重新扫码");
                } else {
                    result.setResult(showDataResult);
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "获取基本信息失败,请重新扫码");
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 小包装出库
     */
    @RequestMapping(value = "/smallCpOut", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult smallCpOut(String json) {
        SmallCpOutParam param = ParamsUtils.handleParams(json, SmallCpOutParam.class);
        ActionResult<ActionResult> result = new ActionResult<ActionResult>();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        param.setFhDate(sdf.format(date));
        try {
            SmallCpBean bigCpBean = mainService.getCPS(param.getQrCodeId());
            param.setCpName(bigCpBean.getCpName());
            param.setCpCode(bigCpBean.getCpCode());
            param.setDwzl(bigCpBean.getDwzl());
            param.setScpc(bigCpBean.getScpc());
            param.setYlpc(bigCpBean.getYlpc());
            param.setSortId(bigCpBean.getSortID());
            int b = mainService.insertCPOutBySmallParam(param);
            if (b <= 0) {
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_MESSAGE_ERROR, "出库记录生成失败");
            }
            /*
            b = mainService.deleteCPSByQrId(param.getQrCodeId());
            if (b <= 0) {
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_MESSAGE_ERROR, "删除小包装记录失败");
            }
            */
            return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "小包装出库成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
        }
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 获取小包装出库显示数据
     */
    @RequestMapping(value = "/getSmallCpOutData", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult getSmallCpOutData(String json) {
        SmallCpOutGetDataParam param = ParamsUtils.handleParams(json, SmallCpOutGetDataParam.class);
        ActionResult<SmallCpOutGetDataResult> result = new ActionResult<SmallCpOutGetDataResult>();
        if (param != null) {
            try {
                SmallCpOutGetDataResult showDataResult = mainService.getSmallCpOutData(param.getQrCodeId());
                if (showDataResult == null) {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_LOGIC_ERROR, "获取基本信息失败,请重新扫码");
                } else {
                    result.setResult(showDataResult);
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "获取基本信息失败,请重新扫码");
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 物料追溯
     */
    @RequestMapping(value = "/wlTrack", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult wlTrack(String json) {
        WlTrackParam param = ParamsUtils.handleParams(json, WlTrackParam.class);
        ActionResult<WlTrackResult> result = new ActionResult<WlTrackResult>();
        if (param != null) {
            try {
                WlTrackResult dataResult = mainService.getWlInData(param.getQrCodeId());
                if (dataResult == null) {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_LOGIC_ERROR, "查无此料");
                } else {
                    result.setResult(dataResult);
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "获取基本信息失败,请重新扫码");
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 半成品追溯
     */
    @RequestMapping(value = "/bcpTrack", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult bcpTrack(String json) {
        WlTrackParam param = ParamsUtils.handleParams(json, WlTrackParam.class);
        ActionResult<BcpTrackResult> result = new ActionResult<BcpTrackResult>();
        if (param != null) {
            try {
                BcpThrowShowDataResult dataResult = mainService.getBcpTempSData(param.getQrCodeId());
                if (dataResult == null) {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_LOGIC_ERROR, "查无此料");
                } else {
                    BcpTrackResult singleData = new BcpTrackResult();
                    singleData.setProductName(dataResult.getProductName());
                    //singleData.setBcpCode(dataResult.getBcpCode());
                    singleData.setCheJian(dataResult.getCheJian());
                    singleData.setCzy(dataResult.getCzy());
                    singleData.setDw(dataResult.getDw());
                    singleData.setDwzl(dataResult.getDwzl());
                    singleData.setGg(dataResult.getGg());
                    singleData.setGx(dataResult.getGx());
                    singleData.setJyzt(dataResult.getJyzt());
                    singleData.setScpc(dataResult.getScpc());
                    singleData.setScTime(dataResult.getScTime());
                    String sortName = mainService.getHlSortBySortId(dataResult.getSortID() + "");
                    singleData.setSortName(sortName);
                    singleData.setYlpc(dataResult.getYlpc());
                    singleData.setZjy(dataResult.getZjy());
                    List<String> ylList = new ArrayList<String>();
                    if (!TextUtils.isEmpty(dataResult.getYl1()))
                        ylList.add(dataResult.getYl1());
                    if (!TextUtils.isEmpty(dataResult.getYl2()))
                        ylList.add(dataResult.getYl2());
                    if (!TextUtils.isEmpty(dataResult.getYl3()))
                        ylList.add(dataResult.getYl3());
                    if (!TextUtils.isEmpty(dataResult.getYl4()))
                        ylList.add(dataResult.getYl4());
                    if (!TextUtils.isEmpty(dataResult.getYl5()))
                        ylList.add(dataResult.getYl5());
                    if (!TextUtils.isEmpty(dataResult.getYl6()))
                        ylList.add(dataResult.getYl6());
                    if (!TextUtils.isEmpty(dataResult.getYl7()))
                        ylList.add(dataResult.getYl7());
                    if (!TextUtils.isEmpty(dataResult.getYl8()))
                        ylList.add(dataResult.getYl8());
                    if (!TextUtils.isEmpty(dataResult.getYl9()))
                        ylList.add(dataResult.getYl9());
                    if (!TextUtils.isEmpty(dataResult.getYl10()))
                        ylList.add(dataResult.getYl10());

                    if(ylList.size()>0) {
                        List<ComponentBean> wlComponentBeans = mainService.getComponentBeansFromWl(ylList);
                        List<ComponentBean> bcpComponentBeans = mainService.getComponentBeansFromBcp(ylList);
                        List<ComponentBean> allComponentBeans = new ArrayList<ComponentBean>();
                        allComponentBeans.clear();
                        allComponentBeans.addAll(wlComponentBeans);
                        allComponentBeans.addAll(bcpComponentBeans);
                        singleData.setComponentBeans(allComponentBeans);
                    }
                    result.setResult(singleData);
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "获取基本信息失败,请重新扫码");
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 成品小包装追溯
     */
    @RequestMapping(value = "/smallCpTrack", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult smallCpTrack(String json) {
        WlTrackParam param = ParamsUtils.handleParams(json, WlTrackParam.class);
        ActionResult<SmallCpTrackResult> result = new ActionResult<SmallCpTrackResult>();
        if (param != null) {
            try {
                List<CPINParam> dataResults = mainService.getSmallCpInData(param.getQrCodeId());
                if (dataResults == null || dataResults.size() < 1) {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_LOGIC_ERROR, "查无此料");
                } else {
                    SmallCpTrackResult singleData = new SmallCpTrackResult();
                    CPINParam bb = dataResults.get(0);
                    singleData.setCpName(bb.getCpName());
                    singleData.setCpCode(bb.getCpCode());
                    singleData.setCzy(bb.getCzy());
                    singleData.setDw(bb.getDw());
                    singleData.setDwzl(bb.getDwzl());
                    singleData.setGg(bb.getGg());
                    singleData.setJyzt(bb.getJyzt());
                    singleData.setScpc(bb.getScpc());
                    singleData.setScTime(bb.getScTime());
                    String sortName = mainService.getHlSortBySortId(bb.getSortID() + "");
                    singleData.setSortName(sortName);
                    singleData.setYlpc(bb.getYlpc());
                    singleData.setZjy(bb.getZjy());
                    List<String> ylList = new ArrayList<String>();
                    if (!TextUtils.isEmpty(bb.getYl1()))
                        ylList.add(bb.getYl1());
                    if (!TextUtils.isEmpty(bb.getYl2()))
                        ylList.add(bb.getYl2());
                    if (!TextUtils.isEmpty(bb.getYl3()))
                        ylList.add(bb.getYl3());
                    if (!TextUtils.isEmpty(bb.getYl4()))
                        ylList.add(bb.getYl4());
                    if (!TextUtils.isEmpty(bb.getYl5()))
                        ylList.add(bb.getYl5());
                    if (!TextUtils.isEmpty(bb.getYl6()))
                        ylList.add(bb.getYl6());
                    if (!TextUtils.isEmpty(bb.getYl7()))
                        ylList.add(bb.getYl7());
                    if (!TextUtils.isEmpty(bb.getYl8()))
                        ylList.add(bb.getYl8());
                    if (!TextUtils.isEmpty(bb.getYl9()))
                        ylList.add(bb.getYl9());
                    if (!TextUtils.isEmpty(bb.getYl10()))
                        ylList.add(bb.getYl10());

                    if(ylList.size()>0) {
                        List<ComponentBean> wlComponentBeans = mainService.getComponentBeansFromWl(ylList);
                        List<ComponentBean> bcpComponentBeans = mainService.getComponentBeansFromBcp(ylList);
                        List<ComponentBean> allComponentBeans = new ArrayList<ComponentBean>();
                        allComponentBeans.clear();
                        allComponentBeans.addAll(wlComponentBeans);
                        allComponentBeans.addAll(bcpComponentBeans);
                        singleData.setComponentBeans(allComponentBeans);
                    }
                    result.setResult(singleData);
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "查无此料");
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 成品大包装追溯
     */
    @RequestMapping(value = "/bigCpTrack", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult bigCpTrack(String json) {
        WlTrackParam param = ParamsUtils.handleParams(json, WlTrackParam.class);
        ActionResult<BigCpTrackResult> result = new ActionResult<BigCpTrackResult>();
        if (param != null) {
            try {
                List<BigCpBean> dataResults = mainService.getBigCpIn2(param.getQrCodeId());
                if (dataResults == null || dataResults.size() < 1) {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_LOGIC_ERROR, "查无此料");
                } else {
                    BigCpTrackResult singleData = new BigCpTrackResult();
                    BigCpBean bb = dataResults.get(0);
                    singleData.setCpName(bb.getcPName());
                    singleData.setCpCode(bb.getcPCode());
                    singleData.setCzy(bb.getCzy());
                    singleData.setDw(bb.getDw());
                    singleData.setDwzl(bb.getDwzl());
                    singleData.setGg(bb.getGg());
                    singleData.setJyzt(bb.getJyzt());
                    singleData.setScpc(bb.getScpc());
                    singleData.setScTime(bb.getScTime());
                    singleData.setSmlPk1(bb.getSmlPk1());
                    String sortName = mainService.getHlSortBySortId(bb.getSortID() + "");
                    singleData.setSortName(sortName);
                    singleData.setYlpc(bb.getYlpc());
                    singleData.setZjy(bb.getZjy());
                    result.setResult(singleData);
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "获取基本信息失败,请重新扫码");
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 发送给相关人员审核通知
     */
    @RequestMapping(value = "/sendNotification", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult sendNotification(String json) {
        NotificationParam param = ParamsUtils.handleParams(json, NotificationParam.class);
        ActionResult<ActionResult> result = new ActionResult<ActionResult>();
        try {
            //要发送的人
            String desPerson = null;
            //推送消息
            String alertMsg = null;
            String alertMsgStr1 = null;
            String alertMsgStr2 = null;
            if(param.getPersonFlag()==NotificationParam.BZ
                    ||param.getPersonFlag()==NotificationParam.FZR
                    ||param.getPersonFlag()==NotificationParam.FLFZR
                    ||param.getPersonFlag()==NotificationParam.LLFZR
                    ||param.getPersonFlag()==NotificationParam.TLFZR
                    ||param.getPersonFlag()==NotificationParam.SLFZR
                    ||param.getPersonFlag()==NotificationParam.KG)
                alertMsgStr2="审核";
            else if(param.getPersonFlag()==NotificationParam.ZJY||param.getPersonFlag()==NotificationParam.ZJLD)
                alertMsgStr2="质检";
            switch (param.getStyle()) {
                case NotificationType.WL_RKD:
                    desPerson = mainService.getPersonFromWlRkd(param);
                    alertMsgStr1="物料入库单";
                    break;
                case NotificationType.WL_CKD:
                    desPerson = mainService.getPersonFromWlCkd(param);
                    alertMsgStr1="物料出库单";
                    break;
                case NotificationType.WL_TKD:
                    desPerson = mainService.getPersonFromWlTkd(param);
                    alertMsgStr1="物料退库单";
                    break;
                case NotificationType.BCP_RKD:
                    desPerson = mainService.getPersonFromBcpRkd(param);
                    alertMsgStr1="半成品录入单";
                    break;
                case NotificationType.BCP_TKD:
                    desPerson = mainService.getPersonFromBcpTkd(param);
                    alertMsgStr1="半成品入库（退库）单";
                    break;
                case NotificationType.CP_RKD:
                    desPerson = mainService.getPersonFromBcpRkd(param);
                    alertMsgStr1="成品入库单";
                    break;
                case NotificationType.CP_CKD:
                    desPerson = mainService.getFzrFromBcpCkd(param.getDh());
                    alertMsgStr1="成品出库单";
                    break;
            }
            try {
                alertMsg = "您有一条"+alertMsgStr1+"需要"+alertMsgStr2;
                JPushUtils.sendNotification(alertMsg, desPerson);
            } catch (APIConnectionException e) {
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "推送失败");
            } catch (APIRequestException e) {
                // Should review the error, and fix the request
//                LOG.error("Should review the error, and fix the request", e);
//                LOG.info("HTTP Status: " + e.getStatus());
//                LOG.info("Error Code: " + e.getErrorCode());
//                LOG.info("Error Message: " + e.getErrorMessage());
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "推送失败");
            }
            return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "已通知仓库管理员审核");
        } catch (Exception e) {
            e.printStackTrace();
            return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
        }
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 获取待审核库单数据
     */
    @RequestMapping(value = "/getNonCheckData", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult getNonCheckData(String json) {
        MainParams param = ParamsUtils.handleParams(json, MainParams.class);
        ActionResult<NonCheckResult> result = new ActionResult<NonCheckResult>();
        if (param != null) {
            try {
                NonCheckResult dataResult = new NonCheckResult();
                List<NonCheckBean> allBeans = new ArrayList<NonCheckBean>();

                Integer kgID=null;
                Integer bzID=null;
                Integer fzrID=null;
                Integer zjyID=null;
                Integer zjldID=null;
                Integer userId = Integer.valueOf(param.getUserId());
                if(param.getCheckQXFlag()==MainParams.KG)
                    kgID=userId;
                else if(param.getCheckQXFlag()==MainParams.BZ)
                    bzID=userId;
                else if(param.getCheckQXFlag()==MainParams.FZR)
                    fzrID=userId;
                else if(param.getCheckQXFlag()==MainParams.ZJY)
                    zjyID=userId;
                else if(param.getCheckQXFlag()==MainParams.ZJLD)
                    zjldID=userId;

                List<WlRkdBean> wlRkNonCheckData = mainService.getWlRkNonCheckData(fzrID,zjyID,zjldID);
                for (int i = 0; i < wlRkNonCheckData.size(); i++) {
                    NonCheckBean bean = new NonCheckBean();
                    WlRkdBean single = wlRkNonCheckData.get(i);
                    bean.setDh(single.getInDh());
                    bean.setName("物料入库单");
                    bean.setTime(single.getShrq());
                    bean.setState(single.getCheckState());
                    allBeans.add(bean);
                }
                List<WlCkdBean> wlCkNonCheckData = null;
                if(kgID!=null||bzID!=null||fzrID!=null) {
                    wlCkNonCheckData = mainService.getWlCkNonCheckData(kgID, bzID, fzrID);
                    for (int i = 0; i < wlCkNonCheckData.size(); i++) {
                        NonCheckBean bean = new NonCheckBean();
                        WlCkdBean single = wlCkNonCheckData.get(i);
                        bean.setDh(single.getOutDh());
                        bean.setName("物料出库单");
                        bean.setTime(single.getLhRq());
                        bean.setFlfzrID(single.getFlfzrID());
                        bean.setLlfzrID(single.getLlfzrID());
                        bean.setState(single.getCheckState());
                        allBeans.add(bean);
                    }
                }
                List<WlTkdBean> wlTkNonCheckData = mainService.getWlTkNonCheckData(bzID,kgID,fzrID);
                for (int i = 0; i < wlTkNonCheckData.size(); i++) {
                    NonCheckBean bean = new NonCheckBean();
                    WlTkdBean single = wlTkNonCheckData.get(i);
                    bean.setDh(single.getBackDh());
                    bean.setName("物料退库单");
                    bean.setTime(single.getThRq());
                    bean.setTlfzrID(single.getTlfzrID());
                    bean.setSlfzrID(single.getSlfzrID());
                    bean.setState(single.getCheckState());
                    allBeans.add(bean);
                }
                List<BcpRkdBean> bcpRkNonCheckData = mainService.getBcpRkNonCheckData(bzID,fzrID,zjyID,zjldID);
                for (int i = 0; i < bcpRkNonCheckData.size(); i++) {
                    NonCheckBean bean = new NonCheckBean();
                    BcpRkdBean single = bcpRkNonCheckData.get(i);
                    bean.setDh(single.getInDh());
                    //bean.setName("半成品/成品入库单");
                    bean.setName("半成品录入单");
                    bean.setTime(single.getShrq());
                    bean.setState(single.getCheckState());
                    allBeans.add(bean);
                }
                List<BcpRkdBean> cpRkNonCheckData = mainService.getCpRkNonCheckData(bzID,fzrID,zjyID,zjldID,kgID);
                for (int i = 0; i < cpRkNonCheckData.size(); i++) {
                    NonCheckBean bean = new NonCheckBean();
                    BcpRkdBean single = cpRkNonCheckData.get(i);
                    bean.setDh(single.getInDh());
                    bean.setName("成品入库单");
                    bean.setTime(single.getShrq());
                    bean.setFlfzrID(single.getFlfzrID());
                    bean.setLlfzrID(single.getLlfzrID());
                    bean.setState(single.getCheckState());
                    allBeans.add(bean);
                }
                List<BcpCkdBean> bcpCkNonCheckData = null;
                if(kgID!=null||fzrID!=null) {
                    bcpCkNonCheckData = mainService.getBcpCkNonCheckData(kgID,fzrID);
                    for (int i = 0; i < bcpCkNonCheckData.size(); i++) {
                        NonCheckBean bean = new NonCheckBean();
                        BcpCkdBean single = bcpCkNonCheckData.get(i);
                        bean.setDh(single.getOutDh());
                        bean.setName("成品出库单");
                        bean.setTime(single.getLhRq());
                        bean.setState(single.getCheckState());
                        allBeans.add(bean);
                    }
                }
                List<BcpTkdBean> bcpTkNonCheckData = mainService.getBcpTkNonCheckData(bzID,zjyID,zjldID,kgID,fzrID);
                for (int i = 0; i < bcpTkNonCheckData.size(); i++) {
                    NonCheckBean bean = new NonCheckBean();
                    BcpTkdBean single = bcpTkNonCheckData.get(i);
                    bean.setDh(single.getBackDh());
                    bean.setName("半成品入库（退库）单");
                    bean.setTime(single.getThRq());
                    bean.setTlfzrID(single.getTlfzrID());
                    bean.setSlfzrID(single.getSlfzrID());
                    bean.setState(single.getCheckState());
                    allBeans.add(bean);
                }
                for (int i = 0; i < allBeans.size() - 1; i++) {
                    for (int j = 0; j < allBeans.size() - 1 - i; j++)// j开始等于0，
                    {
                        String beginTime = allBeans.get(j).getTime();
                        String endTime = allBeans.get(j + 1).getTime();
                        if (beginTime.compareTo(endTime) < 0) {
                            NonCheckBean smallBean = allBeans.get(j);
                            NonCheckBean bigBean = allBeans.get(j + 1);
                            allBeans.set(j, bigBean);
                            allBeans.set(j + 1, smallBean);
                        }
                    }
                }
                dataResult.setBeans(allBeans);
                result.setResult(dataResult);
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "传参异常");
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 获取物料入库审核信息（包括入库单详情和关联的每一条记录的信息）
     */
    @RequestMapping(value = "/getWlInVerifyData", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult getWlInVerifyData(String json) {
        VerifyParam param = ParamsUtils.handleParams(json, VerifyParam.class);
        ActionResult<WlInVerifyResult> result = new ActionResult<WlInVerifyResult>();
        if (param != null) {
            try {
                WlInVerifyResult dataResult = new WlInVerifyResult();
                WlRkdBean rkdBean = mainService.getWlRkdBean(param.getDh());
                dataResult.setFhDw(rkdBean.getJhDw());
                dataResult.setInDh(rkdBean.getInDh());
                dataResult.setRemark(rkdBean.getRemark());
                dataResult.setShRq(rkdBean.getShrq());
                dataResult.setZjyID(rkdBean.getZjyID());
                dataResult.setZjy(rkdBean.getZjy());
                dataResult.setZjldID(rkdBean.getZjldID());
                dataResult.setZjld(rkdBean.getZjld());
                dataResult.setFzrID(rkdBean.getFzrID());
                dataResult.setShFzr(rkdBean.getShFzr());
                List<WLINShowBean> wlinDataList = mainService.getWLINShowBeanListByInDh(param.getDh());
                dataResult.setBeans(wlinDataList);
                result.setResult(dataResult);
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "传参异常");
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 获取物料出库审核信息（包括出库单详情和关联的每一条记录的信息）
     */
    @RequestMapping(value = "/getWlOutVerifyData", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult getWlOutVerifyData(String json) {
        VerifyParam param = ParamsUtils.handleParams(json, VerifyParam.class);
        ActionResult<WlOutVerifyResult> result = new ActionResult<WlOutVerifyResult>();
        if (param != null) {
            try {
                WlOutVerifyResult dataResult = new WlOutVerifyResult();
                WlCkdBean ckdBean = mainService.getWlCkdBean(param.getDh());
                dataResult.setLhDw(ckdBean.getLhDw());
                dataResult.setLhR(ckdBean.getLhR());
                dataResult.setKgID(ckdBean.getKgID());
                dataResult.setKg(ckdBean.getKg());
                dataResult.setFlfzrID(ckdBean.getFlfzrID());
                dataResult.setFhFzr(ckdBean.getFhFzr());
                dataResult.setBzID(ckdBean.getBzID());
                dataResult.setBz(ckdBean.getBz());
                dataResult.setLlfzrID(ckdBean.getLlfzrID());
                dataResult.setLhFzr(ckdBean.getLhFzr());
                dataResult.setLhRq(ckdBean.getLhRq());
                dataResult.setOutDh(ckdBean.getOutDh());
                dataResult.setRemark(ckdBean.getRemark());
                List<WLOutShowBean> wlinDataList = mainService.getWLOutShowBeanListByInDh(param.getDh());
                dataResult.setBeans(wlinDataList);
                result.setResult(dataResult);
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "传参异常");
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 获取物料退库审核信息（包括退库单详情和关联的每一条记录的信息）
     */
    @RequestMapping(value = "/getWlTkVerifyData", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult getWlTkVerifyData(String json) {
        VerifyParam param = ParamsUtils.handleParams(json, VerifyParam.class);
        ActionResult<WlTkVerifyResult> result = new ActionResult<WlTkVerifyResult>();
        if (param != null) {
            try {
                WlTkVerifyResult dataResult = new WlTkVerifyResult();
                WlTkdBean tkdBean = mainService.getWlTkdBean(param.getDh());
                dataResult.setThDw(tkdBean.getThDw());
                dataResult.setThR(tkdBean.getThR());
                dataResult.setShR(tkdBean.getShR());
                dataResult.setShFzr(tkdBean.getShFzr());
                dataResult.setThRq(tkdBean.getThRq());
                dataResult.setBackDh(tkdBean.getBackDh());
                dataResult.setBzID(tkdBean.getBzID());
                dataResult.setBz(tkdBean.getBz());
                dataResult.setBzStatus(tkdBean.getBzStatus());
                dataResult.setFzrID(tkdBean.getFzrID());
                dataResult.setTlfzrID(tkdBean.getTlfzrID());
                dataResult.setThFzr(tkdBean.getThFzr());
                dataResult.setTlfzrStatus(tkdBean.getTlfzrStatus());
                dataResult.setKgID(tkdBean.getKgID());
                dataResult.setKg(tkdBean.getKg());
                dataResult.setKgStatus(tkdBean.getKgStatus());
                dataResult.setSlfzrID(tkdBean.getSlfzrID());
                dataResult.setShFzr(tkdBean.getShFzr());
                dataResult.setSlfzrStatus(tkdBean.getSlfzrStatus());
                dataResult.setRemark(tkdBean.getRemark());
                List<WLTkShowBean> wlinDataList = mainService.getWLTkShowBeanListByOutDh(param.getDh());
                dataResult.setBeans(wlinDataList);
                result.setResult(dataResult);
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "传参异常");
    }

    /**
     * @return
     * @author 逄坤
     * @desc 获取物料退库质检信息（包括退库单详情和关联的每一条记录的信息）
     */
    @RequestMapping(value = "/getWlTkQualityCheckData", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult getWlTkQualityCheckData(String json) {
        VerifyParam param = ParamsUtils.handleParams(json, VerifyParam.class);
        ActionResult<WlTkQualityCheckResult> result = new ActionResult<WlTkQualityCheckResult>();
        if (param != null) {
            try {
                WlTkQualityCheckResult dataResult = new WlTkQualityCheckResult();
                WlTkdBean tkdBean = mainService.getWlTkdBean(param.getDh());
                dataResult.setThDw(tkdBean.getThDw());
                dataResult.setThFzr(tkdBean.getThFzr());
                dataResult.setThR(tkdBean.getThR());
                dataResult.setShFzr(tkdBean.getShFzr());
                dataResult.setThRq(tkdBean.getThRq());
                dataResult.setBackDh(tkdBean.getBackDh());
                dataResult.setRemark(tkdBean.getRemark());
                List<WLTkShowBean> wlinDataList = mainService.getWLTkShowBeanListByOutDh(param.getDh());
                dataResult.setBeans(wlinDataList);
                result.setResult(dataResult);
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "传参异常");
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 获取半成品入库审核信息（包括入库单详情和关联的每一条记录的信息）
     */
    @RequestMapping(value = "/getBcpInVerifyData", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult getBcpInVerifyData(String json) {
        VerifyParam param = ParamsUtils.handleParams(json, VerifyParam.class);
        ActionResult<BcpInVerifyResult> result = new ActionResult<BcpInVerifyResult>();
        if (param != null) {
            try {
                BcpInVerifyResult dataResult = new BcpInVerifyResult();
                BcpRkdBean bean = mainService.getBcpRkdBean(param.getDh());
                dataResult.setJhDw(bean.getJhDw());
                dataResult.setJhR(bean.getJhR());
                dataResult.setShR(bean.getShR());
                dataResult.setShRq(bean.getShrq());
                dataResult.setInDh(bean.getInDh());
                dataResult.setBzID(bean.getBzID());
                dataResult.setBz(bean.getBz());
                dataResult.setFzrID(bean.getFzrID());
                dataResult.setFzr(bean.getFzr());
                dataResult.setFlfzrID(bean.getFlfzrID());
                dataResult.setJhFzr(bean.getJhFzr());
                dataResult.setZjyID(bean.getZjyID());
                dataResult.setZjy(bean.getZjy());
                dataResult.setZjldID(bean.getZjldID());
                dataResult.setZjld(bean.getZjld());
                dataResult.setKgID(bean.getKgID());
                dataResult.setKg(bean.getKg());
                dataResult.setLlfzrID(bean.getLlfzrID());
                dataResult.setShFzr(bean.getShFzr());
                dataResult.setRemark(bean.getRemark());
                List<BcpInShowBean> wlinDataList = mainService.getBcpInShowBeanListByInDh(param.getDh());//先查询是不是半成品
                if (wlinDataList == null || wlinDataList.size() <= 0) {
                    wlinDataList = mainService.getCpInShowBeanListByInDh(param.getDh());//如果半成品没有记录的话，再查成品小包装
                }
                if (wlinDataList == null || wlinDataList.size() <= 0) {
                    wlinDataList = mainService.getBigCpInShowBeanListByInDh(param.getDh());//如果成品小包装也没有记录的话，说明就是成品大包装
                    //大包装入库记录是没有数量的，所以会是null，但App接收的时候是用float接收的，会出错，所以设置成0
                    for (int i = 0; i < wlinDataList.size(); i++) {
                        wlinDataList.get(i).setShl(0);
                    }
                }
                dataResult.setBeans(wlinDataList);
                result.setResult(dataResult);
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "传参异常");
    }

    /**
     * 逄坤
     * 根据大包装二维码获取成品小包装入库审核信息
     * @param json
     * @return
     */
    @RequestMapping(value = "/getSmallCPInVerifyData", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult getSmallCPInVerifyData(String json) {
        BcpInShowBean param = ParamsUtils.handleParams(json, BcpInShowBean.class);
        ActionResult<BcpInVerifyResult> result = new ActionResult<BcpInVerifyResult>();
        BcpInVerifyResult dataResult = new BcpInVerifyResult();
        List<BcpInShowBean> bcpInDataList = mainService.getCpInShowBeanListByCPS2QRCode(param.getqRCodeID());
        dataResult.setBeans(bcpInDataList);
        result.setResult(dataResult);
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
    }

    /**
     * @return
     * @author 逄坤
     * @desc 获取半成品入库质检信息（包括入库单详情和关联的每一条记录的信息）
     */
    @RequestMapping(value = "/getBcpInQualityCheckData", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult getBcpInQualityCheckData(String json) {
        VerifyParam param = ParamsUtils.handleParams(json, VerifyParam.class);
        ActionResult<BcpInQualityCheckResult> result = new ActionResult<BcpInQualityCheckResult>();
        if (param != null) {
            try {
                BcpInQualityCheckResult dataResult = new BcpInQualityCheckResult();
                BcpRkdBean bean = mainService.getBcpRkdBean(param.getDh());
                dataResult.setJhDw(bean.getJhDw());
                dataResult.setShFzr(bean.getShFzr());
                dataResult.setJhR(bean.getJhR());
                dataResult.setJhFzr(bean.getJhFzr());
                dataResult.setShRq(bean.getShrq());
                dataResult.setInDh(bean.getInDh());
                dataResult.setRemark(bean.getRemark());
                List<BcpInShowBean> wlinDataList = mainService.getBcpInShowBeanListByInDh(param.getDh());
                if (wlinDataList == null || wlinDataList.size() <= 0) {
                    wlinDataList = mainService.getCpInShowBeanListByInDh(param.getDh());
                }
                if (wlinDataList == null || wlinDataList.size() <= 0) {
                    wlinDataList = mainService.getBigCpInShowBeanListByInDh(param.getDh());
                    //大包装入库记录是没有数量的，所以会是null，但App接收的时候是用float接收的，会出错，所以设置成-1
                    for (int i = 0; i < wlinDataList.size(); i++) {
                        wlinDataList.get(i).setShl(-1);
                    }
                }
                dataResult.setBeans(wlinDataList);
                result.setResult(dataResult);
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "传参异常");
    }

    /**
     * 逄坤
     * 根据大包装二维码获取成品小包装入库质检信息
     * @param json
     * @return
     */
    @RequestMapping(value = "/getSmallCPInQualityCheckData", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult getSmallCPInQualityCheckData(String json){
        BcpInShowBean param = ParamsUtils.handleParams(json, BcpInShowBean.class);
        ActionResult<BcpInQualityCheckResult> result = new ActionResult<BcpInQualityCheckResult>();
        BcpInQualityCheckResult dataResult = new BcpInQualityCheckResult();
        List<BcpInShowBean> bcpInDataList = mainService.getCpInShowBeanListByCPS2QRCode(param.getqRCodeID());
        dataResult.setBeans(bcpInDataList);
        result.setResult(dataResult);
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 获取半成品出库审核信息（包括出库单详情和关联的每一条记录的信息）
     */
    @RequestMapping(value = "/getCpOutVerifyData", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult getCpOutVerifyData(String json) {
        VerifyParam param = ParamsUtils.handleParams(json, VerifyParam.class);
        ActionResult<CpOutVerifyResult> result = new ActionResult<CpOutVerifyResult>();
        if (param != null) {
            try {
                CpOutVerifyResult dataResult = new CpOutVerifyResult();
                BcpCkdBean bean = mainService.getBcpCkdBean(param.getDh());
                dataResult.setFhFzr(bean.getFhFzr());
                dataResult.setLhRq(bean.getLhRq());
                dataResult.setOutDh(bean.getOutDh());
                dataResult.setRemark(bean.getRemark());
                List<CpOutShowBean> wlinDataList = mainService.getCpOutShowBeanListByOutDh(param.getDh());
                dataResult.setBeans(wlinDataList);
                result.setResult(dataResult);
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "传参异常");
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 获取半成品退库审核信息（包括退库单详情和关联的每一条记录的信息）
     */
    @RequestMapping(value = "/getBcpTkVerifyData", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult getBcpTkVerifyData(String json) {
        VerifyParam param = ParamsUtils.handleParams(json, VerifyParam.class);
        ActionResult<BcpTkVerifyResult> result = new ActionResult<BcpTkVerifyResult>();
        if (param != null) {
            try {
                BcpTkVerifyResult dataResult = new BcpTkVerifyResult();
                BcpTkdBean bean = mainService.getBcpTkdBean(param.getDh());
                dataResult.setThDw(bean.getThDw());
                dataResult.setBackDh(bean.getBackDh());
                dataResult.setRemark(bean.getRemark());
                dataResult.setShFzr(bean.getShFzr());
                dataResult.setThFzr(bean.getThFzr());
                dataResult.setThR(bean.getThR());
                dataResult.setThRq(bean.getThRq());
                dataResult.setBzID(bean.getBzID());
                dataResult.setBzStatus(bean.getBzStatus());
                dataResult.setBzName(bean.getBzName());
                dataResult.setZjyID(bean.getZjyID());
                dataResult.setZjyStatus(bean.getZjyStatus());
                dataResult.setZjyName(bean.getZjyName());
                dataResult.setZjldID(bean.getZjldID());
                dataResult.setZjldStatus(bean.getZjldStatus());
                dataResult.setZjldName(bean.getZjldName());
                List<BcpTkShowBean> wlinDataList = mainService.getBcpTkShowBeanListByBackDh(param.getDh());
                dataResult.setBeans(wlinDataList);
                result.setResult(dataResult);
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "传参异常");
    }

    /**
     * @return
     * @author 逄坤
     * @desc 获取半成品退库质检信息（包括退库单详情和关联的每一条记录的信息）
     */
    @RequestMapping(value = "/getBcpTkQualityCheckData", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult getBcpTkQualityCheckData(String json) {
        VerifyParam param = ParamsUtils.handleParams(json, VerifyParam.class);
        ActionResult<BcpTkQualityCheckResult> result = new ActionResult<BcpTkQualityCheckResult>();
        if (param != null) {
            try {
                BcpTkQualityCheckResult dataResult = new BcpTkQualityCheckResult();
                BcpTkdBean bean = mainService.getBcpTkdBean(param.getDh());
                dataResult.setThDw(bean.getThDw());
                dataResult.setBackDh(bean.getBackDh());
                dataResult.setRemark(bean.getRemark());
                dataResult.setShFzr(bean.getShFzr());
                dataResult.setThFzr(bean.getThFzr());
                dataResult.setThR(bean.getThR());
                dataResult.setThRq(bean.getThRq());
                List<BcpTkShowBean> wlinDataList = mainService.getBcpTkShowBeanListByBackDh(param.getDh());
                dataResult.setBeans(wlinDataList);
                result.setResult(dataResult);
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "传参异常");
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 修改物料入库单审核标志为已审核
     */
    @RequestMapping(value = "/agreeWlIn", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult agreeWlIn(String json) {
        VerifyParam param = ParamsUtils.handleParams(json, VerifyParam.class);
        ActionResult<ActionResult> result = new ActionResult<ActionResult>();
        if (param != null) {
            try {
                Integer fzrStatus=0;
                Integer zjyStatus=0;
                Integer zjldStatus=0;
                if(param.getCheckQXFlag()==VerifyParam.FZR)
                    fzrStatus=1;
                else if(param.getCheckQXFlag()==VerifyParam.ZJY)
                    zjyStatus=1;
                else if(param.getCheckQXFlag()==VerifyParam.ZJLD)
                    zjldStatus=1;

                WlRkdBean wlrkd=new WlRkdBean();
                wlrkd.setInDh(param.getDh());
                wlrkd.setFzrStatus(fzrStatus);
                wlrkd.setZjyStatus(zjyStatus);
                wlrkd.setZjldStatus(zjldStatus);
                int a = mainService.agreeWlIn(wlrkd);
                if (a == 1) {
                    CreateWLRKDParam wlRKD = mainService.getRkdWlByInDh(param.getDh());
                    if(wlRKD.getZjyStatus()==1&&wlRKD.getZjldStatus()==1&&wlRKD.getFzrStatus()==1){
                        /**
                         * @author 马鹏昊
                         * @desc 插入到库存表
                         */
                        List<WLINParam> wlinList = mainService.getWLINParamListByInDh(param.getDh());
                        int count=a;
                        int insertCount=0;
                        String existQrCodeID="";
                        int size = wlinList.size();
                        for (int i=0;i<size;i++){
                            WLINParam wlinParam = wlinList.get(i);
                            a = mainService.queryWLS(wlinParam.getqRCodeID());
                            if (a <= 0) {
                                insertCount+=mainService.insertWLS(wlinParam);
                            }
                            else if (a >= 1){
                                existQrCodeID+=","+wlinParam.getqRCodeID();
                            }
                        }
                        if(insertCount==size){
                            //return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "库存表插入记录成功");
                        }
                        else{
                            existQrCodeID=existQrCodeID.substring(1);
                            //return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "库存表中二维码id"+existQrCodeID+"记录已存在，其他记录插入成功");
                        }
                    }
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
                } else {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "审核失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "传参异常");
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 修改物料入库单审核标志为未通过审核
     */
    @RequestMapping(value = "/refuseWlIn", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult refuseWlIn(String json) {
        VerifyParam param = ParamsUtils.handleParams(json, VerifyParam.class);
        ActionResult<ActionResult> result = new ActionResult<ActionResult>();
        if (param != null) {
            try {
                Integer fzrStatus=0;
                Integer zjyStatus=0;
                Integer zjldStatus=0;
                if(param.getCheckQXFlag()==VerifyParam.FZR)
                    fzrStatus=2;
                else if(param.getCheckQXFlag()==VerifyParam.ZJY)
                    zjyStatus=2;
                else if(param.getCheckQXFlag()==VerifyParam.ZJLD)
                    zjldStatus=2;

                WlRkdBean wlrkd=new WlRkdBean();
                wlrkd.setInDh(param.getDh());
                wlrkd.setFzrStatus(fzrStatus);
                wlrkd.setZjyStatus(zjyStatus);
                wlrkd.setZjldStatus(zjldStatus);
                int a = mainService.refuseWlIn(wlrkd);
                if (a == 1) {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
                } else {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "审核失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "传参异常");
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 修改物料出库单审核标志为已审核
     */
    @RequestMapping(value = "/agreeWlOut", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult agreeWlOut(String json) {
        VerifyParam param = ParamsUtils.handleParams(json, VerifyParam.class);
        ActionResult<ActionResult> result = new ActionResult<ActionResult>();
        if (param != null) {
            try {
                Integer kgStatus=0;
                Integer flfzrStatus=0;
                Integer bzStatus=0;
                Integer llfzrStatus=0;
                if(param.getCheckQXFlag()==VerifyParam.KG)
                    kgStatus=1;
                if(param.getCheckQXFlag()==VerifyParam.FLFZR)
                    flfzrStatus=1;
                else if(param.getCheckQXFlag()==VerifyParam.BZ)
                    bzStatus=1;
                else if(param.getCheckQXFlag()==VerifyParam.LLFZR)
                    llfzrStatus=1;

                WlCkdBean wlckd=new WlCkdBean();
                wlckd.setOutDh(param.getDh());
                wlckd.setKgStatus(kgStatus);
                wlckd.setFlfzrStatus(flfzrStatus);
                wlckd.setBzStatus(bzStatus);
                wlckd.setLlfzrStatus(llfzrStatus);

                int a = mainService.agreeWlOut(wlckd);
                if (a == 1) {
                    WlCkdBean wlCkd = mainService.getWlCkdBean(param.getDh());
                    if(wlCkd.getKgStatus()==1&&wlCkd.getFlfzrStatus()==1&&wlCkd.getBzStatus()==1&&wlCkd.getLlfzrStatus()==1) {
                        List<WLOutParam> wlOutList = mainService.getWLOutParamListByInDh(param.getDh());
                        for (WLOutParam wlOutParam : wlOutList) {
                            //更新仓库库存表数量
                            a = mainService.outUpdateWLS(wlOutParam);
                            if (a <= 0) {
                                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_LOGIC_ERROR, "修改库存表数据失败");
                            }
                            //查询临时库存表中是否有数据
                            a = mainService.findWLTempS(wlOutParam.getQrCodeId());
                            if (a <= 0) {
                                //插入临时库存表（车间）
                                a = mainService.insertWLTempS(wlOutParam);
                            } else {
                                a = mainService.updateWLTempS(wlOutParam);
                            }
                        }
                    }
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
                } else {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "审核失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "传参异常");
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 修改物料出库单审核标志为未通过审核
     */
    @RequestMapping(value = "/refuseWlOut", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult refuseWlOut(String json) {
        VerifyParam param = ParamsUtils.handleParams(json, VerifyParam.class);
        ActionResult<ActionResult> result = new ActionResult<ActionResult>();
        if (param != null) {
            try {
                Integer kgStatus=0;
                Integer flfzrStatus=0;
                Integer bzStatus=0;
                Integer llfzrStatus=0;
                if(param.getCheckQXFlag()==VerifyParam.KG)
                    kgStatus=2;
                else if(param.getCheckQXFlag()==VerifyParam.FLFZR)
                    flfzrStatus=2;
                if(param.getCheckQXFlag()==VerifyParam.BZ)
                    bzStatus=2;
                else if(param.getCheckQXFlag()==VerifyParam.LLFZR)
                    llfzrStatus=2;

                WlCkdBean wlckd=new WlCkdBean();
                wlckd.setOutDh(param.getDh());
                wlckd.setKgStatus(kgStatus);
                wlckd.setFlfzrStatus(flfzrStatus);
                wlckd.setBzStatus(bzStatus);
                wlckd.setLlfzrStatus(llfzrStatus);

                int a = mainService.refuseWlOut(wlckd);
                if (a == 1) {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
                } else {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "审核失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "传参异常");
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 修改物料退库单审核标志为已审核
     */
    @RequestMapping(value = "/agreeWlTk", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult agreeWlTk(String json) {
        VerifyParam param = ParamsUtils.handleParams(json, VerifyParam.class);
        ActionResult<ActionResult> result = new ActionResult<ActionResult>();
        if (param != null) {
            try {
                Integer bzStatus=0;
                Integer tlfzrStatus=0;
                Integer kgStatus=0;
                Integer slfzrStatus=0;
                if(param.getCheckQXFlag()==VerifyParam.BZ)
                    bzStatus=1;
                else if(param.getCheckQXFlag()==VerifyParam.TLFZR)
                    tlfzrStatus=1;
                else if(param.getCheckQXFlag()==VerifyParam.KG)
                    kgStatus=1;
                else if(param.getCheckQXFlag()==VerifyParam.SLFZR)
                    slfzrStatus=1;

                WlTkdBean wltkd=new WlTkdBean();
                wltkd.setBackDh(param.getDh());
                wltkd.setBzStatus(bzStatus);
                wltkd.setTlfzrStatus(tlfzrStatus);
                wltkd.setKgStatus(kgStatus);
                wltkd.setSlfzrStatus(slfzrStatus);

                int a = mainService.agreeWlTk(wltkd);
                if (a == 1) {

                    WlTkdBean wlTKD = mainService.getWlTkdBean(param.getDh());
                    if(wlTKD.getBzStatus()==1&&wlTKD.getTlfzrStatus()==1&&wlTKD.getKgStatus()==1&&wlTKD.getSlfzrStatus()==1){
                        //更新仓库库存表数量（退库的数量加上）
                        List<WLTKParam> wlTKList = mainService.getWLTKParamListByOutDh(param.getDh());
                        for(WLTKParam wlTKParam : wlTKList) {
                            a = mainService.updateWLSByTk(wlTKParam);
                            if (a <= 0) {
                                //return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_LOGIC_ERROR, "修改库存表数据失败");
                            }
                            //临时库存表中数据减去或者删除
                            WLTempSBean wlTempSBean = mainService.getWLTempS(wlTKParam.getQrCodeId());
                            if (wlTKParam.getTkShL() >= wlTempSBean.getSHL()&&wlTKParam.getDwzl() >= wlTempSBean.getDWZL()) {
                                a = mainService.deleteFromWLTempS(wlTKParam.getQrCodeId());
                            } else {
                                a = mainService.updateWLTempSByTk(wlTKParam);
                            }
                        }
                    }

                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
                } else {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "审核失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "传参异常");
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 修改物料退库单审核标志为未通过审核
     */
    @RequestMapping(value = "/refuseWlTk", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult refuseWlTk(String json) {
        VerifyParam param = ParamsUtils.handleParams(json, VerifyParam.class);
        ActionResult<ActionResult> result = new ActionResult<ActionResult>();
        if (param != null) {
            try {
                Integer bzStatus=0;
                Integer tlfzrStatus=0;
                Integer kgStatus=0;
                Integer slfzrStatus=0;
                if(param.getCheckQXFlag()==VerifyParam.BZ)
                    bzStatus=2;
                else if(param.getCheckQXFlag()==VerifyParam.TLFZR)
                    tlfzrStatus=2;
                else if(param.getCheckQXFlag()==VerifyParam.KG)
                    kgStatus=2;
                else if(param.getCheckQXFlag()==VerifyParam.SLFZR)
                    slfzrStatus=2;

                WlTkdBean wltkd=new WlTkdBean();
                wltkd.setBackDh(param.getDh());
                wltkd.setBzStatus(bzStatus);
                wltkd.setTlfzrStatus(tlfzrStatus);
                wltkd.setKgStatus(kgStatus);
                wltkd.setSlfzrStatus(slfzrStatus);

                int a = mainService.refuseWlTk(wltkd);
                if (a == 1) {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
                } else {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "审核失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "传参异常");
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 修改半成品入库单审核标志为已审核
     */
    @RequestMapping(value = "/agreeBcpIn", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult agreeBcpIn(String json) {
        VerifyParam param = ParamsUtils.handleParams(json, VerifyParam.class);
        ActionResult<ActionResult> result = new ActionResult<ActionResult>();
        if (param != null) {
            try {
                Integer bzStatus=0;
                Integer bcpBzStatus=0;
                Integer cpBzStatus=0;
                Integer fzrStatus=0;
                Integer zjyStatus=0;
                Integer zjldStatus=0;
                Integer flfzrStatus=0;
                Integer kgStatus=0;
                Integer llfzrStatus=0;
                if(param.getCheckQXFlag()==VerifyParam.BCPBZ) {
                    bzStatus = 1;
                    bcpBzStatus = 1;
                }
                else if(param.getCheckQXFlag()==VerifyParam.CPBZ) {
                    bzStatus = 1;
                    cpBzStatus = 1;
                }
                else if(param.getCheckQXFlag()==VerifyParam.FZR)
                    fzrStatus=1;
                else if(param.getCheckQXFlag()==VerifyParam.ZJY)
                    zjyStatus=1;
                else if(param.getCheckQXFlag()==VerifyParam.ZJLD)
                    zjldStatus=1;
                else if(param.getCheckQXFlag()==VerifyParam.FLFZR)
                    flfzrStatus=1;
                else if(param.getCheckQXFlag()==VerifyParam.KG)
                    kgStatus=1;
                else if(param.getCheckQXFlag()==VerifyParam.LLFZR)
                    llfzrStatus=1;

                BcpRkdBean bcpRkd=new BcpRkdBean();
                bcpRkd.setInDh(param.getDh());
                bcpRkd.setBzStatus(bzStatus);
                bcpRkd.setBcpBzStatus(bcpBzStatus);
                bcpRkd.setCpBzStatus(cpBzStatus);
                bcpRkd.setFzrStatus(fzrStatus);
                bcpRkd.setZjyStatus(zjyStatus);
                bcpRkd.setZjldStatus(zjldStatus);
                bcpRkd.setFlfzrStatus(flfzrStatus);
                bcpRkd.setKgStatus(kgStatus);
                bcpRkd.setLlfzrStatus(llfzrStatus);

                int a = mainService.agreeBcpIn(bcpRkd);
                if (a == 1) {
                    if(zjyStatus==1){
                        //List<BcpInShowBean> bcpInShowList = mainService.getBigCpInShowBeanListByInDh(param.getDh());
                        //List<BcpInShowBean> bcpInShowList = param.getBcpInShowJAStr();
                        JSONArray bcpInShowJA = JSONArray.fromObject(param.getBcpInShowJAStr());
                        for (int i=0;i<bcpInShowJA.size();i++) {
                            JSONObject bcpInShowJO = (JSONObject)bcpInShowJA.get(i);
                            String qRCodeID = bcpInShowJO.getString("qRCodeID");
                            String zjy = bcpInShowJO.getString("zjy");
                            int zjzt = bcpInShowJO.getInt("zjzt");
                            if("2".equals(qRCodeID.substring(8,9)))
                                mainService.changeBCPInPassCheckFlag(qRCodeID,zjy,zjzt);
                            else if("4".equals(qRCodeID.substring(8,9)))
                                mainService.changeBIG_CPInPassCheckFlag(qRCodeID,zjy,zjzt);
                        }
                    }
                    BcpRkdBean bcpRkdBean = mainService.getBcpRkdBean(param.getDh());
                    if (bcpRkdBean.getBzStatus() == 1 && bcpRkdBean.getFzrStatus() == 1 && bcpRkdBean.getZjyStatus() == 1 && bcpRkdBean.getZjldStatus() == 1) {
                        //因为入库单表里可能是半成品或成品，所以这里要先验证下半成品入库记录里有无数据，没有的话说明是成品
                        List<BcpInShowBean> bcpInDataList = mainService.getBcpInShowBeanListByInDh(param.getDh());
                        if (bcpInDataList == null || bcpInDataList.size() <= 0) {
                            //?????????
                            /*
                            else {
                                List<SmallCPINParam> smallCPINList = mainService.getSmallCPINParamListByInDh(param.getDh());
                                SmallCPINParam inParam = smallCPINList.get(0);
                                String startQrCodeId = inParam.getQrCodeId();
                                Long nextQrCodeId = Long.parseLong(startQrCodeId);
                                size = smallCPINList.size();
                                BigCpBean bigCpBean = null;
                                int nowIndex = 0;
                                if (!TextUtils.isEmpty(inParam.getcPS2QRCode())) {
                                    bigCpBean = mainService.getCPS2(inParam.getcPS2QRCode());
                                    nowIndex = bigCpBean.getNowNum();
                                }
                                for (int i = 0; i < size; i++) {
                                    //插入小包装库存表（车间）
                                    a = mainService.insertCPS(inParam);
                                    if (a <= 0) {
                                        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_MESSAGE_ERROR, "录入CPS失败");
                                    }
                                    //如果是需要关联大包装的小包装则需要以下操作
                                    if (!TextUtils.isEmpty(inParam.getcPS2QRCode())) {
                                        bigCpBean = ProjectUtil.getUpdateCPS2Data(bigCpBean, nowIndex + 1, nextQrCodeId);
                                        a = mainService.updateCPS2(bigCpBean);
                                        a = mainService.updateCPIn2(bigCpBean);
                                    }

                                    String typeNum = nextQrCodeId.toString().substring(0, 9);
                                    int num = Integer.valueOf(nextQrCodeId.toString().substring(9));
                                    num++;
                                    nextQrCodeId = Long.parseLong(typeNum + num);
                                    nowIndex++;
                                    inParam.setQrCodeId(String.valueOf(nextQrCodeId));
                                }
                            }
                            */
                        }
                    }
                    else if(bcpRkdBean.getBzStatus() == 1 && bcpRkdBean.getFlfzrStatus() == 1 && bcpRkdBean.getZjyStatus() == 1
                            && bcpRkdBean.getZjldStatus() == 1 && bcpRkdBean.getKgStatus() == 1 && bcpRkdBean.getLlfzrStatus() == 1){
                        int count = a;
                        List<BigCPINParam> bigCPINList = mainService.getBigCPINParamListByInDh(param.getDh());
                        BigCPINParam bigCPINParam = null;
                        int size = bigCPINList.size();
                        if (size > 0) {
                            for (int i = 0; i < size; i++) {
                                bigCPINParam = bigCPINList.get(i);
                                a = mainService.findCPS2(bigCPINParam.getQrCodeId());
                                if (a <= 0) {
                                    //插入大包装库存表（车间）
                                    a = mainService.insertCPS2(bigCPINParam);

                                }

                                //以下代码是根据大包装关联需要入库的小包装
                                List<SmallCPINParam> smallCPINList = mainService.getSmallCPINParamListByCPS2QRCode(bigCPINParam.getQrCodeId());
                                SmallCPINParam smallCPINParam = null;
                                BigCpBean bigCpBean = null;
                                int nowIndex = 0;
                                Long nextQrCodeId = null;
                                int size1 = smallCPINList.size();
                                if(size1>0) {
                                    smallCPINParam = smallCPINList.get(0);
                                    String startQrCodeId = smallCPINParam.getQrCodeId();
                                    nextQrCodeId = Long.parseLong(startQrCodeId);
                                    if (!TextUtils.isEmpty(smallCPINParam.getcPS2QRCode())) {
                                        bigCpBean = mainService.getCPS2(smallCPINParam.getcPS2QRCode());
                                        nowIndex = bigCpBean.getNowNum();
                                    }
                                }
                                for (int j = 0; j < size1; j++) {
                                    //插入小包装库存表（车间）
                                    a = mainService.insertCPS(smallCPINParam);
                                    if (a <= 0) {
                                        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_MESSAGE_ERROR, "录入CPS失败");
                                    }
                                    //如果是需要关联大包装的小包装则需要以下操作
                                    if (!TextUtils.isEmpty(smallCPINParam.getcPS2QRCode())) {
                                        bigCpBean = ProjectUtil.getUpdateCPS2Data(bigCpBean, nowIndex + 1, nextQrCodeId);
                                        a = mainService.updateCPS2(bigCpBean);
                                        a = mainService.updateCPIn2(bigCpBean);
                                    }

                                    String typeNum = nextQrCodeId.toString().substring(0, 9);
                                    int num = Integer.valueOf(nextQrCodeId.toString().substring(9));
                                    num++;
                                    nextQrCodeId = Long.parseLong(typeNum + num);
                                    nowIndex++;
                                    smallCPINParam.setQrCodeId(String.valueOf(nextQrCodeId));
                                }
                            }
                        }
                    }

                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
                } else {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "审核失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "传参异常");
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 修改半成品入库单审核标志为未通过审核
     */
    @RequestMapping(value = "/refuseBcpIn", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult refuseBcpIn(String json) {
        VerifyParam param = ParamsUtils.handleParams(json, VerifyParam.class);
        ActionResult<ActionResult> result = new ActionResult<ActionResult>();
        if (param != null) {
            try {
                Integer bzStatus=0;
                Integer bcpBzStatus=0;
                Integer cpBzStatus=0;
                Integer fzrStatus=0;
                Integer zjyStatus=0;
                Integer zjldStatus=0;
                Integer flfzrStatus=0;
                Integer kgStatus=0;
                Integer llfzrStatus=0;
                if(param.getCheckQXFlag()==VerifyParam.BCPBZ) {
                    bzStatus = 2;
                    bcpBzStatus=2;
                }
                else if(param.getCheckQXFlag()==VerifyParam.CPBZ) {
                    bzStatus = 2;
                    cpBzStatus=2;
                }
                else if(param.getCheckQXFlag()==VerifyParam.FZR)
                    fzrStatus=2;
                else if(param.getCheckQXFlag()==VerifyParam.ZJY)
                    zjyStatus=2;
                else if(param.getCheckQXFlag()==VerifyParam.ZJLD)
                    zjldStatus=2;
                else if(param.getCheckQXFlag()==VerifyParam.FLFZR)
                    flfzrStatus=2;
                else if(param.getCheckQXFlag()==VerifyParam.KG)
                    kgStatus=2;
                else if(param.getCheckQXFlag()==VerifyParam.LLFZR)
                    llfzrStatus=2;

                BcpRkdBean bcpRkd=new BcpRkdBean();
                bcpRkd.setInDh(param.getDh());
                bcpRkd.setBzStatus(bzStatus);
                bcpRkd.setBcpBzStatus(bcpBzStatus);
                bcpRkd.setCpBzStatus(cpBzStatus);
                bcpRkd.setFzrStatus(fzrStatus);
                bcpRkd.setZjyStatus(zjyStatus);
                bcpRkd.setZjldStatus(zjldStatus);
                bcpRkd.setFlfzrStatus(flfzrStatus);
                bcpRkd.setKgStatus(kgStatus);
                bcpRkd.setLlfzrStatus(llfzrStatus);

                int a = mainService.refuseBcpIn(bcpRkd);
                if (a == 1) {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
                } else {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "审核失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "传参异常");
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 修改半成品出库单审核标志为已审核
     */
    @RequestMapping(value = "/agreeBcpOut", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult agreeBcpOut(String json) {
        VerifyParam param = ParamsUtils.handleParams(json, VerifyParam.class);
        ActionResult<ActionResult> result = new ActionResult<ActionResult>();
        if (param != null) {
            try {
                Integer kgStatus=0;
                Integer fzrStatus=0;
                if(param.getCheckQXFlag()==VerifyParam.KG)
                    kgStatus=1;
                else if(param.getCheckQXFlag()==VerifyParam.FZR)
                    fzrStatus=1;

                BcpCkdBean bcpckd=new BcpCkdBean();
                bcpckd.setOutDh(param.getDh());
                bcpckd.setKgStatus(kgStatus);
                bcpckd.setFzrStatus(fzrStatus);
                int a = mainService.agreeBcpOut(bcpckd);
                if (a == 1) {
                    BcpCkdBean bcpCkd = mainService.getBcpCkdBean(param.getDh());
                    if(bcpCkd.getKgStatus()==1&&bcpCkd.getFzrStatus()==1) {
                        List<BigCpOutParam> cpOutList = mainService.getBigCpOutParamListByOutDh(param.getDh());
                        for (BigCpOutParam cpOutParam : cpOutList) {
                            BigCpBean bigCpBean = mainService.getCPS2(cpOutParam.getQrCodeId());
                            if (bigCpBean != null) {
                                a = mainService.deleteCPS2ByQrId(cpOutParam.getQrCodeId());
                                if (a <= 0) {
                                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_MESSAGE_ERROR, "删除大包装记录失败");
                                }
                                a = mainService.deleteCPSByCps2QrId(cpOutParam.getQrCodeId());
                            } else {
                                a = mainService.deleteCPSByQrId(cpOutParam.getQrCodeId());
                                if (a <= 0) {
                                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_MESSAGE_ERROR, "删除小包装记录失败");
                                }
                            }
                        }
                    }

                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
                } else {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "审核失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "传参异常");
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 修改半成品出库单审核标志为未通过审核
     */
    @RequestMapping(value = "/refuseBcpOut", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult refuseBcpOut(String json) {
        VerifyParam param = ParamsUtils.handleParams(json, VerifyParam.class);
        ActionResult<ActionResult> result = new ActionResult<ActionResult>();
        if (param != null) {
            try {
                Integer kgStatus=0;
                Integer fzrStatus=0;
                if(param.getCheckQXFlag()==VerifyParam.KG)
                    kgStatus=2;
                else if(param.getCheckQXFlag()==VerifyParam.FZR)
                    fzrStatus=2;

                BcpCkdBean bcpCkd=new BcpCkdBean();
                bcpCkd.setOutDh(param.getDh());
                bcpCkd.setKgStatus(kgStatus);
                bcpCkd.setFzrStatus(fzrStatus);

                int a = mainService.refuseBcpOut(bcpCkd);
                if (a == 1) {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
                } else {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "审核失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "传参异常");
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 修改半成品退库单审核标志为已审核
     */
    @RequestMapping(value = "/agreeBcpTk", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult agreeBcpTk(String json) {
        VerifyParam param = ParamsUtils.handleParams(json, VerifyParam.class);
        ActionResult<ActionResult> result = new ActionResult<ActionResult>();
        if (param != null) {
            try {
                Integer bzStatus=0;
                Integer tlfzrStatus=0;
                Integer zjyStatus=0;
                Integer zjldStatus=0;
                Integer kgStatus=0;
                Integer slfzrStatus=0;
                if(param.getCheckQXFlag()==VerifyParam.BZ)
                    bzStatus=1;
                else if(param.getCheckQXFlag()==VerifyParam.TLFZR)
                    tlfzrStatus=1;
                else if(param.getCheckQXFlag()==VerifyParam.ZJY)
                    zjyStatus=1;
                else if(param.getCheckQXFlag()==VerifyParam.ZJLD)
                    zjldStatus=1;
                else if(param.getCheckQXFlag()==VerifyParam.KG)
                    kgStatus=1;
                else if(param.getCheckQXFlag()==VerifyParam.SLFZR)
                    slfzrStatus=1;

                BcpTkdBean bcpTkd=new BcpTkdBean();
                bcpTkd.setBackDh(param.getDh());
                bcpTkd.setBzStatus(bzStatus);
                bcpTkd.setTlfzrStatus(tlfzrStatus);
                bcpTkd.setZjyStatus(zjyStatus);
                bcpTkd.setZjldStatus(zjldStatus);
                bcpTkd.setKgStatus(kgStatus);
                bcpTkd.setSlfzrStatus(slfzrStatus);

                int a = mainService.agreeBcpTk(bcpTkd);
                if (a == 1) {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
                } else {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "审核失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "传参异常");
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 修改半成品退库单审核标志为未通过审核
     */
    @RequestMapping(value = "/refuseBcpTk", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult refuseBcpTk(String json) {
        VerifyParam param = ParamsUtils.handleParams(json, VerifyParam.class);
        ActionResult<ActionResult> result = new ActionResult<ActionResult>();
        if (param != null) {
            try {
                Integer bzStatus=0;
                Integer tlfzrStatus=0;
                Integer zjyStatus=0;
                Integer zjldStatus=0;
                Integer kgStatus=0;
                Integer slfzrStatus=0;
                if(param.getCheckQXFlag()==VerifyParam.BZ)
                    bzStatus=2;
                else if(param.getCheckQXFlag()==VerifyParam.TLFZR)
                    tlfzrStatus=2;
                else if(param.getCheckQXFlag()==VerifyParam.ZJY)
                    zjyStatus=2;
                else if(param.getCheckQXFlag()==VerifyParam.ZJLD)
                    zjldStatus=2;
                else if(param.getCheckQXFlag()==VerifyParam.KG)
                    kgStatus=2;
                else if(param.getCheckQXFlag()==VerifyParam.SLFZR)
                    slfzrStatus=2;

                BcpTkdBean bcpTkd=new BcpTkdBean();
                bcpTkd.setBackDh(param.getDh());
                bcpTkd.setBzStatus(bzStatus);
                bcpTkd.setTlfzrStatus(tlfzrStatus);
                bcpTkd.setZjyStatus(zjyStatus);
                bcpTkd.setZjldStatus(zjldStatus);
                bcpTkd.setKgStatus(kgStatus);
                bcpTkd.setSlfzrStatus(slfzrStatus);

                int a = mainService.refuseBcpTk(bcpTkd);
                if (a == 1) {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
                } else {
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "审核失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "传参异常");
    }

    /**
     * @return
     * @author 逄坤
     * @desc 获取物料入库质检信息（包括入库单详情和关联的每一条记录的信息）
     */
    @RequestMapping(value = "/getWlInQualityCheckData", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult getWlInQualityCheckData(String json) {
        VerifyParam param = ParamsUtils.handleParams(json, VerifyParam.class);
        ActionResult<WlInQualityCheckResult> result = new ActionResult<WlInQualityCheckResult>();
        if (param != null) {
            try {
                WlInQualityCheckResult dataResult = new WlInQualityCheckResult();
                WlRkdBean rkdBean = mainService.getWlRkdBean(param.getDh());
                dataResult.setFhDw(rkdBean.getJhDw());
                dataResult.setInDh(rkdBean.getInDh());
                dataResult.setShFzr(rkdBean.getShFzr());
                dataResult.setShRq(rkdBean.getShrq());
                dataResult.setRemark(rkdBean.getRemark());
                List<WLINShowBean> wlinDataList = mainService.getWLINShowBeanListByInDh(param.getDh());
                dataResult.setBeans(wlinDataList);
                result.setResult(dataResult);
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "传参异常");
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 根据sort获取不同的检验信息
     */
    @RequestMapping(value = "/getQualityData", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult getQualityData(String json) {
        QualityDataParam param = ParamsUtils.handleParams(json, QualityDataParam.class);
        ActionResult<QualityDataResult> result = new ActionResult<QualityDataResult>();
        if (param != null) {
            try {
                QualityDataResult data = new QualityDataResult();
                switch (param.getSort()) {
                    case TrackType.WL:
                        WlTrackResult result1 = mainService.getWlInData(param.getQrCodeId());
                        if (result1 != null) {
                            WlTrackResult b1 = result1;
                            data.setWlCode(b1.getWlCode());
                            data.setWlProductName(b1.getProductName());
                            data.setWlChd(b1.getChd());
                            data.setWlCzy(b1.getCzy());
                            data.setWlDw(b1.getDw());
                            data.setWlDwzl(b1.getDwzl());
                            data.setWlGg(b1.getGg());
                            data.setWlLlTime(b1.getLlTime());
                            data.setWlPczl(b1.getPczl());
                            data.setWlSortName(b1.getSortName());
                            data.setWlYlpc(b1.getYlpc());
                        }
                        break;
                    case TrackType.BCP:
                        List<BcpTrackResult> list2 = mainService.getBcpInShowData(param.getQrCodeId());
                        if (list2 != null && list2.size() > 0) {
                            BcpTrackResult b1 = list2.get(0);
                            data.setBcpCode(b1.getBcpCode());
                            data.setBcpProductName(b1.getProductName());
                            data.setBcpCheJian(b1.getCheJian());
                            data.setBcpCzy(b1.getCzy());
                            data.setBcpDw(b1.getDw());
                            data.setBcpDwzl(b1.getDwzl());
                            data.setBcpGg(b1.getGg());
                            data.setBcpGx(b1.getGx());
                            data.setBcpScpc(b1.getScpc());
                            data.setBcpScTime(b1.getScTime());
                            data.setBcpSortName(b1.getSortName());
                            data.setBcpYlpc(b1.getYlpc());
                        }
                        break;
                    case TrackType.SMALL_CP:
                        List<SmallCpTrackResult> list3 = mainService.getSmallCpInShowData(param.getQrCodeId());
                        if (list3 != null && list3.size() > 0) {
                            SmallCpTrackResult b1 = list3.get(0);
                            data.setCpCode(b1.getCpCode());
                            data.setCpCzy(b1.getCzy());
                            data.setCpDw(b1.getDw());
                            data.setCpDwzl(b1.getDwzl());
                            data.setCpGg(b1.getGg());
                            data.setCpName(b1.getCpName());
                            data.setCpScpc(b1.getScpc());
                            data.setCpScTime(b1.getScTime());
                            data.setCpsortName(b1.getSortName());
                            data.setCpYlpc(b1.getYlpc());
                        }
                        break;
                    case TrackType.BIG_CP:
                        List<BigCpTrackResult> list4 = mainService.getBigCpIn2ShowData(param.getQrCodeId());
                        if (list4 != null && list4.size() > 0) {
                            BigCpTrackResult b1 = list4.get(0);
                            data.setBigCpCode(b1.getCpCode());
                            data.setBigCpCzy(b1.getCzy());
                            data.setBigCpDw(b1.getDw());
                            data.setBigCpDwzl(b1.getDwzl());
                            data.setBigCpGg(b1.getGg());
                            data.setBigCpName(b1.getCpName());
                            data.setBigCpScpc(b1.getScpc());
                            data.setBigCpScTime(b1.getScTime());
                            data.setBigCpSortName(b1.getSortName());
                            data.setBigCpYlpc(b1.getYlpc());
                        }
                        break;
                }
                result.setResult(data);
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "传参异常");
    }
    /**
     * @return
     * @author 马鹏昊
     * @desc 通过审核
     */
    @RequestMapping(value = "/passCheck", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult passCheck(String json) {
        QualityDataParam param = ParamsUtils.handleParams(json, QualityDataParam.class);
        ActionResult<ActionResult> result = new ActionResult<ActionResult>();
        if (param != null) {
            try {
                int b = -1;
                switch (param.getSort()) {
                    case TrackType.WL:
                        b = mainService.changeWLInPassCheckFlag(param.getQrCodeId(),param.getZjy());
                        break;
                    case TrackType.BCP:
                        //b = mainService.changeBCPInPassCheckFlag(param.getQrCodeId(),param.getZjy());
                        break;
                    case TrackType.SMALL_CP:
                        b = mainService.changeSMALL_CPInPassCheckFlag(param.getQrCodeId(),param.getZjy());
                        break;
                    case TrackType.BIG_CP:
                        //b = mainService.changeBIG_CPInPassCheckFlag(param.getQrCodeId(),param.getZjy());
                        break;
                }
                if (b<0){
                    result = ActionResultUtils.setResultMsg(result,ActionResult.STATUS_MESSAGE_ERROR,"修改质检状态失败");
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
                }
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "传参异常");
    }
    /**
     * @return
     * @author 马鹏昊
     * @desc 修改审核数据——物料入库
     */
    @RequestMapping(value = "/updateWlInData", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult updateWlInData(String json) {
        WlInVerifyResult param = ParamsUtils.handleListParams(json, WlInVerifyResult.class,"beans",WLINShowBean.class);
        ActionResult<ActionResult> result = new ActionResult<ActionResult>();
        if (param != null) {
            try {
                int b = mainService.updateWlRkdData(param);
                if (b<0){
                    result = ActionResultUtils.setResultMsg(result,ActionResult.STATUS_MESSAGE_ERROR,"修改物料入库单数据失败");
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
                }else {
                    List<WLINShowBean> beans = param.getBeans();
                    for (int i = 0; i < beans.size(); i++) {
                        b =  mainService.updateWlInData(beans.get(i));
                        if (b<0){
                            result = ActionResultUtils.setResultMsg(result,ActionResult.STATUS_MESSAGE_ERROR,"修改物料入库数据失败");
                            return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
                        }
                    }
                }
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "传参异常");
    }
    /**
     * @return
     * @author 马鹏昊
     * @desc 修改审核数据——物料出库
     */
    @RequestMapping(value = "/updateWlOutData", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult updateWlOutData(String json) {
        WlOutVerifyResult param = ParamsUtils.handleListParams(json, WlOutVerifyResult.class,"beans",WLOutShowBean.class);
        ActionResult<ActionResult> result = new ActionResult<ActionResult>();
        if (param != null) {
            try {
                int b = mainService.updateWlCkdData(param);
                if (b<0){
                    result = ActionResultUtils.setResultMsg(result,ActionResult.STATUS_MESSAGE_ERROR,"修改物料出库单数据失败");
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
                }else {
                    List<WLOutShowBean> beans = param.getBeans();
                    for (int i = 0; i < beans.size(); i++) {
                        b =  mainService.updateWlOutData(beans.get(i));
                        if (b<0){
                            result = ActionResultUtils.setResultMsg(result,ActionResult.STATUS_MESSAGE_ERROR,"修改物料出库数据失败");
                            return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
                        }
                    }
                }
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "传参异常");
    }
    /**
     * @return
     * @author 马鹏昊
     * @desc 修改审核数据——物料出库
     */
    @RequestMapping(value = "/updateWlTkData", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult updateWlTkData(String json) {
        WlTkVerifyResult param = ParamsUtils.handleListParams(json, WlTkVerifyResult.class,"beans",WLTkShowBean.class);
        ActionResult<ActionResult> result = new ActionResult<ActionResult>();
        if (param != null) {
            try {
                int b = mainService.updateWlTkdData(param);
                if (b<0){
                    result = ActionResultUtils.setResultMsg(result,ActionResult.STATUS_MESSAGE_ERROR,"修改物料退库单数据失败");
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
                }else {
                    List<WLTkShowBean> beans = param.getBeans();
                    for (int i = 0; i < beans.size(); i++) {
                        b =  mainService.updateWlTkData(beans.get(i));
                        if (b<0){
                            result = ActionResultUtils.setResultMsg(result,ActionResult.STATUS_MESSAGE_ERROR,"修改物料退库数据失败");
                            return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
                        }
                    }
                }
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "传参异常");
    }
    /**
     * @return
     * @author 马鹏昊
     * @desc 修改审核数据——半成品/成品入库
     */
    @RequestMapping(value = "/updateBcpInData", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult updateBcpInData(String json) {
        BcpInVerifyResult param = ParamsUtils.handleListParams(json, BcpInVerifyResult.class,"beans",BcpInShowBean.class);
        ActionResult<ActionResult> result = new ActionResult<ActionResult>();
        if (param != null) {
            try {
                int b = mainService.updateBcpRkdData(param);
                if (b<0){
                    result = ActionResultUtils.setResultMsg(result,ActionResult.STATUS_MESSAGE_ERROR,"修改半成品入库单数据失败");
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
                }else {
                    List<BcpInShowBean> beans = param.getBeans();
                    for (int i = 0; i < beans.size(); i++) {
                        b =  mainService.updateBcpInData(beans.get(i));
                        b =  mainService.updateSmallCpInData(beans.get(i));
                        b =  mainService.updateBigCpInData(beans.get(i));
                    }
                }
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "传参异常");
    }
    /**
     * @return
     * @author 马鹏昊
     * @desc 修改审核数据——半成品/成品入库
     */
    @RequestMapping(value = "/updateCpOutData", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult updateCpOutData(String json) {
        CpOutVerifyResult param = ParamsUtils.handleListParams(json, CpOutVerifyResult.class,"beans",CpOutShowBean.class);
        ActionResult<ActionResult> result = new ActionResult<ActionResult>();
        if (param != null) {
            try {
                int b = mainService.updateCpCkdData(param);
                if (b<0){
                    result = ActionResultUtils.setResultMsg(result,ActionResult.STATUS_MESSAGE_ERROR,"修改成品出库单数据失败");
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
                }
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "传参异常");
    }
    /**
     * @return
     * @author 马鹏昊
     * @desc 修改审核数据——半成品/成品入库
     */
    @RequestMapping(value = "/updateBcpTkData", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult updateBcpTkData(String json) {
        BcpTkVerifyResult param = ParamsUtils.handleListParams(json, BcpTkVerifyResult.class,"beans",BcpTkShowBean.class);
        ActionResult<ActionResult> result = new ActionResult<ActionResult>();
        if (param != null) {
            try {
                int b = mainService.updateBcpTkdData(param);
                if (b<0){
                    result = ActionResultUtils.setResultMsg(result,ActionResult.STATUS_MESSAGE_ERROR,"修改半成品退库单数据失败");
                    return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
                }else {
                    List<BcpTkShowBean> beans = param.getBeans();
                    for (int i = 0; i < beans.size(); i++) {
                        b =  mainService.updateBcpTkData(beans.get(i));
                    }
                }
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "传参异常");
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 获取所有人员信息
     */
    @RequestMapping(value = "/getAllPerson", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult getAllPerson(String json) {
        User param = ParamsUtils.handleParams(json, User.class);

        ActionResult<PersonResult> result = new ActionResult<PersonResult>();
        try {
            PersonResult personResult = mainService.getAllPerson(param);
            if (personResult.getPersonBeans() == null || personResult.getPersonBeans().size() <= 0) {
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_MESSAGE_ERROR, "无人员信息");
            }
            result.setResult(personResult);
            return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "获取人员信息成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
        }
    }

    /**
     * 逄坤
     * 获取所有人员信息（无筛选条件）
     * @return
     */
    @RequestMapping(value = "/searchAllPerson", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult searchAllPerson() {
        ActionResult<PersonResult> result = new ActionResult<PersonResult>();
        try {
            PersonResult personResult = mainService.searchAllPerson();
            if (personResult.getPersonBeans() == null || personResult.getPersonBeans().size() <= 0) {
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_MESSAGE_ERROR, "无人员信息");
            }
            result.setResult(personResult);
            return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "获取人员信息成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
        }
    }

    /**
     * 逄坤
     * 根据员工Id查询员工信息
     * @param json
     * @return
     */
    @RequestMapping(value = "/getPersonById", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult getPersonById(String json){
        User param = ParamsUtils.handleParams(json, User.class);

        ActionResult<PersonResult> result = new ActionResult<PersonResult>();
        try {
            PersonResult personResult = mainService.getPersonById(param.getUserID());
            if (personResult == null) {
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_MESSAGE_ERROR, "无人员信息");
            }
            String qxGroup = personResult.getCheckQXGroup()+",";
            List<Module2Bean> qxList = mainService.getXZQXData().getBeans();
            String qxNames="";
            for(Module2Bean qx : qxList){
                if(qxGroup.contains(qx.getId()+",")){
                    qxNames+=qx.getName()+",";
                }
            }
            personResult.setQxNameGroup(qxNames.substring(0,qxNames.length()-1));
            result.setResult(personResult);
            return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "获取人员信息成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
        }
    }

    /**
     * @return
     * @author 马鹏昊
     * @desc 获取可修改库单数据
     */
    @RequestMapping(value = "/getCanModifyData", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult getCanModifyData(String json) {
        MainParams param = ParamsUtils.handleParams(json, MainParams.class);
        ActionResult<NonCheckResult> result = new ActionResult<NonCheckResult>();
        if (param != null) {
            try {
                NonCheckResult dataResult = new NonCheckResult();
                List<NonCheckBean> allBeans = new ArrayList<NonCheckBean>();
                List<WlRkdBean> wlRkNonCheckData = mainService.getWlRkCanModifyData(param.getRealName());
                for (int i = 0; i < wlRkNonCheckData.size(); i++) {
                    NonCheckBean bean = new NonCheckBean();
                    WlRkdBean single = wlRkNonCheckData.get(i);
                    bean.setDh(single.getInDh());
                    bean.setName("物料入库单");
                    bean.setTime(single.getShrq());
                    allBeans.add(bean);
                }
                List<WlCkdBean> wlCkNonCheckData = mainService.getWlCkCanModifyData(param.getRealName());
                for (int i = 0; i < wlCkNonCheckData.size(); i++) {
                    NonCheckBean bean = new NonCheckBean();
                    WlCkdBean single = wlCkNonCheckData.get(i);
                    bean.setDh(single.getOutDh());
                    bean.setName("物料出库单");
                    bean.setTime(single.getLhRq());
                    allBeans.add(bean);
                }
                List<WlTkdBean> wlTkNonCheckData = mainService.getWlTkCanModifyData(param.getRealName());
                for (int i = 0; i < wlTkNonCheckData.size(); i++) {
                    NonCheckBean bean = new NonCheckBean();
                    WlTkdBean single = wlTkNonCheckData.get(i);
                    bean.setDh(single.getBackDh());
                    bean.setName("物料退库单");
                    bean.setTime(single.getThRq());
                    allBeans.add(bean);
                }
                List<BcpRkdBean> bcpRkNonCheckData = mainService.getBcpRkCanModifyData(param.getRealName());
                for (int i = 0; i < bcpRkNonCheckData.size(); i++) {
                    NonCheckBean bean = new NonCheckBean();
                    BcpRkdBean single = bcpRkNonCheckData.get(i);
                    bean.setDh(single.getInDh());
                    bean.setName("半成品录入单");
                    bean.setTime(single.getShrq());
                    allBeans.add(bean);
                }
                List<BcpRkdBean> cpRkNonCheckData = mainService.getCpRkCanModifyData(param.getRealName());
                for (int i = 0; i < cpRkNonCheckData.size(); i++) {
                    NonCheckBean bean = new NonCheckBean();
                    BcpRkdBean single = cpRkNonCheckData.get(i);
                    bean.setDh(single.getInDh());
                    bean.setName("成品入库单");
                    bean.setTime(single.getShrq());
                    allBeans.add(bean);
                }
                List<BcpCkdBean> bcpCkNonCheckData = mainService.getBcpCkCanModifyData(param.getRealName());
                for (int i = 0; i < bcpCkNonCheckData.size(); i++) {
                    NonCheckBean bean = new NonCheckBean();
                    BcpCkdBean single = bcpCkNonCheckData.get(i);
                    bean.setDh(single.getOutDh());
                    bean.setName("成品出库单");
                    bean.setTime(single.getLhRq());
                    allBeans.add(bean);
                }
                List<BcpTkdBean> bcpTkNonCheckData = mainService.getBcpTkCanModifyData(param.getRealName());
                for (int i = 0; i < bcpTkNonCheckData.size(); i++) {
                    NonCheckBean bean = new NonCheckBean();
                    BcpTkdBean single = bcpTkNonCheckData.get(i);
                    bean.setDh(single.getBackDh());
                    bean.setName("半成品入库（退库）单");
                    bean.setTime(single.getThRq());
                    allBeans.add(bean);
                }
                for (int i = 0; i < allBeans.size() - 1; i++) {
                    for (int j = 0; j < allBeans.size() - 1 - i; j++)// j开始等于0，
                    {
                        String beginTime = allBeans.get(j).getTime();
                        String endTime = allBeans.get(j + 1).getTime();
                        if (beginTime.compareTo(endTime) < 0) {
                            NonCheckBean smallBean = allBeans.get(j);
                            NonCheckBean bigBean = allBeans.get(j + 1);
                            allBeans.set(j, bigBean);
                            allBeans.set(j + 1, smallBean);
                        }
                    }
                }
                dataResult.setBeans(allBeans);
                result.setResult(dataResult);
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_SUCCEED, "成功");
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_EXCEPTION, "系统异常");
            }
        }
        return ActionResultUtils.setResultMsg(result, ActionResult.STATUS_PARAMS_ERROR, "传参异常");
    }
}