package lunnardo.jwork_android;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ApplyJobRequest extends StringRequest {
    private Map<String, String> params;
    private static final String URL_EWallet = NetworkInfo.getIpAddress() + "/invoice/createEWalletPayment";
    private static final String URL_Bank = NetworkInfo.getIpAddress() + "/invoice/createBankPayment";

    public ApplyJobRequest(String jobIdList, String jobseekerId, Response.Listener<String> listener){
        super(Method.POST, URL_Bank , listener, null);
        params = new HashMap<>();
        params.put("jobIdList", jobIdList);
        params.put("jobseekerId", jobseekerId);
        params.put("adminFee", "2000");
    }

    public ApplyJobRequest(String jobIdList, String jobseekerId, String referralCode, Response.Listener<String> listener) {
        super(Method.POST, URL_EWallet, listener, null);
        params = new HashMap<>();
        params.put("jobIdList", jobIdList);
        params.put("jobseekerId", jobseekerId);
        params.put("referralCode", referralCode);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError{
        return params;
    }
}
