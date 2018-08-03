package com.example.lenovo.rto.unit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by huyin on 2017/9/12.
 */

public class Unit {

     private QuResBean qu_res;
     private SchemaBean schema;
     private String session_id;
     private List<ActionListBean> action_list;

     public QuResBean getQu_res() {
          return qu_res;
     }

     public void setQu_res(QuResBean qu_res) {
          this.qu_res = qu_res;
     }

     public SchemaBean getSchema() {
          return schema;
     }

     public void setSchema(SchemaBean schema) {
          this.schema = schema;
     }

     public String getSession_id() {
          return session_id;
     }

     public void setSession_id(String session_id) {
          this.session_id = session_id;
     }

     public List<ActionListBean> getAction_list() {
          return action_list;
     }

     public void setAction_list(List<ActionListBean> action_list) {
          this.action_list = action_list;
     }

     public static class QuResBean {

          private String log_id;
          private String raw_query;
          private int status;
          private int timestamp;
          private List<IntentCandidatesBean> intent_candidates;

          public String getLog_id() {
               return log_id;
          }

          public void setLog_id(String log_id) {
               this.log_id = log_id;
          }

          public String getRaw_query() {
               return raw_query;
          }

          public void setRaw_query(String raw_query) {
               this.raw_query = raw_query;
          }

          public int getStatus() {
               return status;
          }

          public void setStatus(int status) {
               this.status = status;
          }

          public int getTimestamp() {
               return timestamp;
          }

          public void setTimestamp(int timestamp) {
               this.timestamp = timestamp;
          }

          public List<IntentCandidatesBean> getIntent_candidates() {
               return intent_candidates;
          }

          public void setIntent_candidates(List<IntentCandidatesBean> intent_candidates) {
               this.intent_candidates = intent_candidates;
          }

          public static class IntentCandidatesBean {

               private Object extra_info;
               private String from_who;
               private String func_slot;
               private String intent;
               private double intent_confidence;
               private boolean intent_need_clarify;
               private String match_info;
               private List<SlotsBean> slots;

               public Object getExtra_info() {
                    return extra_info;
               }

               public void setExtra_info(Object extra_info) {
                    this.extra_info = extra_info;
               }

               public String getFrom_who() {
                    return from_who;
               }

               public void setFrom_who(String from_who) {
                    this.from_who = from_who;
               }

               public String getFunc_slot() {
                    return func_slot;
               }

               public void setFunc_slot(String func_slot) {
                    this.func_slot = func_slot;
               }

               public String getIntent() {
                    return intent;
               }

               public void setIntent(String intent) {
                    this.intent = intent;
               }

               public double getIntent_confidence() {
                    return intent_confidence;
               }

               public void setIntent_confidence(double intent_confidence) {
                    this.intent_confidence = intent_confidence;
               }

               public boolean isIntent_need_clarify() {
                    return intent_need_clarify;
               }

               public void setIntent_need_clarify(boolean intent_need_clarify) {
                    this.intent_need_clarify = intent_need_clarify;
               }

               public String getMatch_info() {
                    return match_info;
               }

               public void setMatch_info(String match_info) {
                    this.match_info = match_info;
               }

               public List<SlotsBean> getSlots() {
                    return slots;
               }

               public void setSlots(List<SlotsBean> slots) {
                    this.slots = slots;
               }

               public static class SlotsBean {

                    private double confidence;
                    private int length;
                    private boolean need_clarify;
                    private String normalized_word;
                    private int offset;
                    private String original_word;
                    private String type;
                    private String word_type;

                    public double getConfidence() {
                         return confidence;
                    }

                    public void setConfidence(double confidence) {
                         this.confidence = confidence;
                    }

                    public int getLength() {
                         return length;
                    }

                    public void setLength(int length) {
                         this.length = length;
                    }

                    public boolean isNeed_clarify() {
                         return need_clarify;
                    }

                    public void setNeed_clarify(boolean need_clarify) {
                         this.need_clarify = need_clarify;
                    }

                    public String getNormalized_word() {
                         return normalized_word;
                    }

                    public void setNormalized_word(String normalized_word) {
                         this.normalized_word = normalized_word;
                    }

                    public int getOffset() {
                         return offset;
                    }

                    public void setOffset(int offset) {
                         this.offset = offset;
                    }

                    public String getOriginal_word() {
                         return original_word;
                    }

                    public void setOriginal_word(String original_word) {
                         this.original_word = original_word;
                    }

                    public String getType() {
                         return type;
                    }

                    public void setType(String type) {
                         this.type = type;
                    }

                    public String getWord_type() {
                         return word_type;
                    }

                    public void setWord_type(String word_type) {
                         this.word_type = word_type;
                    }
               }
          }
     }

     public static class SchemaBean {

          private String current_qu_intent;
          private double intent_confidence;
          private List<BotMergedSlotsBean> bot_merged_slots;

          public String getCurrent_qu_intent() {
               return current_qu_intent;
          }

          public void setCurrent_qu_intent(String current_qu_intent) {
               this.current_qu_intent = current_qu_intent;
          }

          public double getIntent_confidence() {
               return intent_confidence;
          }

          public void setIntent_confidence(double intent_confidence) {
               this.intent_confidence = intent_confidence;
          }

          public List<BotMergedSlotsBean> getBot_merged_slots() {
               return bot_merged_slots;
          }

          public void setBot_merged_slots(List<BotMergedSlotsBean> bot_merged_slots) {
               this.bot_merged_slots = bot_merged_slots;
          }

          public static class BotMergedSlotsBean {

               private int begin;
               private int confidence;
               private int length;
               private String merge_method;
               private String normalized_word;
               private String original_word;
               private int session_offset;
               private String type;
               private String word_type;

               public int getBegin() {
                    return begin;
               }

               public void setBegin(int begin) {
                    this.begin = begin;
               }

               public int getConfidence() {
                    return confidence;
               }

               public void setConfidence(int confidence) {
                    this.confidence = confidence;
               }

               public int getLength() {
                    return length;
               }

               public void setLength(int length) {
                    this.length = length;
               }

               public String getMerge_method() {
                    return merge_method;
               }

               public void setMerge_method(String merge_method) {
                    this.merge_method = merge_method;
               }

               public String getNormalized_word() {
                    return normalized_word;
               }

               public void setNormalized_word(String normalized_word) {
                    this.normalized_word = normalized_word;
               }

               public String getOriginal_word() {
                    return original_word;
               }

               public void setOriginal_word(String original_word) {
                    this.original_word = original_word;
               }

               public int getSession_offset() {
                    return session_offset;
               }

               public void setSession_offset(int session_offset) {
                    this.session_offset = session_offset;
               }

               public String getType() {
                    return type;
               }

               public void setType(String type) {
                    this.type = type;
               }

               public String getWord_type() {
                    return word_type;
               }

               public void setWord_type(String word_type) {
                    this.word_type = word_type;
               }
          }
     }

     public static class ActionListBean {

          @SerializedName("action_id")
          private String actionId;
          @SerializedName("action_type")
          private ActionTypeBean actionType;
          @SerializedName("code_actions")
          private CodeActionsBean codeActions;
          private int confidence;
          @SerializedName("main_exe")
          private String mainExe;
          private String say;
          @SerializedName("arg_list")
          private List<?> argList;
          @SerializedName("exe_status")
          private List<?> exeStatus;
          @SerializedName("hint_list")
          private List<HintListBean> hintList;

          public String getActionId() {
               return actionId;
          }

          public void setActionId(String actionId) {
               this.actionId = actionId;
          }

          public ActionTypeBean getActionType() {
               return actionType;
          }

          public void setActionType(ActionTypeBean actionType) {
               this.actionType = actionType;
          }

          public CodeActionsBean getCodeActions() {
               return codeActions;
          }

          public void setCodeActions(CodeActionsBean codeActions) {
               this.codeActions = codeActions;
          }

          public int getConfidence() {
               return confidence;
          }

          public void setConfidence(int confidence) {
               this.confidence = confidence;
          }

          public String getMainExe() {
               return mainExe;
          }

          public void setMainExe(String mainExe) {
               this.mainExe = mainExe;
          }

          public String getSay() {
               return say;
          }

          public void setSay(String say) {
               this.say = say;
          }

          public List<?> getArgList() {
               return argList;
          }

          public void setArgList(List<?> argList) {
               this.argList = argList;
          }

          public List<?> getExeStatus() {
               return exeStatus;
          }

          public void setExeStatus(List<?> exeStatus) {
               this.exeStatus = exeStatus;
          }

          public List<HintListBean> getHintList() {
               return hintList;
          }

          public void setHintList(List<HintListBean> hintList) {
               this.hintList = hintList;
          }

          public static class ActionTypeBean {

               @SerializedName("act_target")
               private String actTarget;
               @SerializedName("act_target_detail")
               private String actTargetDetail;
               @SerializedName("act_type")
               private String actType;
               @SerializedName("act_type_detail")
               private String actTypeDetail;

               public String getActTarget() {
                    return actTarget;
               }

               public void setActTarget(String actTarget) {
                    this.actTarget = actTarget;
               }

               public String getActTargetDetail() {
                    return actTargetDetail;
               }

               public void setActTargetDetail(String actTargetDetail) {
                    this.actTargetDetail = actTargetDetail;
               }

               public String getActType() {
                    return actType;
               }

               public void setActType(String actType) {
                    this.actType = actType;
               }

               public String getActTypeDetail() {
                    return actTypeDetail;
               }

               public void setActTypeDetail(String actTypeDetail) {
                    this.actTypeDetail = actTypeDetail;
               }
          }

          public static class CodeActionsBean {
          }

          public static class HintListBean {

               @SerializedName("hint_query")
               private String hintQuery;

               public String getHintQuery() {
                    return hintQuery;
               }

               public void setHintQuery(String hintQuery) {
                    this.hintQuery = hintQuery;
               }
          }
     }
}
