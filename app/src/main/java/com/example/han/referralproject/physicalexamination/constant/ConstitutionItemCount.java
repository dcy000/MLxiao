package com.example.han.referralproject.physicalexamination.constant;

/**
 * Created by lenovo on 2018/5/9.
 */

public interface ConstitutionItemCount {
    /**
     * 体质类型:		条目数:
     * 平和质:		8
     * 气虚质:		8
     * 阳虚质:		7
     * 阴虚质:		8
     * 血瘀质:		7
     * 痰湿质:		8
     * 湿热质:		7
     * 气郁质:		7
     * 特禀质:		7
     * <p>
     * 判定标准:
     * 原始分=各个条目的分会相加。
     * 转化分数=[（原始分-条目数（每项问题数））/（条目数×4）] ×100
     */
    int A = 8;
    int B = 8;
    int C = 7;
    int D = 8;
    int E = 7;
    int F = 8;
    int G = 7;
    int H = 7;
    int I = 7;

    String A_DESC = "平和质";
    String B_DESC = "气虚质";
    String C_DESC = "阳虚质";
    String D_DESC = "阴虚质";
    String E_DESC = "血瘀质";
    String F_DESC = "痰湿质";
    String G_DESC = "湿热质";
    String H_DESC = "气郁质";
    String I_DESC = "特禀质";

    String A_MANTENANCE = "环境起居调摄：起居顺应四时阴阳，劳逸结合。\n" +
            "　　体育锻炼：适度运动即可。\n" +
            "　　精神调适：清净立志、开朗乐观、心理平衡。\n" +
            "　　饮食调理：食物宜多样化，不偏食，不可过饥过饱、偏寒偏热。\n" +
            "　　药物调养：不需。";
    String B_MANTENANCE = "环境起居调摄：热则耗气，夏当避暑；冬当避寒，以防感冒；避免过劳伤正气。\n" +
            "　　体育锻炼：起居宜柔缓，不宜剧烈运动以防耗气，应散步、慢跑、打太极、五禽戏等。\n" +
            "　　精神调适：气虚之人多神疲乏力、四肢酸懒，应清净养藏，祛除杂念，不躁动，少思虑。\n" +
            "　　饮食调理：常食益气健脾食物，如粳米、糯米、小米、大麦、山药、土豆、大枣、香菇、鸡肉、鹅肉、兔肉、鹌鹑、牛肉、青鱼、鲢鱼，少吃耗气食物如生萝卜、空心菜等。\n" +
            "　　药物调养：可用甘温补气之品，如人参、山药、黄芪等。脾气虚，宜选四君子汤，或参苓白术散；肺气虚，宜选补肺汤；肾气虚，多服肾气丸。\n";
    String C_MANTENANCE = "　环境起居调摄：冬避寒就温，春夏培补阳气，多日光浴。夏不露宿室外，眠不直吹电扇，开空调室内外温差不要过大，避免在树荫、水亭及过堂风大的过道久停，注意足下、背部及丹田部位的保暖。\n" +
            "　　体育锻炼：动则生阳，体育锻炼每天1至2次。宜舒缓柔和，如散步、慢跑、太极拳、五禽戏、八段锦等。冬天避免在大风大寒大雾大雪及空气污染的环境中锻炼。\n" +
            "　　精神调适：这类人常情绪不佳，肝阳虚者善恐、心阳虚者善悲。应保持沉静内敛，消除不良情绪。\n" +
            "　　饮食调理：宜食温阳食品如羊肉、狗肉、鹿肉、鸡肉，少吃西瓜等生冷食物。“春夏养阳”，夏日三伏每伏食附子粥或羊肉附子汤一次。平时可用当归生姜羊肉汤、韭菜炒胡桃仁。\n" +
            "　　药物调养：可选补阳祛寒、温养肝肾之品，如鹿茸、海狗肾、蛤蚧、冬虫夏草、巴戟天、仙茅、肉苁蓉、补骨脂、杜仲等，成方可选金匮肾气丸、右归丸。偏心阳虚者，桂枝甘草汤加肉桂常服，虚甚者可加人参；偏脾阳虚者可选择理中丸或附子理中丸。\n";
    String D_MANTENANCE = "环境起居调摄：夏应避暑，多去海边高山。秋冬要养阴。居室应安静。不熬夜，不剧烈运动，不在高温下工作。\n" +
            "　　体育锻炼：宜选动静结合项目，如太极拳、八段锦等。控制出汗量，及时补水。\n" +
            "　　精神调适：循《内经》“恬澹虚无”、“精神内守”之法，养成冷静沉着的习惯。对非原则性问题，少与人争，少参加争胜负的文娱活动。\n" +
            "　　饮食调理：多食梨、百合、银耳、木瓜、菠菜、无花果、冰糖、茼蒿等甘凉滋润食物，喝沙参粥、百合粥、枸杞粥、桑椹粥、山药粥。少吃葱、姜、蒜、椒等辛辣燥烈品。\n" +
            "　　药物调养：可用滋阴清热、滋养肝肾之品，如女贞子、山茱萸、五味子、旱莲草、麦门冬、天门冬、黄精、玉竹、枸杞子等药。常用方有六味地黄丸、大补阴丸等。如肺阴虚，宜服百合固金汤；心阴虚，宜服天王补心丸；脾阴虚，宜服慎柔养真汤；肾阴虚，宜服六味丸；肝阴虚，宜服一贯煎。\n";
    String E_MANTENANCE = "　环境起居调摄：远离潮湿；阴雨季避湿邪侵袭；多户外活动；穿透气散湿的棉衣；常晒太阳。\n" +
            "　　体育锻炼：身重易倦，应长期坚持锻炼，如散步、慢跑、球类、武术、八段锦及舞蹈等。活动量应逐渐增强，让疏松的皮肉逐渐结实致密。\n" +
            "　　精神调适：易神疲困顿，要多参加各种活动，多听轻松音乐，以动养神。\n" +
            "　　饮食调理：少食甜粘油腻，少喝酒勿过饱。多食健脾利湿化痰祛湿的清淡食物，如白萝卜、葱、姜、白果、红小豆等。\n" +
            "　　药物调养：重点调补肺脾肾。可用温燥化湿之品，如半夏、茯苓、泽泻、瓜蒌、白术、车前子等。若肺失宣降，当宣肺化痰，选二陈汤；若脾不健运，当健脾化痰，选六君子汤或香砂六君子汤；若肾不温化，当选苓桂术甘汤。\n";
    String F_MANTENANCE = "环境起居调摄：避暑湿，环境宜干燥通风，不宜熬夜过劳，长夏应避湿热侵袭。\n" +
            "　　体育锻炼：适合高强度大运动量锻炼，如中长跑、游泳、爬山、球类等，以湿祛散热。夏季应凉爽时锻炼。\n" +
            "　　精神调适：多参加开朗轻松的活动，放松身心。\n" +
            "　　饮食调理：多吃西红柿、草莓、黄瓜、绿豆、芹菜、薏米、苦瓜、茵陈蒿等物，饮石竹茶。忌辛温滋腻，少喝酒，少吃海鲜。\n" +
            "　　药物调养：可用甘淡苦寒清热利湿之品，如黄芩、黄连、龙胆草、虎杖、栀子等。方药可选龙胆泄肝汤、茵陈蒿汤等。\n";
    String G_MANTENANCE = "环境起居调摄：血得温则行，居住宜温不宜凉；冬应防寒。作息规律，睡眠足够，不可过逸免气滞血瘀。\n" +
            "　　体育锻炼：多做益心脏血脉的活动，如舞蹈、太极拳、八段锦、保健按摩等，各部分都要活动，以助气血运行。\n" +
            "　　精神调适：培养乐观情绪，则气血和畅，有利血瘀改善，苦闷忧郁会加重血瘀。\n" +
            "　　饮食调理：常食红糖、丝瓜、玫瑰花、月季花、酒、桃仁等活血祛瘀的食物，酒可少量常饮，醋可多吃，宜喝山楂粥、花生粥。\n" +
            "　　药物调养：可用当归、川芎、怀牛膝、徐长卿、鸡血藤、茺蔚子等活血养血的药物，成方可选四物汤等。\n";
    String H_MANTENANCE = "环境起居调摄：室内常通风，装修宜明快亮丽。阴雨天调节好情绪。\n" +
            "　　体育锻炼：宜动不宜静，多跑步、爬山、武术、游泳等以流通气血。着意锻炼呼吸吐纳功法，以开导郁滞。\n" +
            "　　精神调适：“喜胜忧”，要主动寻快乐，常看喜剧、励志剧、听相声，勿看悲苦剧。多听轻松开朗音乐，多社交活动以开朗豁达。\n" +
            "　　饮食调理：少饮酒以活动血脉提情绪。多食行气食物，如佛手、橙子、柑皮、荞麦、韭菜、茴香菜、大蒜、高粱、刀豆等。\n" +
            "　　药物调养：常用香附、乌药、川楝子、小茴香、青皮、郁金等疏肝理气解郁的药为主组成方剂，如越鞠丸等。若气郁引起血瘀，当配伍活血化瘀药。\n";
    String I_MANTENANCE = "特禀体质情况更复杂，要根据相关体质特征予以调养。\n" +
            "　　很多人可能都是复合体质，所以上述方法仅供参考，尤其是药物调养的内容，一定要谨遵医嘱。\n";


    String A_FEATURE = "形体特征：体形匀称健壮。 \n" +
            "　　　　常见表现：面色肤色润泽，头发稠密有光泽，目光有神。鼻色明润，嗅觉通利，唇色红润，无口气。不容易疲劳，精力充沛。寒热均有较好的耐受力，睡眠良好，胃口好。大小便正常。观察舌头颜色，呈淡红，舌苔薄而白，脉和而有神。 \n" +
            "　　 \n" +
            "　　　　发病倾向：平时患病少。 \n" +
            "　　 \n" +
            "　　　　对外界环境适应能力：对自然环境和社会环境的适应能力比较强。\n";
    String B_FEATURE = "　形体特征：肌肉不健壮。 \n" +
            "　　 \n" +
            "　　　　常见表现：语音低怯、气短懒言，肢体容易疲乏，精神不振，容易出汗。舌头呈淡红色，舌体显胖大，舌边缘有齿印痕，脉象虚缓。容易头晕、健忘。有的人大便正常，有的人大便稀烂，便后仍感觉“没拉完”。小便则正常或者量、次数偏多。 \n" +
            "　　 \n" +
            "　　　　心理特征：性格内向、情绪不稳定，胆小不喜欢冒险。 \n" +
            "　　 \n" +
            "　　　　发病倾向：平素体质虚弱，容易感冒。还容易患内脏下垂、虚劳等病。 \n" +
            "　　 \n" +
            "　　　　对外界环境适应能力：不耐受寒邪、风邪、暑邪。\n";
    String C_FEATURE = "形体特征：体形白胖，肌肉不结实。 \n" +
            "　　 \n" +
            "　　　　常见表现：平时怕冷，手足“热力不足”，喜欢热饮热食，精神不振，睡眠偏多。舌头颜色偏淡，略显胖大，边缘有齿印痕，舌苔湿润。脉象沉迟微弱。有些人面色柔白，常带“熊猫眼”，唇色淡，头发容易脱落，容易出汗。大便多稀烂，少量多次，尿则清长。由于怕冷，有些人睡觉常缩成虾状。 \n" +
            "　　 \n" +
            "　　　　心理特征：性格多沉静、内向。 \n" +
            "　　 \n" +
            "　　　　发病倾向：发病多为寒证，或者容易出现痰饮、肿胀、腹泻等。 \n" +
            "　　 \n" +
            "　　　　对外界环境适应能力：不耐受寒邪和湿邪，耐夏不耐冬。 \n";
    String D_FEATURE = "形体特征：体形瘦长。 \n" +
            "　　 \n" +
            "　　　　常见表现：手足心热、平时容易口燥热，咽喉干涩，口渴爱喝冷饮。鼻腔偏干，鼻涕少。大便干燥、舌头红，口水偏少，舌苔偏少。有些人还会面色潮红，心有烘热感，眼睛干涩、看物发花，皮肤偏干燥，因而更容易生皱纹。有些人会出现眩晕耳鸣，睡眠质量差，小便短而不畅，脉象“细弦而数”。 \n" +
            "　　 \n" +
            "　　　　心理特征：性情急躁、外向活泼好动。 \n" +
            "　　 \n" +
            "　　　　发病倾向：容易出现阴亏燥热的病变，或者病后表现为阴亏。 \n" +
            "　　 \n" +
            "　　　　对外界环境适应能力：不耐热邪，耐冬不耐夏，恰恰和阳虚者相反，也不耐受燥邪。\n";
    String E_FEATURE = "形体特征：体形肥胖，尤其是腹部肥胖松软。 \n" +
            "　　 \n" +
            "　　　　常见表现：面部皮肤油脂较多，汗水多且粘，容易胸闷，痰多。有些人面色淡黄发暗，眼圈微浮肿，容易困倦。舌头胖大，舌苔白腻，嘴里常有粘、发腻、发甜的感觉。平时比较爱吃甜食和肥腻食物。大便正常或者略稀烂，小便量不多或者颜色稍微有些浑浊。脉象滑。 \n" +
            "　　 \n" +
            "　　　　心理特征：性格偏温和、稳重，多善于忍耐。 \n" +
            "　　 \n" +
            "　　　　发病倾向：和其他体质相比，比较容易发展为“消渴”（糖尿病）、中风、胸痹。 \n" +
            "　　 \n" +
            "　　　　对外界环境适应能力：最怵梅雨季节以及湿润环境。\n";
    String F_FEATURE = "形体特征：形体偏胖或者很瘦。 \n" +
            "　　 \n" +
            "　　　　常见表现：平时面部常有油光，容易生痤疮粉刺。舌头颜色偏红，舌苔黄腻，容易口苦口干，身体感沉重容易疲倦。有些人还会心烦意乱，做事无精神，眼球血丝多，大便干燥硬结，或者显得比较粘，小便短而颜色发深，有些男性的阴囊显得比较潮湿，女性则白带增多。脉象多“滑数”，显得急促。 \n" +
            "　　 \n" +
            "　　　　心理特征：性格多急躁易怒。 \n" +
            "　　 \n" +
            "　　　　发病倾向：易患痤疮、火疖，常长疙瘩，也比较容易患黄疸、火热等症。 \n" +
            "　　 \n" +
            "　　　　对外界环境适应能力：难适应湿环境或者气温偏高，尤其是夏末秋初的“湿热交蒸”气候。\n";
    String G_FEATURE = "形体特征：瘦人居多。 \n" +
            "　　 \n" +
            "　　　　常见表现：面色灰暗，皮肤偏暗有色素沉着，容易出现瘀斑和疼痛。唇色暗淡或者发紫。舌头暗有点、片状瘀斑，舌头下静脉曲张，脉象则细涩。有些人眼眶暗黑，鼻子暗滞，头发容易脱落，肌肤发干。女性常常痛经、闭经，或者经血中有比较多凝结的血块，经血颜色紫黑有块状物，有些人甚至有出血倾向、吐血和崩漏。 \n" +
            "　　 \n" +
            "　　　　心理特征：心情容易烦躁，急躁健忘。 \n" +
            "　　 \n" +
            "　　　　发病倾向：容易患出血、中风、胸痹等疾病。 \n" +
            "　　 \n" +
            "　　　　对外界环境适应能力：怕风邪、寒邪。\n";
    String H_FEATURE = "形体特征：瘦者居多。 \n" +
            "　　 \n" +
            "　　　　常见表现：最多见的是性格内向不稳定，抑郁脆弱，敏感多疑，对精神刺激的适应能力较差，平时苦着脸。表情烦闷不开心。有些人胸部有胀痛感或者有疼痛游走感，常叹气、打嗝，或者咽喉总觉得不舒服，有东西梗着。有些女性乳房胀痛。睡眠较差，食欲减退，健忘，痰多，大便多发干，小便正常。舌头颜色淡红，舌苔薄而白，脉象弦细。 \n" +
            "　　 \n" +
            "　　　　心理特征：与上述“常见表现”相同。 \n" +
            "　　 \n" +
            "　　　　发病倾向：容易患抑郁、脏躁、不寐（失眠）、惊恐等。 \n" +
            "　　 \n" +
            "　　　　对外界环境适应能力：不喜欢阴雨天气，对精神刺激的适应能力较差。\n";
    String I_FEATURE = "形体特征：有的畸形，有先天生理缺陷或者外表无特殊。 \n" +
            "　　 \n" +
            "　　　　常见表现：遗传性疾病有垂直遗传，家族共同特征等，胎传性疾病为母体影响胎儿个体生长发育以及相关疾病特征。 \n" +
            "　　 \n" +
            "　　　　心理特征：因疾病各有不同。 \n" +
            "　　 \n" +
            "　　　　发病倾向：过敏体质者容易药物过敏、患花粉症，遗传疾病如血友病、先天愚型等。胎传疾病包括胎热、胎赤、胎惊、胎肥、胎痫、胎弱、发育迟缓等。 \n" +
            "　　 \n" +
            "　　　　对外界环境适应能力：很差，尤其是过敏体质者，季节变化可诱发宿疾发作。\n";


}
