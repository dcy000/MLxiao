package com.gcml.module_blutooth_devices.ecg;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.gcml.common.service.IFragmentProvider;
import com.sjtu.yifei.annotation.Route;

@Route(path = "/bluetooth/ecg/pdf/fragment")
public class ECGPDFFragmentImp implements IFragmentProvider {
    private String key_pdf_url;

    public ECGPDFFragmentImp(String key_pdf_url) {
        this.key_pdf_url = key_pdf_url;
    }

    @Override
    public Fragment getECG_PDF_Fragment() {
        ECG_PDF_Fragment pdfFragment = new ECG_PDF_Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("key_pdf_url", key_pdf_url);
        pdfFragment.setArguments(bundle);
        return pdfFragment;
    }
}
