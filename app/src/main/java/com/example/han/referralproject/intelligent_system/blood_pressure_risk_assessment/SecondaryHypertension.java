package com.example.han.referralproject.intelligent_system.blood_pressure_risk_assessment;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2018/5/7.
 */

public class SecondaryHypertension implements Parcelable{

    /**
     * illnessType : 1
     * primary : null
     * secondary : {"analysis":"根据您的检测结果，您有可能是继发性高血压，并且可能存在心、脑、肾脏、眼的靶器官损害，引起疾病原因可能是：肾性、内分泌性、阻塞性睡眠呼吸暂停低通气综合征、药源性引发的高血压，建议去医院做如下检测进行确诊","illnessFactor":[{"name":"肾性","info":"实验室检查包括：血、尿常规；血电解质（钠、钾、氯）、肌酐、尿酸、血糖、血脂； 24小时尿蛋白定量或尿白蛋白/肌酐比值（ACR）、12小时尿沉渣检查，如发现蛋白尿、血尿及尿白细胞增加，则需进一步行中段尿细菌培养、尿蛋白电泳、尿相差显微镜检查，明确尿蛋白、红细胞来源及排除感染；肾脏B超：了解肾脏大小、形态及有无肿瘤；如发现肾脏体积及形态异常，或发现肿物，则需进一步做肾动脉CTA/MRA以确诊并查病因；眼底检查；有条件的医院可行肾脏穿刺及病理学检查。"},{"name":"内分泌性","info":"血浆醛固酮与肾素活性测定及比值（ARR）、盐水试验、卡托普利试验、肾上腺静脉取血激素测定；血、尿儿茶酚胺，肾上腺等部位CT，血皮质醇昼夜节律、大小剂量地塞米松试验、胰岛素样生长因子测定、OGTT同时测定血糖及生长激素等。"},{"name":"阻塞性睡眠呼吸暂停低通气综合征","info":"多导睡眠监测是诊断OSAHS的\u201c金标准\u201d。诊断标准为每晚7小时睡眠中，呼吸暂停及低通气反复发作在30次以上和（或）呼吸暂停低通气指数≥5次/小时；呼吸暂停是指口鼻气流停止10秒以上；低通气是指呼吸气流降低到基础值的50%以下并伴有血氧饱和度下降超过4%。"},{"name":"药源性"}]}
     * targets : ["心","脑","肾脏","眼"]
     */

    private String illnessType;
    private SecondaryBean secondary;
    private List<String> targets;

    protected SecondaryHypertension(Parcel in) {
        illnessType = in.readString();
        secondary = in.readParcelable(SecondaryBean.class.getClassLoader());
        targets = in.createStringArrayList();
    }

    public static final Creator<SecondaryHypertension> CREATOR = new Creator<SecondaryHypertension>() {
        @Override
        public SecondaryHypertension createFromParcel(Parcel in) {
            return new SecondaryHypertension(in);
        }

        @Override
        public SecondaryHypertension[] newArray(int size) {
            return new SecondaryHypertension[size];
        }
    };

    public String getIllnessType() {
        return illnessType;
    }

    public void setIllnessType(String illnessType) {
        this.illnessType = illnessType;
    }

    public SecondaryBean getSecondary() {
        return secondary;
    }

    public void setSecondary(SecondaryBean secondary) {
        this.secondary = secondary;
    }

    public List<String> getTargets() {
        return targets;
    }

    public void setTargets(List<String> targets) {
        this.targets = targets;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(illnessType);
        dest.writeParcelable(secondary, flags);
        dest.writeStringList(targets);
    }

    public static class SecondaryBean implements Parcelable{
        /**
         * analysis : 根据您的检测结果，您有可能是继发性高血压，并且可能存在心、脑、肾脏、眼的靶器官损害，引起疾病原因可能是：肾性、内分泌性、阻塞性睡眠呼吸暂停低通气综合征、药源性引发的高血压，建议去医院做如下检测进行确诊
         * illnessFactor : [{"name":"肾性","info":"实验室检查包括：血、尿常规；血电解质（钠、钾、氯）、肌酐、尿酸、血糖、血脂； 24小时尿蛋白定量或尿白蛋白/肌酐比值（ACR）、12小时尿沉渣检查，如发现蛋白尿、血尿及尿白细胞增加，则需进一步行中段尿细菌培养、尿蛋白电泳、尿相差显微镜检查，明确尿蛋白、红细胞来源及排除感染；肾脏B超：了解肾脏大小、形态及有无肿瘤；如发现肾脏体积及形态异常，或发现肿物，则需进一步做肾动脉CTA/MRA以确诊并查病因；眼底检查；有条件的医院可行肾脏穿刺及病理学检查。"},{"name":"内分泌性","info":"血浆醛固酮与肾素活性测定及比值（ARR）、盐水试验、卡托普利试验、肾上腺静脉取血激素测定；血、尿儿茶酚胺，肾上腺等部位CT，血皮质醇昼夜节律、大小剂量地塞米松试验、胰岛素样生长因子测定、OGTT同时测定血糖及生长激素等。"},{"name":"阻塞性睡眠呼吸暂停低通气综合征","info":"多导睡眠监测是诊断OSAHS的\u201c金标准\u201d。诊断标准为每晚7小时睡眠中，呼吸暂停及低通气反复发作在30次以上和（或）呼吸暂停低通气指数≥5次/小时；呼吸暂停是指口鼻气流停止10秒以上；低通气是指呼吸气流降低到基础值的50%以下并伴有血氧饱和度下降超过4%。"},{"name":"药源性"}]
         */

        private String analysis;
        private List<IllnessFactorBean> illnessFactor;

        protected SecondaryBean(Parcel in) {
            analysis = in.readString();
            illnessFactor = in.createTypedArrayList(IllnessFactorBean.CREATOR);
        }

        public static final Creator<SecondaryBean> CREATOR = new Creator<SecondaryBean>() {
            @Override
            public SecondaryBean createFromParcel(Parcel in) {
                return new SecondaryBean(in);
            }

            @Override
            public SecondaryBean[] newArray(int size) {
                return new SecondaryBean[size];
            }
        };

        public String getAnalysis() {
            return analysis;
        }

        public void setAnalysis(String analysis) {
            this.analysis = analysis;
        }

        public List<IllnessFactorBean> getIllnessFactor() {
            return illnessFactor;
        }

        public void setIllnessFactor(List<IllnessFactorBean> illnessFactor) {
            this.illnessFactor = illnessFactor;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(analysis);
            dest.writeTypedList(illnessFactor);
        }

        public static class IllnessFactorBean implements Parcelable{
            /**
             * name : 肾性
             * info : 实验室检查包括：血、尿常规；血电解质（钠、钾、氯）、肌酐、尿酸、血糖、血脂； 24小时尿蛋白定量或尿白蛋白/肌酐比值（ACR）、12小时尿沉渣检查，如发现蛋白尿、血尿及尿白细胞增加，则需进一步行中段尿细菌培养、尿蛋白电泳、尿相差显微镜检查，明确尿蛋白、红细胞来源及排除感染；肾脏B超：了解肾脏大小、形态及有无肿瘤；如发现肾脏体积及形态异常，或发现肿物，则需进一步做肾动脉CTA/MRA以确诊并查病因；眼底检查；有条件的医院可行肾脏穿刺及病理学检查。
             */

            private String name;
            private String info;

            protected IllnessFactorBean(Parcel in) {
                name = in.readString();
                info = in.readString();
            }

            public static final Creator<IllnessFactorBean> CREATOR = new Creator<IllnessFactorBean>() {
                @Override
                public IllnessFactorBean createFromParcel(Parcel in) {
                    return new IllnessFactorBean(in);
                }

                @Override
                public IllnessFactorBean[] newArray(int size) {
                    return new IllnessFactorBean[size];
                }
            };

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getInfo() {
                return info;
            }

            public void setInfo(String info) {
                this.info = info;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(name);
                dest.writeString(info);
            }
        }
    }
}
