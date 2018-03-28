package com.example.han.referralproject.tool.xfparsebean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2018/2/27.
 */

public class WordFindingBean implements Serializable {

    /**
     * antonym : ["享受","快活"]
     * synonym : ["焦虑","痛苦"]
     * source : iflytek
     * word : 煎熬
     */

    public String source;
    public String word;
    public List<String> antonym;
    public List<String> synonym;
}
