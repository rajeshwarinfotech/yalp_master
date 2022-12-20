package com.cointizen.paysdk.http.request;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.cointizen.paysdk.bean.SignBean;
import com.cointizen.paysdk.common.Constant;
import com.cointizen.paysdk.http.RequestUtil;
import com.cointizen.paysdk.utils.MCErrorCodeUtils;
import com.cointizen.paysdk.utils.MCLog;

import org.json.JSONException;
import org.json.JSONObject;

public class SignDetRequest {

    private static final String TAG = "SignDetRequest";

    HttpUtils http;
    Handler mHandler;

    public SignDetRequest(Handler mHandler) {
        http = new HttpUtils();
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(String url, RequestParams params) {
        if (TextUtils.isEmpty(url) || null == params) {
            MCLog.e(TAG, "fun#post url is null add params is null");
            noticeResult(Constant.SIGN_DET_FAIL, "参数异常");
            return;
        }
        MCLog.e(TAG, "fun#post url " + url);
        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String response = RequestUtil.getResponse(responseInfo);
                int status;
                try {
                    JSONObject json = new JSONObject(response);
                    status = json.optInt("code");
                    if (status == 200 ) {
                        SignBean signBean = new SignBean();
                        JSONObject jsonData = json.optJSONObject("data");
                        signBean.setSigned_day(jsonData.optInt("signed_day"));
                        signBean.setToday_signed(jsonData.optInt("today_signed"));
                        signBean.setTotal_sign(jsonData.optInt("total_sign"));
                        signBean.setTotay_point(jsonData.optInt("totay_point"));

                        SignBean.PointArrBean pointArrBean = new SignBean.PointArrBean();
                        JSONObject jsonPointArr = jsonData.optJSONObject("point_arr");
                        pointArrBean.setDay1(jsonPointArr.optInt("1"));
                        pointArrBean.setDay2(jsonPointArr.optInt("2"));
                        pointArrBean.setDay3(jsonPointArr.optInt("3"));
                        pointArrBean.setDay4(jsonPointArr.optInt("4"));
                        pointArrBean.setDay5(jsonPointArr.optInt("5"));
                        pointArrBean.setDay6(jsonPointArr.optInt("6"));
                        pointArrBean.setDay7(jsonPointArr.optInt("7"));

                        signBean.setPoint_arr(pointArrBean);
                        noticeResult(Constant.SIGN_DET_SUCCESS, signBean);
                    } else {
                        String msg;
                        if (!TextUtils.isEmpty(json.optString("msg"))) {
                            msg = json.optString("msg","");
                        } else {
                            msg = MCErrorCodeUtils.getErrorMsg(status);
                        }
                        MCLog.e(TAG, "msg:" + msg);
                        noticeResult(Constant.SIGN_DET_FAIL, msg);
                    }
                } catch (JSONException e) {
                    noticeResult(Constant.SIGN_DET_FAIL, "解析参数异常");
                    MCLog.e(TAG, "fun#post JSONException:" + e);
                } catch (Exception e) {
                    noticeResult(Constant.SIGN_DET_FAIL, "Failed to connect to the server");
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                MCLog.e(TAG, "fun#onFailure error = " + error.getExceptionCode());
                MCLog.e(TAG, "onFailure" + msg);
                noticeResult(Constant.SIGN_DET_FAIL, "Failed to connect to the server");
            }
        });
    }

    private void noticeResult(int type, Object obj) {
        Message msg = new Message();
        msg.what = type;
        msg.obj = obj;
        if (null != mHandler) {
            mHandler.sendMessage(msg);
        }
    }
}
