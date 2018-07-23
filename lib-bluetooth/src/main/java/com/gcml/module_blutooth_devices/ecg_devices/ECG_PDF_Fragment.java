package com.gcml.module_blutooth_devices.ecg_devices;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.gcml.lib_utils.data.DataUtils;
import com.gcml.lib_utils.data.FileUtils;
import com.gcml.lib_utils.data.TimeUtils;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.lib_utils.thread.ThreadUtils;
import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.BaseFragment;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.link.LinkHandler;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnLongPressListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.github.barteksc.pdfviewer.model.LinkTapEvent;
import com.github.barteksc.pdfviewer.util.FitPolicy;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

import timber.log.Timber;

public class ECG_PDF_Fragment extends BaseFragment {
    public static final String KEY_BUNDLE_PDF_URL = "key_pdf_url";
    private View view;
    private PDFView mPdfView;
    private String url_pdf;
    private boolean isDestroy = false;

    @Override
    protected int initLayout() {
        return R.layout.bluetooth_fragment_pdf;
    }

    private final ThreadUtils.SimpleTask<File> pdfDownload = new ThreadUtils.SimpleTask<File>() {
        @Nullable
        @Override
        public File doInBackground() throws Throwable {
            InputStream input = null;
            FileOutputStream fos = null;
            File pdfFile = null;
            try {
                URL url = new URL(url_pdf);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(10000);
                connection.setRequestMethod("GET");
                if (HttpURLConnection.HTTP_OK != connection.getResponseCode()) {
                    return null;
                }
                String diskCacheDir = FileUtils.getDiskCacheDir(getContext());
                pdfFile = new File(diskCacheDir,
                        "gcml_" + TimeUtils.getCurTimeString(new SimpleDateFormat("yyyyMMddHHmm")));
                FileUtils.createOrExistsFile(pdfFile);
                input = new BufferedInputStream(connection.getInputStream());
                fos = new FileOutputStream(pdfFile);

                int count;
                byte buf[] = new byte[1024];
                while ((count = input.read(buf)) != -1) {
                    if (isDestroy) {
                        cancel();
                        break;
                    }
                    fos.write(buf, 0, count);
                }
                return pdfFile;
            } catch (IOException e) {
                e.printStackTrace();
                if (pdfFile != null && pdfFile.exists()) {
                    pdfFile.delete();
                }
            } finally {
                if (null != fos) {
                    try {
                        fos.flush();
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (null != input) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        public void onSuccess(@Nullable File result) {
            if (result != null) {
                mPdfView.fromFile(result)
//                        .pages(0, 2,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   1, 3, 3, 3) //指定显示某页
                        .enableSwipe(true)
                        .swipeHorizontal(false)
                        .enableDoubletap(true)
                        .defaultPage(0)
                        // allows to draw something on the current page, usually visible in the middle of the screen
                        .onDraw(new OnDrawListener() {
                            @Override
                            public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {
                                Timber.e("pageWidth=" + pageWidth + "--pageHeight=" + pageHeight + "---displayedPage=" + displayedPage);
                            }
                        })
                        .onDrawAll(new OnDrawListener() {
                            @Override
                            public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

                            }
                        })
                        .onLoad(new OnLoadCompleteListener() {
                            @Override
                            public void loadComplete(int nbPages) {

                            }
                        })
                        .onPageChange(new OnPageChangeListener() {
                            @Override
                            public void onPageChanged(int page, int pageCount) {

                            }
                        })
                        .onPageScroll(new OnPageScrollListener() {
                            @Override
                            public void onPageScrolled(int page, float positionOffset) {

                            }
                        })
                        .onError(new OnErrorListener() {
                            @Override
                            public void onError(Throwable t) {

                            }
                        })
                        .onPageError(new OnPageErrorListener() {
                            @Override
                            public void onPageError(int page, Throwable t) {

                            }
                        })
                        .onRender(new OnRenderListener() {
                            @Override
                            public void onInitiallyRendered(int nbPages) {

                            }
                        })
                        .onTap(new OnTapListener() {
                            @Override
                            public boolean onTap(MotionEvent e) {
                                return false;
                            }
                        })
                        .onLongPress(new OnLongPressListener() {
                            @Override
                            public void onLongPress(MotionEvent e) {

                            }
                        })
                        .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                        .password(null)
                        .scrollHandle(null)
                        .enableAntialiasing(true)
                        .spacing(0)
                        .autoSpacing(false)
                        .linkHandler(new LinkHandler() {
                            @Override
                            public void handleLinkEvent(LinkTapEvent event) {

                            }
                        })
                        .pageFitPolicy(FitPolicy.WIDTH)
                        .pageSnap(true) // snap pages to screen boundaries
                        .pageFling(false) // make a fling change only a single page like ViewPager
                        .nightMode(false) // toggle night mode
                        .load();
            }
        }
    };

    @Override
    protected void initView(View view, Bundle bundle) {
        mPdfView = (PDFView) view.findViewById(R.id.pdfView);
        if (bundle != null) {
            url_pdf = bundle.getString(KEY_BUNDLE_PDF_URL);
            if (DataUtils.isNullString(url_pdf)) {
                ToastUtils.showShort("资源文件不存在");
                return;
            }
            ThreadUtils.executeByIo(pdfDownload);
        } else {
            ToastUtils.showShort("资源不存在");
        }

    }

    @Override
    public void onDestroyView() {
        isDestroy = true;
        ThreadUtils.cancel(pdfDownload);
        super.onDestroyView();
    }
}
