package lunnardo.jwork_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SelesaiJobActivity extends AppCompatActivity {
    TextView tvInvoiceId, tvStaticJobseeker, tvStaticInvoiceDate, tvStaticPaymentType, tvStaticInvoiceStatus , tvStaticReferralCode, tvStaticJobName, tvStaticTotalFee;
    TextView tvJobseekerName, tvInvoiceDate, tvPaymentType, tvInvoiceStatus, tvReferralCode, tvJobName, tvJobFee, tvTotalFee;

    View invoiceLine;

    private Button btnCancel, btnFinish;

    private int jobseekerId;
    private String jobseekerName;
    private int invoiceId;
    private String invoiceDate;
    private String paymentType;
    private String invoiceStatus;
    private String referralCode;
    private String jobName;
    private int jobFee;
    private int totalFee;
    private JSONObject bonus;
    private int extraFee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selesai_job);

         tvInvoiceId = findViewById(R.id.invoice_id);
         tvStaticJobseeker = findViewById(R.id.static_jobseeker);
         tvStaticInvoiceDate = findViewById(R.id.static_invoice_date);
         tvStaticPaymentType = findViewById(R.id.static_payment_type);
         tvStaticInvoiceStatus = findViewById(R.id.static_invoice_status);
         tvStaticReferralCode = findViewById(R.id.static_referral_code);
         tvStaticJobName = findViewById(R.id.static_job_name);
         tvStaticTotalFee = findViewById(R.id.static_total_fee);

         tvJobseekerName = findViewById(R.id.tv_jobseeker_name);
         tvInvoiceDate = findViewById(R.id.tv_invoice_date);
         tvPaymentType = findViewById(R.id.tv_payment_type);
         tvInvoiceStatus = findViewById(R.id.tv_invoice_status);
         tvReferralCode = findViewById(R.id.tv_referral_code);
         tvJobName = findViewById(R.id.tv_job_name);
         tvJobFee = findViewById(R.id.tv_invoice_job_fee);
         tvTotalFee = findViewById(R.id.tv_total_fee);

         invoiceLine = findViewById(R.id.invoice_line);

        btnCancel = findViewById(R.id.btn_invoice_cancel);
        btnFinish = findViewById(R.id.btn_invoice_finish);

        Bundle mExtras = getIntent().getExtras();
        if (!mExtras.equals(null)) {
            jobseekerId = mExtras.getInt("jobseekerId");
            jobseekerName = mExtras.getString("jobseekerName");
        }

        tvInvoiceId.setVisibility(View.INVISIBLE);
        tvStaticJobseeker.setVisibility(View.INVISIBLE);
        tvStaticInvoiceDate.setVisibility(View.INVISIBLE);
        tvStaticPaymentType.setVisibility(View.INVISIBLE);
        tvStaticInvoiceStatus.setVisibility(View.INVISIBLE);
        tvStaticReferralCode.setVisibility(View.INVISIBLE);
        tvStaticJobName.setVisibility(View.INVISIBLE);
        tvStaticTotalFee.setVisibility(View.INVISIBLE);

        tvJobseekerName.setVisibility(View.INVISIBLE);
        tvInvoiceDate.setVisibility(View.INVISIBLE);
        tvPaymentType.setVisibility(View.INVISIBLE);
        tvInvoiceStatus.setVisibility(View.INVISIBLE);
        tvReferralCode.setVisibility(View.INVISIBLE);
        tvJobName.setVisibility(View.INVISIBLE);
        tvJobFee.setVisibility(View.INVISIBLE);
        tvTotalFee.setVisibility(View.INVISIBLE);

        invoiceLine.setVisibility(View.INVISIBLE);

        btnCancel.setVisibility(View.INVISIBLE);
        btnCancel.setEnabled(false);

        btnFinish.setVisibility(View.INVISIBLE);
        btnFinish.setEnabled(false);

        fetchJob();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Intent intent = new Intent(SelesaiJobActivity.this,
                                    MainActivity.class);
                            intent.putExtra("jobseekerId", jobseekerId);
                            intent.putExtra("jobseekerName", jobseekerName);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                Toast.makeText(SelesaiJobActivity.this, "Pekerjaan yang didaftarkan telah diselesaikan",
                        Toast.LENGTH_LONG).show();
                JobBatalRequest jobBatalRequest = new JobBatalRequest(String.valueOf(invoiceId),
                        responseListener);
                RequestQueue queue = Volley.newRequestQueue(SelesaiJobActivity.this);
                queue.add(jobBatalRequest);
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Intent intent = new Intent(SelesaiJobActivity.this,
                                    MainActivity.class);
                            intent.putExtra("jobseekerId", jobseekerId);
                            intent.putExtra("jobseekerName", jobseekerName);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                Toast.makeText(SelesaiJobActivity.this, "Pekerjaan yang didaftarkan telah diselesaikan",
                        Toast.LENGTH_LONG).show();
                JobSelesaiRequest jobSelesaiRequest = new JobSelesaiRequest(String.valueOf(invoiceId),
                        responseListener);
                RequestQueue queue = Volley.newRequestQueue(SelesaiJobActivity.this);
                queue.add(jobSelesaiRequest);
            }
        });
    }
    protected void fetchJob() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() == 0) {
                        noAppliedJob();
                    }

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        invoiceStatus = jsonObject.getString("invoiceStatus");

                        if (invoiceStatus.equals("OnGoing")) {
                            tvInvoiceId.setVisibility(View.VISIBLE);
                            tvStaticJobseeker.setVisibility(View.VISIBLE);
                            tvStaticInvoiceDate.setVisibility(View.VISIBLE);
                            tvStaticPaymentType.setVisibility(View.VISIBLE);
                            tvStaticInvoiceStatus.setVisibility(View.VISIBLE);
                            tvStaticReferralCode.setVisibility(View.VISIBLE);
                            tvStaticJobName.setVisibility(View.VISIBLE);
                            tvStaticTotalFee.setVisibility(View.VISIBLE);

                            tvJobseekerName.setVisibility(View.VISIBLE);
                            tvInvoiceDate.setVisibility(View.VISIBLE);
                            tvPaymentType.setVisibility(View.VISIBLE);
                            tvInvoiceStatus.setVisibility(View.VISIBLE);
                            tvReferralCode.setVisibility(View.VISIBLE);
                            tvJobName.setVisibility(View.VISIBLE);
                            tvJobFee.setVisibility(View.VISIBLE);
                            tvTotalFee.setVisibility(View.VISIBLE);

                            invoiceLine.setVisibility(View.VISIBLE);

                            btnCancel.setVisibility(View.VISIBLE);
                            btnCancel.setEnabled(true);

                            btnFinish.setVisibility(View.VISIBLE);
                            btnFinish.setEnabled(true);

                            invoiceId = jsonObject.getInt("id");
                            invoiceDate = jsonObject.getString("date");
                            paymentType = jsonObject.getString("paymentType");
                            totalFee = jsonObject.getInt("totalFee");
                            referralCode = "null";

                            try {
                                bonus = jsonObject.getJSONObject("bonus");
                                referralCode = bonus.getString("referralCode");
                                extraFee = bonus.getInt("extraFee");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            tvInvoiceId.setText(String.valueOf(invoiceId));
                            tvInvoiceDate.setText(invoiceDate.substring(0, 10));
                            tvInvoiceStatus.setText(invoiceStatus);
                            tvPaymentType.setText(paymentType);
                            if (!referralCode.isEmpty()) {
                                tvTotalFee.setText(String.valueOf(totalFee + extraFee));
                            } else {
                                tvTotalFee.setText(String.valueOf(totalFee));
                            }
                            tvReferralCode.setText(referralCode);

                            JSONObject jsonJobseeker = jsonObject.getJSONObject("jobseeker");
                            jobseekerName = jsonJobseeker.getString("name");
                            tvJobseekerName.setText(jobseekerName);

                            JSONArray jsonJobList = jsonObject.getJSONArray("jobs");
                            for (int j = 0; j < jsonJobList.length(); j++) {
                                JSONObject jsonJob = jsonJobList.getJSONObject(j);
                                jobName = jsonJob.getString("name");
                                jobFee = jsonJob.getInt("fee");

                                tvJobName.setText(jobName);
                                tvJobFee.setText(String.valueOf(jobFee));
                            }
                        }

                        if (i == (jsonArray.length() - 1) && !invoiceStatus.equals("OnGoing")) {
                            noAppliedJob();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        JobFetchRequest jobFetchRequest = new JobFetchRequest(String.valueOf(jobseekerId), responseListener);
        RequestQueue queue = Volley.newRequestQueue(SelesaiJobActivity.this);
        queue.add(jobFetchRequest);
    }

    protected void noAppliedJob() {
        tvInvoiceId.setVisibility(View.INVISIBLE);
        tvStaticJobseeker.setVisibility(View.INVISIBLE);
        tvStaticInvoiceDate.setVisibility(View.INVISIBLE);
        tvStaticPaymentType.setVisibility(View.INVISIBLE);
        tvStaticInvoiceStatus.setVisibility(View.INVISIBLE);
        tvStaticReferralCode.setVisibility(View.INVISIBLE);
        tvStaticJobName.setVisibility(View.INVISIBLE);
        tvStaticTotalFee.setVisibility(View.INVISIBLE);

        tvJobseekerName.setVisibility(View.INVISIBLE);
        tvInvoiceDate.setVisibility(View.INVISIBLE);
        tvPaymentType.setVisibility(View.INVISIBLE);
        tvInvoiceStatus.setVisibility(View.INVISIBLE);
        tvReferralCode.setVisibility(View.INVISIBLE);
        tvJobName.setVisibility(View.INVISIBLE);
        tvJobFee.setVisibility(View.INVISIBLE);
        tvTotalFee.setVisibility(View.INVISIBLE);

        invoiceLine.setVisibility(View.INVISIBLE);

        btnCancel.setVisibility(View.INVISIBLE);
        btnCancel.setEnabled(false);

        btnFinish.setVisibility(View.INVISIBLE);
        btnFinish.setEnabled(false);

        Toast.makeText(SelesaiJobActivity.this,
                "Anda belum mendaftar ke pekerjaan apapun", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(SelesaiJobActivity.this,
                MainActivity.class);
        intent.putExtra("jobseekerId", jobseekerId);
        intent.putExtra("jobseekerName", jobseekerName);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

}