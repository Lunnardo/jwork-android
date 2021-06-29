package lunnardo.jwork_android;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class BonusRequest extends StringRequest {
    private static final String URL = NetworkInfo.getIpAddress() + "/bonus/";
    private Map<String, String> params;

    public BonusRequest (String referralCode, Response.Listener<String> listener){
        super(Request.Method.GET, URL+ referralCode, listener, null);
        params = new HashMap<>();
    }

    @Override
    protected Map<String,String> getParams() throws AuthFailureError {
        return params;
    }
}