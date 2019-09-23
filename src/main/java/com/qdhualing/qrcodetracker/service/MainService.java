package com.qdhualing.qrcodetracker.service;

import com.qdhualing.qrcodetracker.bean.*;
import com.qdhualing.qrcodetracker.bean.CreateWLRKDParam;
import com.qdhualing.qrcodetracker.dao.MainDao;
import com.qdhualing.qrcodetracker.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/29.
 */
@Service
public class MainService {

    @Autowired
    private MainDao mainDao;

	public DataResult getMaterialInInputedData(GetNeedInputedDataParams params) {

        DataResult result = mainDao.getMaterialInData(params.getId());
        return result;

    }
    //保存物料的入库单信息
    public int createWL_RKD(CreateWLRKDParam rkdpParams) {
    	int a=mainDao.createWL_RKD(rkdpParams);
    	if(a==1) {
    		return 1;
    	}
    	return 0;
    }
    //保存物料委托的入库单信息
    public int createWLWT_RKD(CreateWLRKDParam rkdpParams) {
    	int b=mainDao.createWLWT_RKD(rkdpParams);
    	if(b==1) {
    		return 1;
    	}
    	return 0;
    }
    //插入物料入库信息
    public int createWLIN_M(WLINParam wlinParam) {
    	wlinParam.setBz(1);
    	int b=mainDao.createWLIN_M(wlinParam);
    	if(b==1) {
    		return 1;
    	}
		return 0;
    }
    //根据入库单号删除对应的物料入库信息
    public int deletWLIN_m(WLINParam wlinParam) {
    	
		return 0;
    }
    //根据单号去查询物料入库信息
    public int getCreateRKDParamByInDh(String InDh) {
    	CreateWLRKDParam param=mainDao.getCreateRKDParamByInDh(InDh);
    	if(param!=null) {
    		return 1;
    	}
		return 0;
    };
    
    //根据入库单号查询物料入库信息
    public List<WLINParam> getWLINParamListByInDh(String InDh){
    	List<WLINParam> list=mainDao.getWLINParamListByInDh(InDh);
    	if(list.size()<=0) {
    		return list;
    	}
		return list;
    }
    //根据二维码编号查询物料入库存量的对应记录
    public WLINParam getWLSParamByQRCode(String QRCode_ID) {
    	WLINParam wlinParam=mainDao.getWLSParamByQRCode(QRCode_ID);
    	if(wlinParam!=null) {
    		return wlinParam;
    	}
		return wlinParam;
    };
    //根据二维码编号将数据更新到物料入库存量的对应记录中
    public int updataWLINParamByQRCode(double PCZL,String QRCode_ID) {
    	int a=mainDao.updataWLINParamByQRCode(PCZL, QRCode_ID);
    	if(a==1) {
    		return a;
    	}
    	return a;
    }
    
    public int commitMaterialInputedData(DataInputParams params) {
        return mainDao.commitMaterialInputedData(params);
    }

	public PdtSortResult getPdtSort() {
    	List<PdtSortBean> beans =  mainDao.getPdtSort();
		PdtSortResult pdtSortResult = new PdtSortResult();
		pdtSortResult.setSortBeans(beans);
		return pdtSortResult;
	}

	public HlSortResult getParentHlSort(String qrCodeId) {
		List<HlSortBean> beans =  mainDao.getParentHlSort(qrCodeId);
		HlSortResult hlSortResult = new HlSortResult();
		hlSortResult.setHlSortBeans(beans);
		return hlSortResult;
	}

	public HlSortResult getChildHlSort(int parentID,String qrCodeId) {
		List<HlSortBean> beans =  mainDao.getChildHlSort(parentID,qrCodeId);
		HlSortResult hlSortResult = new HlSortResult();
		hlSortResult.setHlSortBeans(beans);
		return hlSortResult;
	}

	public HlProductResult getHlProduct(int sortID) {
		List<HlProductBean> beans =  mainDao.getHlProduct(sortID);
		HlProductResult hlProductResult = new HlProductResult();
		hlProductResult.setHlProductBeans(beans);
		return hlProductResult;
	}

	public int createWL_CKD(CreateWLCKDParam ckdParam) {
		int a=mainDao.createWL_CKD(ckdParam);
		return a;
	}

	public int createWLWT_CKD(CreateWLCKDParam ckdParam) {
		int a=mainDao.createWLWT_CKD(ckdParam);
		return a;
	}

	public WLOutShowDataResult getWLSData(String qrcodeId) {
		WLOutShowDataResult result = null;
		result = mainDao.getWLSData(qrcodeId);
		return result;
	}

	public BCPOutShowDataResult getBCPSData(String qrcodeId) {
		BCPOutShowDataResult result = null;
		result = mainDao.getBCPSData(qrcodeId);
		return result;
	}

	public int queryWLS(String s) {
    	Integer bb = mainDao.queryWLS(s);
		return bb==null?0:bb;
	}

	public int insertWLS(WLINParam wlinParam) {
		return mainDao.insertWLS(wlinParam);
	}

	public int updateWLS(WLINParam wlinParam) {
		return mainDao.updateWLS(wlinParam);
	}

	public BcpSBean findBcpS(String qrCodeId) {
		BcpSBean bcpsBean = null;
		bcpsBean = mainDao.findBcpS(qrCodeId);
		return bcpsBean;
	}

	public WLSBean findWLS(String qrCodeId) {
    	WLSBean wlsBean = null;
    	wlsBean = mainDao.findWLS(qrCodeId);
		return wlsBean;
	}

	public int insertWLOUT(WLOutParam wlOutParam) {
		wlOutParam.setBz(1);
		int a=mainDao.insertWLOUT(wlOutParam);
		return a;
	}

	public int insertBcpOUT(BcpOutParam bcpOutParam) {
		int a=mainDao.insertBcpOUT(bcpOutParam);
		return a;
	}

	public int outUpdateWLS(WLOutParam wlOutParam) {
		return mainDao.outUpdateWLS(wlOutParam);
	}

	public int outUpdateBCPS(BcpOutParam bcpOutParam) {
		return mainDao.outUpdateBCPS(bcpOutParam);
	}

	public CKDWLBean findWL_CKD(String outDh) {
		CKDWLBean ckdwlBean = null;
		ckdwlBean = mainDao.findWL_CKD(outDh);
		return ckdwlBean;
	}

	public UserGroupResult getUserGroupData() {
		List<UserGroupBean> beans =  mainDao.getUserGroupData();
		UserGroupResult userGroupResult = new UserGroupResult();
		userGroupResult.setGroupBeanList(beans);
		return userGroupResult;
	}

	public int insertWLTempS(WLOutParam wlOutParam) {
		return mainDao.insertWLTempS(wlOutParam);
	}

	public int findWLTempS(String qrCodeId) {
		return mainDao.findWLTempS(qrCodeId);
	}

	public int updateWLTempS(WLOutParam wlOutParam) {
		return mainDao.updateWLTempS(wlOutParam);
	}

	public int createWL_TKD(CreateWLTKDParam tkdParam) {
		return mainDao.createWL_TKD(tkdParam);
	}

	public WLTKShowDataResult getWLTempSData(String qrcodeId) {
		WLTKShowDataResult result = null;
		result = mainDao.getWLTempSData(qrcodeId);
		return result;
	}

	public TKDWLBean findWL_TKD(String outDh) {
		return mainDao.findWL_TKD(outDh);
	}

	public WLTempSBean getWLTempS(String qrCodeId) {
		return mainDao.getWLTempS(qrCodeId);
	}

	public int insertWLBk(WLTKParam wlTKParam) {
		return mainDao.insertWLBk(wlTKParam);
	}

	public int updateWLSByTk(WLTKParam wlTKParam) {
		return mainDao.updateWLSByTk(wlTKParam);
	}

	public int deleteFromWLTempS(String qrCodeId) {
		return mainDao.deleteFromWLTempS(qrCodeId);
	}

	public int updateWLTempSByTk(WLTKParam wlTKParam) {
		return mainDao.updateWLTempSByTk(wlTKParam);
	}

	public int insertWLTl(WLThrowParam wlTLParam) {
		return mainDao.insertWLTl(wlTLParam);
	}

	public int updateWLTempSByTl(WLThrowParam wlTLParam) {
		return mainDao.updateWLTempSByTl(wlTLParam);
	}

	public GXResult getGXData(String[] cjIdArray) {
		List<GXBean> beans =  mainDao.getGXData(cjIdArray);
		GXResult result = new GXResult();
		result.setGxBeans(beans);
		return result;
	}

	public CJResult getCJData() {
		List<CJBean> beans =  mainDao.getCJData();
		CJResult result = new CJResult();
		result.setCjBeans(beans);
		return result;
	}

	public int createBCP_RKD(CreateBCPRKDParam rkdParam) {
		return  mainDao.createBCP_RKD(rkdParam);
	}

	public SXYLResult getSXYLData(int gxId, String trackType) {
		List<TLYLBean> bb = new ArrayList<TLYLBean>();
		List<TLYLBean> beans1 = null;
		List<TLYLBean> beans2 = null;
    	//获取该工序下的物料
		if(TrackType.BCP.equals(trackType)) {
			beans1 = mainDao.getWLTLDataByGxId(gxId);
			bb.addAll(beans1);
		}
		//获取该工序下的半成品
		beans2 =  mainDao.getBCPTLDataByGxId(gxId);
		bb.addAll(beans2);
		SXYLResult result = new SXYLResult();
		result.setTlylList(bb);
		return result;
	}

	public int getWLTLDataCount(String qrcodeId) {
    	return mainDao.getWLTLDataCount(qrcodeId);
	}

	public int updateWLTl(WLThrowParam wlTLParam) {
    	return mainDao.updateWLTl(wlTLParam);
	}

	public int insertBCPIn(BCPINParam bcpInParam) {
		return mainDao.insertBCPIn(bcpInParam);
	}

	public int findBCPTempS(String qrCodeID) {
		return mainDao.findBCPTempS(qrCodeID);
	}

	public int insertBCPTempS(BCPINParam bcpInParam) {
		return mainDao.insertBCPTempS(bcpInParam);
	}

	public int updateBCPTempS(BCPINParam bcpInParam) {
		return mainDao.updateBCPTempS(bcpInParam);
	}

	public BcpThrowShowDataResult getBcpTempSData(String qrcodeId) {
		BcpThrowShowDataResult result = null;
		result = mainDao.getBcpTempSData(qrcodeId);
		return result;
	}

	public BCPTempSBean getBcpTempS(String qrcodeId) {
		return mainDao.getBcpTempS(qrcodeId);
	}

	public int getBcpTLDataCount(String qrcodeId) {
		return mainDao.getBcpTLDataCount(qrcodeId);
	}

	public int insertBcpTl(BcpThrowParam bcpTLParam) {
		return mainDao.insertBcpTl(bcpTLParam);
	}

	public int updateBcpTl(BcpThrowParam bcpTLParam) {
		return mainDao.updateBcpTl(bcpTLParam);
	}

	public int deleteFromBcpTempS(String qrcodeId) {
		return mainDao.deleteFromBcpTempS(qrcodeId);
	}

	public int updateBcpTempSByTl(BcpThrowParam bcpTLParam) {
		return mainDao.updateBcpTempSByTl(bcpTLParam);
	}

	public int createBCP_TKD(CreateBCPTKDParam param) {
		return mainDao.createBCP_TKD(param);
	}

	public BCPTKShowDataResult getBCPTKShowData(String qrcodeId) {
		BCPTKShowDataResult result = null;
		result = mainDao.getBCPTKShowData(qrcodeId);
		return result;
	}

	public TKDBCPBean getTKDBCPBean(String backDh) {
		return mainDao.getTKDBCPBean(backDh);
	}

	public int insertBCPBk(BCPTKParam param) {
		return mainDao.insertBCPBk(param);
	}

	public int getBCPSCount(String qrCodeId) {
		return mainDao.getBCPSCount(qrCodeId);
	}

	public int insertBCPS(BCPTKParam param) {
		return mainDao.insertBCPS(param);
	}

	public int updateBCPSByTk(BCPTKParam param) {
		return mainDao.updateBCPSByTk(param);
	}

	public int updateBCPTempSByBCPTk(BCPTKParam param) {
		return mainDao.updateBCPTempSByBCPTk(param);
	}

	public int createBCP_CKD(CreateBCPCKDParam param) {
		return mainDao.createBCP_CKD(param);
	}

	public int insertCPIn2(BigCPINParam inParam) {
		return mainDao.insertCPIn2(inParam);
	}

	public int findCPS2(String qrCodeId) {
		return mainDao.findCPS2(qrCodeId);
	}

	public int insertCPS2(BigCPINParam inParam) {
		return mainDao.insertCPS2(inParam);
	}

	public BigCpResult getBigCpData() {
		List<BigCpBean> beans =  mainDao.getBigCpData();
		BigCpResult result = new BigCpResult();
		result.setBeans(beans);
		return result;
	}

	public int findCPS(String qrCodeId) {
		return mainDao.findCPS(qrCodeId);
	}

	public int insertCPS(SmallCPINParam inParam) {
		return mainDao.insertCPS(inParam);
	}

	public int updateCPS(SmallCPINParam inParam) {
		return mainDao.updateCPS(inParam);
	}

	public int insertCPIn(SmallCPINParam inParam) {
		return mainDao.insertCPIn(inParam);
	}

	public int updateCPS2(BigCpBean bigCpBean) {
		return mainDao.updateCPS2(bigCpBean);
	}

	public BigCpBean getCPS2(String bigCpQrId) {
		return mainDao.getCPS2(bigCpQrId);
	}

	public int updateCPIn2(BigCpBean bigCpBean) {
		return mainDao.updateCPIn2(bigCpBean);
	}

	public int insertCPOut(BigCpOutParam param) {
		return mainDao.insertCPOut(param);
	}

	public int deleteCPS2ByQrId(String qrCodeId) {
		return mainDao.deleteCPS2ByQrId(qrCodeId);
	}

	public int deleteCPSByCps2QrId(String qrCodeId) {
		return mainDao.deleteCPSByCps2QrId(qrCodeId);
	}

	public BigCpOutGetDataResult getCP2ShowData(String qrCodeId) {
		return mainDao.getCP2ShowData(qrCodeId);
	}

	public SmallCpOutGetDataResult getSmallCpOutData(String qrCodeId) {
		return mainDao.getSmallCpOutData(qrCodeId);
	}

	public SmallCpBean getCPS(String qrCodeId) {
		return mainDao.getCPS(qrCodeId);
	}

	public int deleteCPSByQrId(String qrCodeId) {
		return mainDao.deleteCPSByQrId(qrCodeId);
	}

	public int insertCPOutBySmallParam(SmallCpOutParam param) {
		return mainDao.insertCPOutBySmallParam(param);
	}

	public WlTrackResult getWlInData(String qrcodeId) {
		return mainDao.getWlInData(qrcodeId);
	}

	public List<BCPINParam> getBcpInData(String qrCodeId) {
		return mainDao.getBcpInData(qrCodeId);
	}

	public String getHlSortBySortId(String sortID) {
		return mainDao.getHlSortBySortId(sortID);
	}

	public List<ComponentBean> getComponentBeansFromBcp(List<String> ylList) {
		return mainDao.getComponentBeansFromBcp(ylList);
	}

	public List<ComponentBean> getComponentBeansFromWl(List<String> ylList) {
		return mainDao.getComponentBeansFromWl(ylList);
	}

	public List<CPINParam> getSmallCpInData(String qrCodeId) {
		return mainDao.getSmallCpInData(qrCodeId);
	}

	public List<BigCpBean> getBigCpIn2(String qrCodeId) {
		return mainDao.getBigCpIn2(qrCodeId);
	}

	public String getPersonFromWlRkd(NotificationParam param) {
		return mainDao.getPersonFromWlRkd(param.getDh(),param.getPersonFlag());
	}

	public String getPersonFromWlCkd(NotificationParam param) {
		return mainDao.getPersonFromWlCkd(param.getDh(),param.getPersonFlag());
	}


	public String getPersonFromWlTkd(NotificationParam param) {
		return mainDao.getPersonFromWlTkd(param.getDh(),param.getPersonFlag());
	}

	public String getPersonFromBcpRkd(NotificationParam param) {
		return mainDao.getPersonFromBcpRkd(param.getDh(),param.getPersonFlag());
	}

	public String getPersonFromBcpTkd(NotificationParam param) {
		return mainDao.getPersonFromBcpTkd(param.getDh(),param.getPersonFlag());
	}

	public String getPersonFromBcpCkd(NotificationParam param) {
		return mainDao.getPersonFromBcpCkd(param.getDh(),param.getPersonFlag());
	}

	public List<WlRkdBean> getWlRkNonCheckData(Integer fzrID,Integer zjyID,Integer zjldID) {
		WlRkdBean wlrkd=new WlRkdBean();
		wlrkd.setFzrID(fzrID);
		wlrkd.setZjyID(zjyID);
		wlrkd.setZjldID(zjldID);

		return mainDao.getWlRkNonCheckData(wlrkd);
	}

	public List<WlCkdBean> getWlCkNonCheckData(Integer kgID,Integer bzID,Integer fzrID) {
		WlCkdBean wlCkd=new WlCkdBean();
		wlCkd.setKgID(kgID);
		wlCkd.setBzID(bzID);
		wlCkd.setFzrID(fzrID);
		wlCkd.setFlfzrID(fzrID);
		wlCkd.setLlfzrID(fzrID);

		return mainDao.getWlCkNonCheckData(wlCkd);
	}

	public List<WlTkdBean> getWlTkNonCheckData(Integer bzID,Integer kgID,Integer fzrID) {
    	WlTkdBean wltkd=new WlTkdBean();
		wltkd.setBzID(bzID);
		wltkd.setFzrID(fzrID);
		wltkd.setKgID(kgID);
		wltkd.setTlfzrID(fzrID);
		wltkd.setSlfzrID(fzrID);

		return mainDao.getWlTkNonCheckData(wltkd);
	}

	public List<BcpRkdBean> getBcpRkNonCheckData(Integer bzID,Integer fzrID,Integer zjyID,Integer zjldID) {
		BcpRkdBean bcprkd=new BcpRkdBean();
		bcprkd.setBzID(bzID);
		bcprkd.setFzrID(fzrID);
		bcprkd.setZjyID(zjyID);
		bcprkd.setZjldID(zjldID);

		return mainDao.getBcpRkNonCheckData(bcprkd);
	}

	public List<BcpRkdBean> getCpRkNonCheckData(Integer bzID, Integer fzrID, Integer zjyID, Integer zjldID, Integer kgID) {
		BcpRkdBean bcprkd=new BcpRkdBean();
		bcprkd.setBzID(bzID);
		bcprkd.setFzrID(fzrID);
		bcprkd.setFlfzrID(fzrID);
		bcprkd.setZjyID(zjyID);
		bcprkd.setZjldID(zjldID);
		bcprkd.setKgID(kgID);
		bcprkd.setLlfzrID(fzrID);

		return mainDao.getCpRkNonCheckData(bcprkd);
	}

	public List<BcpCkdBean> getCpCkNonCheckData(Integer kgID,Integer fzrID) {
		BcpCkdBean bcpCkdBean=new BcpCkdBean();
		bcpCkdBean.setKgID(kgID);
		bcpCkdBean.setFzrID(fzrID);

		return mainDao.getCpCkNonCheckData(bcpCkdBean);
	}

	public List<BcpCkdBean> getBcpCkNonCheckData(Integer kgID, Integer bzID, Integer fzrID) {
		BcpCkdBean bcpCkdBean=new BcpCkdBean();
		bcpCkdBean.setKgID(kgID);
		bcpCkdBean.setBzID(bzID);
		bcpCkdBean.setFzrID(fzrID);
		bcpCkdBean.setLlfzrID(fzrID);

		return mainDao.getBcpCkNonCheckData(bcpCkdBean);
	}

	public List<BcpTkdBean> getBcpTkNonCheckData(Integer bzID,Integer zjyID,Integer zjldID,Integer kgID,Integer fzrID) {
		BcpTkdBean bcptkd=new BcpTkdBean();
		bcptkd.setBzID(bzID);
		bcptkd.setZjyID(zjyID);
		bcptkd.setZjldID(zjldID);
		bcptkd.setKgID(kgID);
		bcptkd.setFzrID(fzrID);
		bcptkd.setTlfzrID(fzrID);
		bcptkd.setSlfzrID(fzrID);

		return mainDao.getBcpTkNonCheckData(bcptkd);
	}

	public WlRkdBean getWlRkdBean(String dh) {
		return mainDao.getWlRkdBean(dh);
	}

	public List<WLINShowBean> getWLINShowBeanListByInDh(String dh) {
		List<WLINShowBean> list=mainDao.getWLINShowBeanListByInDh(dh);
		return list;
	}

	public WlCkdBean getWlCkdBean(String dh) {
		return mainDao.getWlCkdBean(dh);
	}

	public List<WLOutShowBean> getWLOutShowBeanListByOutDh(String dh) {
		List<WLOutShowBean> list=mainDao.getWLOutShowBeanListByOutDh(dh);
		return list;
	}

	public WlTkdBean getWlTkdBean(String dh) {
		return mainDao.getWlTkdBean(dh);
	}

	public List<WLTkShowBean> getWLTkShowBeanListByOutDh(String dh) {
		List<WLTkShowBean> list=mainDao.getWLTkShowBeanListByOutDh(dh);
		return list;
	}

	public BcpRkdBean getBcpRkdBean(String dh) {
		return mainDao.getBcpRkdBean(dh);
	}

	public List<BcpInShowBean> getBcpInShowBeanListByInDh(String dh) {
		List<BcpInShowBean> list=mainDao.getBcpInShowBeanListByInDh(dh);
		return list;
	}

	public List<BcpInShowBean> getCpInShowBeanListByCPS2QRCode(String cPS2QRCode) {
    	return mainDao.getCpInShowBeanListByCPS2QRCode(cPS2QRCode);
	}

	public List<BcpInShowBean> getCpInShowBeanListByInDh(String dh) {
		List<BcpInShowBean> list=mainDao.getCpInShowBeanListByInDh(dh);
		return list;
	}

	public List<BcpInShowBean> getBigCpInShowBeanListByInDh(String dh) {
		List<BcpInShowBean> list=mainDao.getBigCpInShowBeanListByInDh(dh);
		return list;
	}

	public BcpCkdBean getBcpCkdBean(String dh) {
		return mainDao.getBcpCkdBean(dh);
	}

	public List<BcpOutShowBean> getBcpOutShowBeanListByOutDh(String dh) {
		List<BcpOutShowBean> list=mainDao.getBcpOutShowBeanListByOutDh(dh);
		return list;
	}

	public List<CpOutShowBean> getCpOutShowBeanListByOutDh(String dh) {
		List<CpOutShowBean> list=mainDao.getCpOutShowBeanListByOutDh(dh);
		return list;
	}

	public BcpTkdBean getBcpTkdBean(String dh) {
		return mainDao.getBcpTkdBean(dh);
	}

	public List<BcpTkShowBean> getBcpTkShowBeanListByBackDh(String dh) {
		List<BcpTkShowBean> list=mainDao.getBcpTkShowBeanListByBackDh(dh);
		return list;
	}

	public int agreeWlIn(WlRkdBean wlrkd) {
    	return mainDao.agreeWlIn(wlrkd);
	}

	public int refuseWlIn(WlRkdBean wlrkd) {
		return mainDao.refuseWlIn(wlrkd);
	}

	public int agreeWlOut(WlCkdBean wlckd) {
		return mainDao.agreeWlOut(wlckd);
	}

	public int refuseWlOut(WlCkdBean wlckd) {
		return mainDao.refuseWlOut(wlckd);
	}

	public int agreeWlTk(WlTkdBean wltkd) {
		return mainDao.agreeWlTk(wltkd);
	}

	public int refuseWlTk(WlTkdBean wltkd) {
		return mainDao.refuseWlTk(wltkd);
	}

	public int agreeBcpIn(BcpRkdBean bcpRkd) {
		return mainDao.agreeBcpIn(bcpRkd);
	}

	public int refuseBcpIn(BcpRkdBean bcpRkd) {
		return mainDao.refuseBcpIn(bcpRkd);
	}

	public int agreeBcpOut(BcpCkdBean bcpckd) {
		return mainDao.agreeBcpOut(bcpckd);
	}

	public int agreeCpOut(BcpCkdBean bcpckd) {
		return mainDao.agreeCpOut(bcpckd);
	}

	public int refuseBcpOut(BcpCkdBean bcpCkd) {
		return mainDao.refuseBcpOut(bcpCkd);
	}

	public int refuseCpOut(BcpCkdBean bcpCkd) {
		return mainDao.refuseCpOut(bcpCkd);
	}

	public int agreeBcpTk(BcpTkdBean bcpTkd) {
		return mainDao.agreeBcpTk(bcpTkd);
	}

	public int refuseBcpTk(BcpTkdBean bcpTkd) {
		return mainDao.refuseBcpTk(bcpTkd);
	}

	public List<BcpTrackResult> getBcpInShowData(String qrCodeId) {
		return mainDao.getBcpInShowData(qrCodeId);
	}

	public List<SmallCpTrackResult> getSmallCpInShowData(String qrCodeId) {
		return mainDao.getSmallCpInShowData(qrCodeId);
	}

	public List<BigCpTrackResult> getBigCpIn2ShowData(String qrCodeId) {
		return mainDao.getBigCpIn2ShowData(qrCodeId);
	}

	public int changeWLInPassCheckFlag(String qrCodeId, String zjy) {
		return mainDao.changeWLInPassCheckFlag(qrCodeId,zjy);
	}

	public int changeBCPInPassCheckFlag(String qrCodeId, String zjy, Integer zjzt) {
		return mainDao.changeBCPInPassCheckFlag(qrCodeId,zjy,zjzt);
	}

	public int changeSMALL_CPInPassCheckFlag(String qrCodeId, String zjy) {
		return mainDao.changeSMALL_CPInPassCheckFlag(qrCodeId,zjy);
	}

	public int changeBIG_CPInPassCheckFlag(String qrCodeId, String zjy, Integer zjzt) {
		return mainDao.changeBIG_CPInPassCheckFlag(qrCodeId,zjy,zjzt);
	}

	public List<WlRkdBean> getWlRkNonPassCheckDataByCzy(String realName) {
		return mainDao.getWlRkNonPassCheckDataByCzy(realName);
	}

	public List<WlCkdBean> getWlCkNonPassCheckDataByCzy(String realName) {
		return mainDao.getWlCkNonPassCheckDataByCzy(realName);
	}

	public List<WlTkdBean> getWlTkNonPassCheckDataByCzy(String realName) {
		return mainDao.getWlTkNonPassCheckDataByCzy(realName);
	}

	public List<BcpRkdBean> getBcpRkNonPassCheckDataByCzy(String realName) {
		return mainDao.getBcpRkNonPassCheckDataByCzy(realName);
	}

	public List<BcpCkdBean> getBcpCkNonPassCheckDataByCzy(String realName) {
		return mainDao.getBcpCkNonPassCheckDataByCzy(realName);
	}

	public List<BcpTkdBean> getBcpTkNonPassCheckDataByCzy(String realName) {
		return mainDao.getBcpTkNonPassCheckDataByCzy(realName);
	}

	public int updateWlInData(WLINShowBean param) {
		return mainDao.updateWlInData(param);
	}

	public int updateWlRkdData(WlInVerifyResult param) {
		return mainDao.updateWlRkdData(param);
	}

	public int updateWlCkdData(WlOutVerifyResult param) {
		return mainDao.updateWlCkdData(param);
	}

	public int updateWlOutData(WLOutShowBean wlOutShowBean) {
		return mainDao.updateWlOutData(wlOutShowBean);
	}

	public int updateWlTkdData(WlTkVerifyResult param) {
		return mainDao.updateWlTkdData(param);
	}

	public int updateWlTkData(WLTkShowBean wlTkShowBean) {
		return mainDao.updateWlTkData(wlTkShowBean);
	}

	public int updateBcpRkdData(BcpInVerifyResult param) {
		return mainDao.updateBcpRkdData(param);
	}

	public int updateBcpInData(BcpInShowBean bcpInShowBean) {
		return mainDao.updateBcpInData(bcpInShowBean);
	}

	public int updateSmallCpInData(BcpInShowBean bcpInShowBean) {
		return mainDao.updateSmallCpInData(bcpInShowBean);
	}

	public int updateBigCpInData(BcpInShowBean bcpInShowBean) {
		return mainDao.updateBigCpInData(bcpInShowBean);
	}

	public int updateBcpTkdData(BcpTkVerifyResult param) {
		return mainDao.updateBcpTkdData(param);
	}

	public int updateBcpTkData(BcpTkShowBean bcpTkShowBean) {
		return mainDao.updateBcpTkData(bcpTkShowBean);
	}

	public int updateBcpCkdData(BcpOutVerifyResult param) {
		return mainDao.updateBcpCkdData(param);
	}

	public int updateCpCkdData(CpOutVerifyResult param) {
		return mainDao.updateCpCkdData(param);
	}

	public int updateWLIN_M(WLINParam wlinParam) {
    	int count=0;
		String qRCodeID = wlinParam.getqRCodeID();
		String typeNum = qRCodeID.substring(0, 9);
		int num = Integer.valueOf(qRCodeID.substring(9));
		float ts = wlinParam.gettS();
		for(int i=num;i<num + ts;i++){
			if(i!=num){
				wlinParam.setqRCodeID(typeNum+i);//批量录入时，设置下一个二维码编号
			}
			if (mainDao.updateWLIN_M(wlinParam)>0)
				count++;
			else{//如果没有更新记录成功，说明没有二维码了，但前面已经把二维码编号加1了，这里就得复原
				wlinParam.setqRCodeID(typeNum+(num+count-1));
				break;
			}
		}
		return count;
	}

	public int createWL_RKD_New(CreateWLRKDParam rkdpParams) {
		return mainDao.createWL_RKD_New(rkdpParams);
	}

	public PersonResult getAllPerson(User param) {
		List<PersonBean> beans =  mainDao.getAllPerson(param);
		PersonResult personResult = new PersonResult();
		personResult.setPersonBeans(beans);
		return personResult;
	}

	public PersonResult searchAllPerson() {
		List<PersonBean> beans =  mainDao.searchAllPerson();
		PersonResult personResult = new PersonResult();
		personResult.setPersonBeans(beans);
		return personResult;
	}

	public int updateBcpIn(BCPINParam bcpInParam) {
		int count=0;
		String qRCodeID = bcpInParam.getQrCodeId();
		String typeNum = qRCodeID.substring(0, 9);
		int num = Integer.valueOf(qRCodeID.substring(9));
		int ts = bcpInParam.gettS();
		for(int i=num;i<num + ts;i++){
			if(i!=num){
				bcpInParam.setQrCodeId(typeNum+i);//批量录入时，设置下一个二维码编号
			}
			if (mainDao.updateBcpIn(bcpInParam)>0)
				count++;
			else{//如果没有更新记录成功，说明没有二维码了，但前面已经把二维码编号加1了，这里就得复原
				bcpInParam.setQrCodeId(typeNum+(num+count-1));
				break;
			}
		}
		return count;
	}

	public List<WlRkdBean> getWlRkCanModifyData(String realName) {
		return mainDao.getWlRkCanModifyData(realName);
	}

	public List<WlCkdBean> getWlCkCanModifyData(String realName) {
		return mainDao.getWlCkCanModifyData(realName);
	}

	public List<WlTkdBean> getWlTkCanModifyData(String realName) {
		return mainDao.getWlTkCanModifyData(realName);
	}

	public List<BcpRkdBean> getBcpRkCanModifyData(String realName) {
		return mainDao.getBcpRkCanModifyData(realName);
	}

	public List<BcpCkdBean> getBcpCkCanModifyData(String realName) {
		return mainDao.getBcpCkCanModifyData(realName);
	}

	public List<BcpCkdBean> getCpCkCanModifyData(String realName) {
		return mainDao.getCpCkCanModifyData(realName);
	}

	public List<BcpTkdBean> getBcpTkCanModifyData(String realName) {
		return mainDao.getBcpTkCanModifyData(realName);
	}

	public int updateCPIn(SmallCPINParam inParam) {
		int count=0;
		String qRCodeID = inParam.getQrCodeId();
		String typeNum = qRCodeID.substring(0, 9);
		int num = Integer.valueOf(qRCodeID.substring(9));
		float shl = inParam.getShl();
		for(int i=num;i<num + shl;i++){
			if(i!=num){
				inParam.setQrCodeId(typeNum+i);//批量录入时，设置下一个二维码编号
			}
			if (mainDao.updateCPIn(inParam)>0)
				count++;
			else{//如果没有更新记录成功，说明没有二维码了，但前面已经把二维码编号加1了，这里就得复原
				inParam.setQrCodeId(typeNum+(num+count-1));
				break;
			}
		}

		return count;
	}

	public int updateCPIn2ByParam(BigCPINParam inParam) {
		int count=0;
		String qRCodeID = inParam.getQrCodeId();
		String typeNum = qRCodeID.substring(0, 9);
		int num = Integer.valueOf(qRCodeID.substring(9));
		int ts = inParam.gettS();
		for (int i=num;i<num + ts;i++) {
			if (i != num) {
				inParam.setQrCodeId(typeNum + i);//批量录入时，设置下一个二维码编号
			}
			if (mainDao.updateCPIn2ByParam(inParam) > 0) {
				count++;
			} else {//如果没有更新记录成功，说明没有二维码了，但前面已经把二维码编号加1了，这里就得复原
				inParam.setQrCodeId(typeNum + (num + count - 1));
				break;
			}
		}
		return count;
	}

	/**
	 *根据入库单号查询入库单信息
	 * */
	public CreateWLRKDParam getRkdWlByInDh(String dh) {
		return mainDao.getCreateRKDParamByInDh(dh);
	}

	public List<WLOutParam> getWLOutParamListByOutDh(String dh) {
		return mainDao.getWLOutParamListByOutDh(dh);
	}

	public List<BcpOutParam> getBcpOutParamListByOutDh(String dh) {
		return mainDao.getBcpOutParamListByOutDh(dh);
	}

	public List<WLTKParam> getWLTKParamListByOutDh(String dh) {
		return mainDao.getWLTKParamListByOutDh(dh);
	}

	public List<BCPTKParam> getBCPTKParamListByOutDh(String dh) {
		return mainDao.getBCPTKParamListByOutDh(dh);
	}

	public List<BigCPINParam> getBigCPINParamListByInDh(String dh) {
		return mainDao.getBigCPINParamListByInDh(dh);
	}

	public List<SmallCPINParam> getSmallCPINParamListByInDh(String dh) {
		return mainDao.getSmallCPINParamListByInDh(dh);
	}

	public List<BigCpOutParam> getBigCpOutParamListByOutDh(String dh) {
		return mainDao.getBigCpOutParamListByOutDh(dh);
	}

	public List<SmallCPINParam> getSmallCPINParamListByCPS2QRCode(String qrCodeId) {
		return mainDao.getSmallCPINParamListByCPS2QRCode(qrCodeId);
	}

	/**
	 * 根据小包装二维码还原相关大包装入库单状态
	 * */
	public int updateBcpRkdStatusByQRCodeID(String qrCodeId) {
		return mainDao.updateBcpRkdStatusByQRCodeID(qrCodeId);
	}

    public Module2Result getXZQXData() {
		Module2Result result=new Module2Result();
		List<Module2Bean> beans=mainDao.getXZQXData();
		result.setBeans(beans);
		return result;
    }

    public Module2Result getXZQXDataByShenFen(PersonParam param) {
		Module2Result result=new Module2Result();
		List<Module2Bean> beans=mainDao.getXZQXDataByShenFen(param);
		result.setBeans(beans);
		return result;
    }

	public int registerUser(PersonParam personParam) {
		int count=0;
		count = mainDao.checkLoginName(personParam);
		if(count==0)
			count = mainDao.registerUser(personParam);
		else
			count=0;
		return count;
	}

	public PersonResult getPersonById(int userID) {
		return mainDao.getPersonById(userID);
	}

	public int updateUserData(PersonResult personResult) {
		int count=0;
		PersonParam personParam = new PersonParam();
		personParam.setUserId(personResult.getUserId());//这里是修改用户时验证账号，要加用户Id，排除除了自己之外还有无重复的账号
		personParam.setLoginName(personResult.getLoginName());
		count = mainDao.checkLoginName(personParam);
		if(count==0)
			count = mainDao.updateUserData(personResult);
		else
			count=0;
		return count;
	}

    public int deleteUser(int userId) {
		return mainDao.deleteUser(userId);
    }

	public List<BcpRkdBean> getCpRkCanModifyData(String realName) {
		return mainDao.getCpRkCanModifyData(realName);
	}

	public boolean checkExistByQrCodeId(String qrCodeId, Integer currentFunctionType) {
		int count=mainDao.checkExistByQrCodeId(qrCodeId, currentFunctionType);
		return count>0?true:false;
	}

	public int deleteFromWLS(String qrCodeId) {
		return mainDao.deleteFromWLS(qrCodeId);
	}

	public WLThrowShowDataResult getWLTl(String qrCodeId) {
		return mainDao.getWLTl(qrCodeId);
	}

	public BcpThrowShowDataResult getBCPTl(String qrCodeId) {
		return mainDao.getBCPTl(qrCodeId);
	}

	public int deleteFromWLTl(String qrCodeId) {
		return mainDao.deleteFromWLTl(qrCodeId);
	}

	public int deleteFromBCPTl(String qrCodeId) {
		return mainDao.deleteFromBCPTl(qrCodeId);
	}

	public int deleteFromBCPS(String qrCodeId) {
		return mainDao.deleteFromBCPS(qrCodeId);
	}

	public int updateWLTlByBcpIn(WLThrowParam wlThrowParam) {
		return mainDao.updateWLTlByBcpIn(wlThrowParam);
	}

	public int updateBCPTlByBcpIn(BcpThrowParam bcpThrowParam) {
		return mainDao.updateBCPTlByBcpIn(bcpThrowParam);
	}

}
