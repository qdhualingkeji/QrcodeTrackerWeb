package com.qdhualing.qrcodetracker.dao;

import java.util.List;

import com.qdhualing.qrcodetracker.bean.*;
import com.qdhualing.qrcodetracker.model.User;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Administrator on 2018/1/29.
 */

public interface MainDao {

    //获取物料入库所需录入数据
    DataResult getMaterialInData(String qrcodeId);
    //插入入库单的物料委托信息
    int createWLWT_RKD(CreateWLRKDParam rkdpParams);
    //插入入库单的物料信息
    int createWL_RKD(CreateWLRKDParam rkdpParams);
    //插入物料入库信息
    int createWLIN_M(WLINParam wlinParam);
    //根据入库单号查询入库信息
    CreateWLRKDParam getCreateRKDParamByInDh(String InDh);
    //根据入库单号删除对应的入库信息
    int delWLIn_M(String InDh);
    //根据入库单号删除对应的入库单委托信息
    int delWLWT_RKD(String InDh);
    //根据入库单号删除对应的入库单信息
    int delWL_RKD(String InDh);
    //根据入库单号查询物料入库记录(可能为多条)
    List<WLINParam> getWLINParamListByInDh(String InDh);
    //根据二维码编号查询物料入库存量的对应记录
    WLINParam getWLSParamByQRCode(String QRCode_ID);
    //根据二维码编号将物料存量进行更新
    int updataWLINParamByQRCode(double PCZL,String QRCode_ID);
    //物料入库信息提交
    int commitMaterialInputedData(DataInputParams params);
    //获取物料类别数据
    List<PdtSortBean> getPdtSort();
    //获取父级物料分类数据
    List<HlSortBean> getParentHlSort(@Param("qrCodeId") String qrCodeId);
    //获取子级物料分类数据
    List<HlSortBean> getChildHlSort(@Param("parentID") int parentID,@Param("qrCodeId") String qrCodeId);
    //获取产品数据
    List<HlProductBean> getHlProduct(int sortID);
    //创建物料出库单
    int createWL_CKD(CreateWLCKDParam ckdParam);
    //插入物料委托表记录
    int createWLWT_CKD(CreateWLCKDParam ckdParam);
    //物料出库获取已知数据用于显示不可修改
    WLOutShowDataResult getWLSData(String qrcodeId);
    //半成品出库获取已知数据用于显示不可修改
    BCPOutShowDataResult getBCPSData(String qrcodeId);
    //查询物料库存表是否已存在记录
    Integer queryWLS(String s);
    //插入物料库存表记录
    int insertWLS(WLINParam wlinParam);
    //物料入库修改库存表数据（数量加）
    int updateWLS(WLINParam wlinParam);
    //select半成品库存表记录
    BcpSBean findBcpS(String qrCodeId);
    //select库存表记录
    WLSBean findWLS(String qrCodeId);
    //插入物料出库记录表记录
    int insertWLOUT(WLOutParam wlOutParam);
    //插入半成品出库记录表记录
    int insertBcpOUT(BcpOutParam bcpOutParam);
    //物料出库修改库存表记录（数量减）
    int outUpdateWLS(WLOutParam wlOutParam);
    //半成品出库修改库存表记录（数量减）
    int outUpdateBCPS(BcpOutParam bcpOutParam);
    //查找出库单数据
    CKDWLBean findWL_CKD(String outDh);
    //获取部门数据
    List<UserGroupBean> getUserGroupData();
    //插入临时库存表
    int insertWLTempS(WLOutParam wlOutParam);
    //查找临时库存表中有没有该条数据
    int findWLTempS(String qrCodeId);
    //更新物料临时库存表
    int updateWLTempS(WLOutParam wlOutParam);
    //创建物料退库单
    int createWL_TKD(CreateWLTKDParam tkdParam);
    //获取退库时需要显示的数据
    WLTKShowDataResult getWLTempSData(String qrcodeId);
    //获取指定退库单信息
    TKDWLBean findWL_TKD(String outDh);
    //获取物料临时库存表数据
    WLTempSBean getWLTempS(String qrCodeId);
    //插入物料出库信息
    int insertWLBk(WLTKParam wlTKParam);
    //根据退库参数更新物料库存表
    int updateWLSByTk(WLTKParam wlTKParam);
    //删除物料临时库存表数据
    int deleteFromWLTempS(String qrCodeId);
    //根据退库参数更新物料临时库存表数据
    int updateWLTempSByTk(WLTKParam wlTKParam);
    //插入物料投料表数据
    int insertWLTl(WLThrowParam wlTLParam);
    //根据物料投料参数更新物料物料临时库存表数据
    int updateWLTempSByTl(WLThrowParam wlTLParam);
    //获取工序
    List<GXBean> getGXData(String[] cjIdArray);
    //获取车间
    List<CJBean> getCJData();
    //创建半成品入库单
    int createBCP_RKD(CreateBCPRKDParam rkdParam);
    //通过工序id获取物料投料表的数据（获取已经投到此工序上的原料）
    List<TLYLBean> getWLTLDataByGxId(int gxId);
    //通过工序id获取半成品投料表的数据（获取已经投到此工序上的半成品）
    List<TLYLBean> getBCPTLDataByGxId(int gxId);
    //获取物料投料表数据数量
    int getWLTLDataCount(WLThrowParam wlTLParam);
    //根据物料投料参数更新物料投料表数据
    int updateWLTl(WLThrowParam wlTLParam);
    //插入半成品入库记录
    int insertBCPIn(BCPINParam bcpInParam);
    //查找是否有半成品临时库存记录
    int findBCPTempS(String qrCodeID);
    //插入半成品临时库存表
    int insertBCPTempS(BCPINParam bcpInParam);
    //更新半成品临时库存表
    int updateBCPTempS(BCPINParam bcpInParam);
    //半成品投料操作获取半成品投料显示数据
    BcpThrowShowDataResult getBcpTempSData(String qrcodeId);
    //半成品投料操作获取办成品临时库存表
    BCPTempSBean getBcpTempS(String qrcodeId);
    //半成品投料操作获取半成品投料表的记录数量
    int getBcpTLDataCount(BcpThrowParam bcpTLParam);
    //半成品投料操作插入半成品投料记录
    int insertBcpTl(BcpThrowParam bcpTLParam);
    //半成品投料操作更新半成品投料表记录
    int updateBcpTl(BcpThrowParam bcpTLParam);
    //删除半成品临时库存表记录
    int deleteFromBcpTempS(String qrcodeId);
    //根据半成品投料参数更新半成品临时库存表数据
    int updateBcpTempSByTl(BcpThrowParam bcpTLParam);
    //创建半成品退库单
    int createBCP_TKD(CreateBCPTKDParam param);
    //获取半成品退库显示数据
    BCPTKShowDataResult getBCPTKShowData(String qrcodeId);
    //获取半成品退库单数据
    TKDBCPBean getTKDBCPBean(String backDh);
    //插入半成品退库记录
    int insertBCPBk(BCPTKParam param);
    //查找半成品库存表中的该二维码id记录的数量
    int getBCPSCount(String qrCodeId);
    //插入半成品库存表记录
    int insertBCPS(BCPTKParam param);
    //通过退库参数更新半成品库存表
    int updateBCPSByTk(BCPTKParam param);
    //通过退库参数更新半成品临时库存表
    int updateBCPTempSByBCPTk(BCPTKParam param);
    //创建半成品/成品出库单
    int createBCP_CKD(CreateBCPCKDParam param);
    //插入大包装记录表
    int insertCPIn2(BigCPINParam inParam);
    //查找大包装是否已经存在记录了
    int findCPS2(String qrCodeId);
    //插入大包装库存表
    int insertCPS2(BigCPINParam inParam);
    //获取大包装数据
    List<BigCpBean> getBigCpData();
    //查找小包装库存有没有记录
    int findCPS(String qrCodeId);
    //插入小包装库存表
    int insertCPS(SmallCPINParam inParam);
    //更新小包装库存表
    int updateCPS(SmallCPINParam inParam);
    //插入成品入库记录表
    int insertCPIn(SmallCPINParam inParam);
    //更新大包装库存
    int updateCPS2(BigCpBean bigCpBean);
    //获取大包装信息
    BigCpBean getCPS2(String bigCpQrId);
    //更新大包装入库记录
    int updateCPIn2(BigCpBean bigCpBean);
    //插入成品入库记录
    int insertCPOut(BigCpOutParam param);
    //删除大包装库存数据
    int deleteCPS2ByQrId(String qrCodeId);
    //删除大包装关联的小包装库存数据
    int deleteCPSByCps2QrId(String qrCodeId);
    //获取大包装出库显示数据
    BigCpOutGetDataResult getCP2ShowData(String qrCodeId);
    //获取小包装出库显示数据
    SmallCpOutGetDataResult getSmallCpOutData(String qrCodeId);
    //获取小包装信息
    SmallCpBean getCPS(String qrCodeId);
    //删除小包装库存数据
    int deleteCPSByQrId(String qrCodeId);
    //根据小包装出库参数插入成品入库记录
    int insertCPOutBySmallParam(SmallCpOutParam param);
    //查询物料追溯信息
    WlTrackResult getWlInData(String qrcodeId);
    //查询半成品入库记录表
    List<BCPINParam> getBcpInData(String qrCodeId);
    //根据ID获取分类名字
    String getHlSortBySortId(String id);
    //获取组成成分(半成品)
    List<ComponentBean> getComponentBeansFromBcp(List<String> ylList);
    //获取组成成分(物料)
    List<ComponentBean> getComponentBeansFromWl(List<String> ylList);
    //获取小包装入库记录
    List<CPINParam> getSmallCpInData(String qrCodeId);
    //获取大包装记录信息
    List<BigCpBean> getBigCpIn2(String qrCodeId);

    String getPersonFromWlRkd(String dh,@Param("personFlag") int personFlag);

    String getPersonFromWlCkd(String dh,@Param("personFlag") int personFlag);

    String getPersonFromWlTkd(String dh,@Param("personFlag") int personFlag);

    String getPersonFromBcpRkd(String dh,@Param("personFlag") int personFlag);

    String getPersonFromBcpTkd(String dh,@Param("personFlag") int personFlag);

    String getPersonFromBcpCkd(String dh,@Param("personFlag") int personFlag);

    List<WlRkdBean> getWlRkNonCheckData(WlRkdBean wlrkd);

    List<WlCkdBean> getWlCkNonCheckData(WlCkdBean wlCkd);

    List<WlTkdBean> getWlTkNonCheckData(WlTkdBean wltkd);

    List<BcpRkdBean> getBcpRkNonCheckData(BcpRkdBean bcprkd);

    List<BcpRkdBean> getCpRkNonCheckData(BcpRkdBean bcprkd);

    List<BcpCkdBean> getBcpCkNonCheckData(BcpCkdBean bcpCkdBean);

    List<BcpCkdBean> getCpCkNonCheckData(BcpCkdBean bcpCkdBean);

    List<BcpTkdBean> getBcpTkNonCheckData(BcpTkdBean bcptkd);

    WlRkdBean getWlRkdBean(String dh);

    List<WLINShowBean> getWLINShowBeanListByInDh(String dh);

    WlCkdBean getWlCkdBean(String dh);

    List<WLOutShowBean> getWLOutShowBeanListByOutDh(String dh);

    List<WLOutParam> getWLOutParamListByOutDh(String dh);

    List<BcpOutParam> getBcpOutParamListByOutDh(String dh);

    WlTkdBean getWlTkdBean(String dh);

    List<WLTkShowBean> getWLTkShowBeanListByOutDh(String dh);

    List<WLTKParam> getWLTKParamListByOutDh(String dh);

    List<BCPTKParam> getBCPTKParamListByOutDh(String dh);

    BcpRkdBean getBcpRkdBean(String dh);

    List<BcpInShowBean> getBcpInShowBeanListByInDh(String dh);

    List<BcpInShowBean> getCpInShowBeanListByInDh(String dh);

    List<BcpInShowBean> getCpInShowBeanListByCPS2QRCode(String cPS2QRCode);

    List<BcpInShowBean> getBigCpInShowBeanListByInDh(String dh);

    BcpCkdBean getBcpCkdBean(String dh);

    List<BcpOutShowBean> getBcpOutShowBeanListByOutDh(String dh);

    List<CpOutShowBean> getCpOutShowBeanListByOutDh(String dh);

    BcpTkdBean getBcpTkdBean(String dh);

    List<BcpTkShowBean> getBcpTkShowBeanListByBackDh(String dh);

    int agreeWlIn(WlRkdBean wlrkd);

    int refuseWlIn(WlRkdBean wlrkd);

    int agreeWlOut(WlCkdBean wlckd);

    int refuseWlOut(WlCkdBean wlckd);

    int agreeWlTk(WlTkdBean wltkd);

    int refuseWlTk(WlTkdBean wltkd);

    int agreeBcpIn(BcpRkdBean bcpRkd);

    int refuseBcpIn(BcpRkdBean bcpRkd);

    int agreeBcpOut(BcpCkdBean bcpckd);

    int agreeCpOut(BcpCkdBean bcpckd);

    int refuseBcpOut(BcpCkdBean bcpCkd);

    int refuseCpOut(BcpCkdBean bcpCkd);

    int agreeBcpTk(BcpTkdBean bcpTkd);

    int refuseBcpTk(BcpTkdBean bcpTkd);

    List<BcpTrackResult> getBcpInShowData(String qrCodeId);

    List<SmallCpTrackResult> getSmallCpInShowData(String qrCodeId);

    List<BigCpTrackResult> getBigCpIn2ShowData(String qrCodeId);

    int changeWLInPassCheckFlag(String qrCodeId, String zjy);

    int changeBCPInPassCheckFlag(String qrCodeId, String zjy, Integer zjzt);

    int changeSMALL_CPInPassCheckFlag(String qrCodeId, String zjy);

    int changeBIG_CPInPassCheckFlag(String qrCodeId, String zjy, Integer zjzt);

    List<WlRkdBean> getWlRkNonPassCheckDataByCzy(String realName);

    List<WlCkdBean> getWlCkNonPassCheckDataByCzy(String realName);

    List<WlTkdBean> getWlTkNonPassCheckDataByCzy(String realName);

    List<BcpRkdBean> getBcpRkNonPassCheckDataByCzy(String realName);

    List<BcpCkdBean> getBcpCkNonPassCheckDataByCzy(String realName);

    List<BcpTkdBean> getBcpTkNonPassCheckDataByCzy(String realName);

    int updateWlInData(WLINShowBean param);

    int updateWlRkdData(WlInVerifyResult param);

    int updateWlCkdData(WlOutVerifyResult param);

    int updateWlOutData(WLOutShowBean wlOutShowBean);

    int updateBcpOutData(BcpOutShowBean bcpOutShowBean);

    int updateWlTkdData(WlTkVerifyResult param);

    int updateWlTkData(WLTkShowBean wlTkShowBean);

    int updateBcpRkdData(BcpInVerifyResult param);

    int updateBcpInData(BcpInShowBean bcpInShowBean);

    int updateSmallCpInData(BcpInShowBean bcpInShowBean);

    int updateBigCpInData(BcpInShowBean bcpInShowBean);

    int updateBcpTkdData(BcpTkVerifyResult param);

    int updateBcpTkData(BcpTkShowBean bcpTkShowBean);

    int updateBcpCkdData(BcpOutVerifyResult param);

    int updateCpCkdData(CpOutVerifyResult param);

    int updateWLIN_M(WLINParam wlinParam);

    int createWL_RKD_New(CreateWLRKDParam rkdpParams);

    List<PersonBean> getAllPerson(User param);

    List<PersonBean> searchAllPerson();

    int updateBcpIn(BCPINParam bcpInParam);

    List<WlRkdBean> getWlRkCanModifyData(String realName);

    List<WlCkdBean> getWlCkCanModifyData(String realName);

    List<WlTkdBean> getWlTkCanModifyData(String realName);

    List<BcpRkdBean> getBcpRkCanModifyData(String realName);

    List<BcpRkdBean> getCpRkCanModifyData(String realName);

    List<BcpCkdBean> getBcpCkCanModifyData(String realName);

    List<BcpCkdBean> getCpCkCanModifyData(String realName);

    List<BcpTkdBean> getBcpTkCanModifyData(String realName);

    int updateCPIn(SmallCPINParam inParam);

    int updateCPIn2ByParam(BigCPINParam inParam);

    List<BigCPINParam> getBigCPINParamListByInDh(String dh);

    List<SmallCPINParam> getSmallCPINParamListByInDh(String dh);

    List<BigCpOutParam> getBigCpOutParamListByOutDh(String dh);

    List<SmallCPINParam> getSmallCPINParamListByCPS2QRCode(String qrCodeId);

    int updateBcpRkdStatusByQRCodeID(String qrCodeId);

    List<Module2Bean> getXZQXData();

    List<Module2Bean> getXZQXDataByShenFen(PersonParam param);

    int registerUser(PersonParam personParam);

    PersonResult getPersonById(int userID);

    int updateUserData(PersonResult personResult);

    /**
     * 验证账号是否存在，用于注册和修改用户时验证
     * @param personParam
     * @return
     */
    int checkLoginName(PersonParam personParam);

    int deleteUser(int userId);

    int checkExistByQrCodeId(@Param("qrCodeId") String qrCodeId, @Param("currentFunctionType") Integer currentFunctionType);

    int deleteFromWLS(String qrCodeId);

    WLThrowShowDataResult getWLTl(WLThrowParam wlThrowParam);

    BcpThrowShowDataResult getBCPTl(BcpThrowParam bcpThrowParam);

    int deleteFromWLTl(WLThrowParam wlThrowParam);

    int deleteFromBCPTl(BcpThrowParam bcpThrowParam);

    int deleteFromBCPS(String qrCodeId);

    int updateWLTlByBcpIn(WLThrowParam wlThrowParam);

    int updateBCPTlByBcpIn(BcpThrowParam bcpThrowParam);

    int updateBigCpSYZL(float dwzl, String qrCodeId);
}
