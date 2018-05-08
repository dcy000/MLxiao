package com.example.han.referralproject.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/4.
 */

public class HypertensionDetection implements Serializable{

    /**
     * primary : {"name":"原发性检测","list":{"genetic":"你父母、兄弟是否也患有高血压？","drinkWine":"你是否经常饮酒？","mentalStress":"你是长期从事高度精神紧张工作？","naSalt":"你是否偏好吃咸？","sport":"你是经常运动？"}}
     * secondary : {"name":"继发性检测","list":{"drugInduced":"你是否有长期口服药物，减压效果差，不易控制？","renalIllness":"你是否有夜尿增多，血尿、泡沫尿或有肾脏疾病史？","endocrineIllness":"你是否有阵发性高血压，发作时伴有头痛、心悸、皮肤苍白及多汗？","prrs":"你是否有夜间睡眠时打鼾并出现呼吸暂停？"}}
     * receptor : {"name":"靶器官检测","list":{"eye":"眼部是否出现血管渗出、黄斑和视盘的水肿、眼球后部的出血等症状？","kidney":"是否有劳力性呼吸困难、平卧时出现气急，坐起后即好转，右上腹疼痛，并有肝大等症状？","encephalon":"是否有夜尿增多，伴微量白蛋白尿，或血尿等症状？","heart":"是否出现过头痛、抽搐和意识障碍等症状？"}}
     */

    private PrimaryBean primary;
    private SecondaryBean secondary;
    private ReceptorBean receptor;

    public PrimaryBean getPrimary() {
        return primary;
    }

    public void setPrimary(PrimaryBean primary) {
        this.primary = primary;
    }

    public SecondaryBean getSecondary() {
        return secondary;
    }

    public void setSecondary(SecondaryBean secondary) {
        this.secondary = secondary;
    }

    public ReceptorBean getReceptor() {
        return receptor;
    }

    public void setReceptor(ReceptorBean receptor) {
        this.receptor = receptor;
    }

    public static class PrimaryBean implements Serializable{
        /**
         * name : 原发性检测
         * list : {"genetic":"你父母、兄弟是否也患有高血压？","drinkWine":"你是否经常饮酒？","mentalStress":"你是长期从事高度精神紧张工作？","naSalt":"你是否偏好吃咸？","sport":"你是经常运动？"}
         */

        private String name;
        private ListBean list;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ListBean getList() {
            return list;
        }

        public void setList(ListBean list) {
            this.list = list;
        }

        public static class ListBean implements Serializable{
            /**
             * genetic : 你父母、兄弟是否也患有高血压？
             * drinkWine : 你是否经常饮酒？
             * mentalStress : 你是长期从事高度精神紧张工作？
             * naSalt : 你是否偏好吃咸？
             * sport : 你是经常运动？
             */

            private String genetic;
            private String drinkWine;
            private String mentalStress;
            private String naSalt;
            private String sport;

            public String getGenetic() {
                return genetic;
            }

            public void setGenetic(String genetic) {
                this.genetic = genetic;
            }

            public String getDrinkWine() {
                return drinkWine;
            }

            public void setDrinkWine(String drinkWine) {
                this.drinkWine = drinkWine;
            }

            public String getMentalStress() {
                return mentalStress;
            }

            public void setMentalStress(String mentalStress) {
                this.mentalStress = mentalStress;
            }

            public String getNaSalt() {
                return naSalt;
            }

            public void setNaSalt(String naSalt) {
                this.naSalt = naSalt;
            }

            public String getSport() {
                return sport;
            }

            public void setSport(String sport) {
                this.sport = sport;
            }
        }
    }

    public static class SecondaryBean implements Serializable{
        /**
         * name : 继发性检测
         * list : {"drugInduced":"你是否有长期口服药物，减压效果差，不易控制？","renalIllness":"你是否有夜尿增多，血尿、泡沫尿或有肾脏疾病史？","endocrineIllness":"你是否有阵发性高血压，发作时伴有头痛、心悸、皮肤苍白及多汗？","prrs":"你是否有夜间睡眠时打鼾并出现呼吸暂停？"}
         */

        private String name;
        private ListBeanX list;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ListBeanX getList() {
            return list;
        }

        public void setList(ListBeanX list) {
            this.list = list;
        }

        public static class ListBeanX implements Serializable{
            /**
             * drugInduced : 你是否有长期口服药物，减压效果差，不易控制？
             * renalIllness : 你是否有夜尿增多，血尿、泡沫尿或有肾脏疾病史？
             * endocrineIllness : 你是否有阵发性高血压，发作时伴有头痛、心悸、皮肤苍白及多汗？
             * prrs : 你是否有夜间睡眠时打鼾并出现呼吸暂停？
             */

            private String drugInduced;
            private String renalIllness;
            private String endocrineIllness;
            private String prrs;

            public String getDrugInduced() {
                return drugInduced;
            }

            public void setDrugInduced(String drugInduced) {
                this.drugInduced = drugInduced;
            }

            public String getRenalIllness() {
                return renalIllness;
            }

            public void setRenalIllness(String renalIllness) {
                this.renalIllness = renalIllness;
            }

            public String getEndocrineIllness() {
                return endocrineIllness;
            }

            public void setEndocrineIllness(String endocrineIllness) {
                this.endocrineIllness = endocrineIllness;
            }

            public String getPrrs() {
                return prrs;
            }

            public void setPrrs(String prrs) {
                this.prrs = prrs;
            }
        }
    }

    public static class ReceptorBean implements Serializable{
        /**
         * name : 靶器官检测
         * list : {"eye":"眼部是否出现血管渗出、黄斑和视盘的水肿、眼球后部的出血等症状？","kidney":"是否有劳力性呼吸困难、平卧时出现气急，坐起后即好转，右上腹疼痛，并有肝大等症状？","encephalon":"是否有夜尿增多，伴微量白蛋白尿，或血尿等症状？","heart":"是否出现过头痛、抽搐和意识障碍等症状？"}
         */

        private String name;
        private ListBeanXX list;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ListBeanXX getList() {
            return list;
        }

        public void setList(ListBeanXX list) {
            this.list = list;
        }

        public static class ListBeanXX implements Serializable{
            /**
             * eye : 眼部是否出现血管渗出、黄斑和视盘的水肿、眼球后部的出血等症状？
             * kidney : 是否有劳力性呼吸困难、平卧时出现气急，坐起后即好转，右上腹疼痛，并有肝大等症状？
             * encephalon : 是否有夜尿增多，伴微量白蛋白尿，或血尿等症状？
             * heart : 是否出现过头痛、抽搐和意识障碍等症状？
             */

            private String eye;
            private String kidney;
            private String encephalon;
            private String heart;

            public String getEye() {
                return eye;
            }

            public void setEye(String eye) {
                this.eye = eye;
            }

            public String getKidney() {
                return kidney;
            }

            public void setKidney(String kidney) {
                this.kidney = kidney;
            }

            public String getEncephalon() {
                return encephalon;
            }

            public void setEncephalon(String encephalon) {
                this.encephalon = encephalon;
            }

            public String getHeart() {
                return heart;
            }

            public void setHeart(String heart) {
                this.heart = heart;
            }
        }
    }
}
