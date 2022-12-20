package com.cointizen.paysdk.http.process;

import android.os.Handler;

import com.lidroid.xutils.http.RequestParams;
import com.cointizen.paysdk.bean.SdkDomain;
import com.cointizen.paysdk.config.MCHConstant;
import com.cointizen.paysdk.http.request.ThirdLoginTypeRequest;
import com.cointizen.paysdk.utils.MCLog;
import com.cointizen.util.RequestParamUtil;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import com.cointizen.paysdk.http.HttpConstants;

public class ThirdLoginTypeProcess {

	private static final String TAG = "ThirdLoginTypeProcess";

	private String getParamStr(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("game_id", SdkDomain.getInstance().getGameId());
		MCLog.w(TAG, "fun#ptb_pay params:" + map.toString());
		
		return RequestParamUtil.getRequestParamString(map);
	}
	
	public void post(Handler handler) {
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(getParamStr().toString()));
		} catch (UnsupportedEncodingException e) {
			params = null;
			MCLog.e(TAG, "fun#post UnsupportedEncodingException:" + e);
		}

		if (null != handler && null != params) {
			MCLog.e(TAG,HttpConstants.Log_DUgelOUBAq+MCHConstant.getInstance().getThirdLoginTypeUrl());
			ThirdLoginTypeRequest request = new ThirdLoginTypeRequest(handler);
			request.post(MCHConstant.getInstance().getThirdLoginTypeUrl(),
					params);
		} else {
			MCLog.e(TAG, "fun#post handler is null or url is null");
		}
	}
	
}
