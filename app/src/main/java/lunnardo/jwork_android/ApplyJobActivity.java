package lunnardo.jwork_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ApplyJobActivity extends AppCompatActivity {

    private int jobseekerId;
    private String jobseekerName;
    private int jobId;
    private String jobName;
    private String jobCategory;
    private double jobFee;
    private int bonus;
    private String selectedPayment;
    private int extraFee;
    private int minTotalFee;
    private boolean active;
    private String referralCode;
    ApplyJobRequest applyJobRequest;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_apply_job);

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                jobseekerId = extras.getInt("jobseekerId");
                jobseekerName = extras.getString("jobseekerName");
                jobId = extras.getInt("job_id");
                jobName = extras.getString("job_name");
                jobFee = extras.getInt("job_fee");
                jobCategory = extras.getString("job_category");
            }

            final EditText etReferralCode = findViewById(R.id.referral_code);
            final TextView tvReferralCode = findViewById(R.id.textCode);

            final TextView tvJobName = findViewById(R.id.job_name);
            final TextView tvJobCategory = findViewById(R.id.job_category);
            final TextView tvJobFee = findViewById(R.id.job_fee);
            final TextView tvTotalFee = findViewById(R.id.total_fee);


            final Button btnApply = findViewById(R.id.btnApply);
            final Button btnHitung = findViewById(R.id.hitung);

            final RadioGroup radioGroup = findViewById(R.id.radioGroup);



            btnApply.setVisibility(View.INVISIBLE);
            tvReferralCode.setVisibility(View.INVISIBLE);
            etReferralCode.setVisibility(View.INVISIBLE);

            btnHitung.setEnabled(false);
            btnApply.setEnabled(false);

            tvJobName.setText(jobName);
            tvJobCategory.setText(jobCategory);
            tvJobFee.setText("Rp. " + String.valueOf((int) jobFee));
            tvTotalFee.setText("Rp. 0");

            radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                btnApply.setVisibility(View.INVISIBLE);

                RadioButton rbId = findViewById(checkedId);
                String selectedPayment = rbId.getText().toString().trim();
                btnHitung.setEnabled(true);

                switch(selectedPayment){
                    case "Bank":
                        etReferralCode.setVisibility(View.GONE);
                        tvReferralCode.setVisibility(View.GONE);
                        btnHitung.setVisibility(View.VISIBLE);
                        break;
                    case "E-wallet":
                        etReferralCode.setVisibility(View.VISIBLE);
                        etReferralCode.setEnabled(true);
                        tvReferralCode.setVisibility(View.VISIBLE);
                        break;
                }
            });


            btnHitung.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String refCode = etReferralCode.getText().toString().trim();

                    int rgBtnId = radioGroup.getCheckedRadioButtonId();
                    RadioButton rbCheck = findViewById(rgBtnId);

                    try {
                        String paymentMethod = rbCheck.getText().toString().trim();

                        switch (paymentMethod) {
                            case "E-wallet":
                                if (!refCode.isEmpty()) {
                                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                if (!jsonObject.equals(null)) {
                                                    int extraFee = jsonObject.getInt("extraFee");
                                                    int minTotalFee = jsonObject.getInt("minTotalFee");
                                                    boolean status = jsonObject.getBoolean("active");

                                                    if (status && (jobFee > minTotalFee || jobFee > extraFee)) {
                                                        tvTotalFee.setText("Rp." + (jobFee + extraFee));
                                                        Toast.makeText(ApplyJobActivity.this,
                                                                "Total biaya berhasil dihitung",
                                                                Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        tvTotalFee.setText("Rp." + jobFee);
                                                        Toast.makeText(ApplyJobActivity.this,
                                                                "Referral code tidak ada, masukkan referral code lainnya",
                                                                Toast.LENGTH_SHORT).show();
                                                        btnHitung.setVisibility(View.VISIBLE);
                                                        btnHitung.setEnabled(true);
                                                        btnApply.setVisibility(View.INVISIBLE);
                                                    }
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                                Toast.makeText(ApplyJobActivity.this,
                                                        "Referral code tidak ada, masukkan referral code lainnya",
                                                        Toast.LENGTH_SHORT).show();
                                                btnHitung.setVisibility(View.VISIBLE);
                                                btnHitung.setEnabled(true);
                                                btnApply.setVisibility(View.INVISIBLE);
                                            }
                                        }
                                    };

                                    BonusRequest bonusRequest = new BonusRequest(refCode, responseListener);
                                    RequestQueue queue = Volley.newRequestQueue(ApplyJobActivity.this);
                                    queue.add(bonusRequest);
                                } else {
                                    tvTotalFee.setText("Rp." + jobFee);
                                    Toast.makeText(ApplyJobActivity.this,
                                            "Total biaya berhasil dihitung",
                                            Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case "Bank":
                                tvTotalFee.setText("Rp." + jobFee);
                                Toast.makeText(ApplyJobActivity.this, "Total biaya berhasil dihitung",
                                        Toast.LENGTH_LONG).show();
                                break;
                            default:
                                break;
                        }
                        btnHitung.setVisibility(View.INVISIBLE);
                        btnHitung.setEnabled(false);

                        btnApply.setVisibility(View.VISIBLE);
                        btnApply.setEnabled(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            btnApply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String refCode = etReferralCode.getText().toString().trim();

                    int rgBtnId = radioGroup.getCheckedRadioButtonId();
                    RadioButton rbCheck = findViewById(rgBtnId);
                    String paymentMethod = rbCheck.getText().toString().trim();

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (!jsonObject.equals(null)) {
                                    Toast.makeText(ApplyJobActivity.this,
                                            "Pekerjaan berhasil didaftar", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(ApplyJobActivity.this,
                                            "Pekerjaan gagal didaftar", Toast.LENGTH_LONG).show();
                                }
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(ApplyJobActivity.this,
                                        "Pekerjaan gagal didaftar", Toast.LENGTH_LONG).show();
                            }
                        }
                    };

                    switch (paymentMethod) {
                        case "E-wallet":
                            applyJobRequest = new ApplyJobRequest(String.valueOf(jobId),
                                    String.valueOf(jobseekerId), refCode,
                                    responseListener);
                            break;
                        case "Bank":
                            applyJobRequest = new ApplyJobRequest(String.valueOf(jobId),
                                    String.valueOf(jobseekerId), responseListener);
                            break;
                        default:
                            break;
                    }

                    RequestQueue queue = Volley.newRequestQueue(ApplyJobActivity.this);
                    queue.add(applyJobRequest);
                }
            });
        }
}


