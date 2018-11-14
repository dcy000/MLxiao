package com.gzq.lib_core.http.upload;


import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.exception.ErrorType;
import com.gzq.lib_core.http.exception.ServerException;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version: V1.2.5
 * created on 2018/10/15 15:43
 * created by: gzq
 * description: TODO
 */
public class RxUploadUtil {
    /**
     * 上传单个文件
     *
     *
     * 使用方式：
     * 1.创建
     * public interface FileUploadService {
     *      @Multipart
     *      @POST("/upload")
     *      Flowable<HttpResult<String>> upload(@Part MultipartBody.Part file);
     *   }
     *
     *2.
     */
    public static <T> Flowable<Object> uploadFile(File file, Class<T> uploadServiceClass, String uploadFucntionName, Object... params) {
        //进度Observable
        UploadOnSubscribe uploadOnSubscribe = new UploadOnSubscribe(file.length());
        Flowable<Integer> progressObservale = Flowable.create(uploadOnSubscribe, BackpressureStrategy.BUFFER);

        UploadRequestBody uploadRequestBody = new UploadRequestBody(file);
        //设置进度监听
        uploadRequestBody.setUploadOnSubscribe(uploadOnSubscribe);

        //创建表单主体
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("upload", file.getName(), uploadRequestBody);

        //上传
        T service = Box.getRetrofit(uploadServiceClass);

        try {
            //获得上传方法的参数类型   和参数
            Class[] paramClasses = new Class[params.length + 1];
            Object[] uploadParams = new Object[params.length + 1];
            paramClasses[params.length] = MultipartBody.Part.class;
            uploadParams[params.length] = filePart;
            for (int i = 0; i < params.length; i++) {
                paramClasses[i] = params[i].getClass();
                uploadParams[i] = params[i];
            }

            //获得上传方法
            Method uploadMethod = uploadServiceClass.getMethod(uploadFucntionName, paramClasses);

            //运行上传方法
            Object o = uploadMethod.invoke(service, uploadParams);
            if (o instanceof Flowable) {
                Flowable uploadFlowable = (Flowable) o;

                //合并Observable
                return Flowable.merge(progressObservale, uploadFlowable)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Flowable.error(e);
        }
        return Flowable.error(new ServerException("no upload method found or api service error", ErrorType.UNKNOWN));
    }

    /**
     * 上传多个文件
     *
     * 用法：
     * 1public interface FilesUploadService {
     *   @Multipart
     *   @POST("/uploads")
     *   Flowable<HttpResult<ArrayList<FileBean>>> uploads(@Part ArrayList<MultipartBody.Part> files);
     *
     *   }
     */
    public static <T> Flowable<T> uploadFiles(ArrayList<File> files, Class<T> uploadsServiceClass, String uploadFucntionName, Object... params) {
        //总长度
        long sumLength = 0L;
        for (File file : files) {
            sumLength += file.length();
        }

        //进度Observable
        UploadOnSubscribe uploadOnSubscribe = new UploadOnSubscribe(sumLength);
        Flowable<Integer> progressObservale = Flowable.create(uploadOnSubscribe, BackpressureStrategy.BUFFER);

        ArrayList<MultipartBody.Part> fileParts = new ArrayList<>();

        for (File file : files) {

            UploadRequestBody uploadRequestBody = new UploadRequestBody(file);
            //设置进度监听
            uploadRequestBody.setUploadOnSubscribe(uploadOnSubscribe);

            fileParts.add(MultipartBody.Part.createFormData("upload", file.getName(), uploadRequestBody));
        }

        //上传
        T service =  Box.getRetrofit(uploadsServiceClass);
        try {
            //获得上传方法的参数类型   和参数
            Class[] paramClasses = new Class[params.length + 1];
            Object[] uploadParams = new Object[params.length + 1];
            paramClasses[params.length] = ArrayList.class;
            uploadParams[params.length] = fileParts;
            for (int i = 0; i < params.length; i++) {
                paramClasses[i] = params[i].getClass();
                uploadParams[i] = params[i];
            }

            //获得上传方法
            Method uploadMethod = uploadsServiceClass.getMethod(uploadFucntionName, paramClasses);

            //运行上传方法
            Object o = uploadMethod.invoke(service, uploadParams);
            if (o instanceof Flowable) {
                Flowable uploadFlowable = (Flowable) o;

                //合并Observable
                return Flowable.merge(progressObservale, uploadFlowable)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Flowable.error(e);
        }
        return Flowable.error(new ServerException("no upload method found or api service error", ErrorType.UNKNOWN));
    }
}
