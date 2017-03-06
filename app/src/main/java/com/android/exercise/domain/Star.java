package com.android.exercise.domain;

import java.util.List;

/**
 * Created by wangzhen on 2017/3/6.
 */

public class Star {

    /**
     * name : 狮子座
     * date : 2017年
     * year : 2017
     * mima : {"info":"梦想成真 自由放飞","text":["对大多数狮子座而言，未来一年将是梦想成真，终于扬眉吐气自由放飞的一年。过去两年，很多狮子座都因为定下了某个所有人都不看好的梦想而备受嘲笑和煎熬。但在近期，这个看似不可实现的梦想将因为你们坚忍不懈的努力终于成为现实，让所有人跌破眼镜。土天拱会持续作用在你们的梦想实现宫位，也让狮子们可以持续发力大展拳脚，一鼓作气更上一层楼。但毕竟木星还在狮子座星盘中相对弱势的位置徘徊，你也必须着眼实际，从眼前的小事做起，注意一些你平时懒于搭理的人际关系，注意团队建设，多写多练才将有机会让自己的意见影响他人，从而实现个人权威地位的建立。4月底开始，月亮北交点也将移位狮子座的命宫与合作婚姻宫轴线，一些狮子座将面临情感关系的挑战，或是因为自己提升太快而不想再妥协于一段过气关系中，或是进入一段相对独特的关系之中。等到10月10日木星进入天蝎之后，你将更加关注自己的内心需求与渴望，或是买房让自己安定下来，或是为另一半做出某种牺牲与取舍，以寻求最理想的情感关系。 "]}
     * career : ["对不少狮子座来说，今年将是事业发展面临抉择的一年。过去几年，其实很多狮子座都一直在试图改变旧有模式，尝试一条少有人走的路，而这条路将有望在今年走通。此后，你们也会希望在这条通过努力最终选定的道路上深度挖掘，以专业形象获得权威认可，并将此处变为进入全新行业的踏脚石。对创业者和企业家而言，今年可能是新旧交替的一年，新的产品或是服务上线，以专业形象立足获得行业肯定，但同期旧的产品也是麻烦不断，让你需要费力理顺当下关系，尤其从3月中旬开始到4月中旬一段时间更是兵荒马乱，必须从多次研讨和折腾，甚至不停出差与人交涉过程中找到真正适合自己的一条道路、形象或是产品。上班族则要做好因为项目亏钱而需要不停跑动乃至出差劳民伤财的准备。但好在只要熬过5月，事业方向将基本确定。在8月7月月偏食到8月22日日全食期间，部分想跳槽的狮子座也会收到相对满意的合作意向或是工作合同，下半年就开始可以安心享受胜利果实。  "]
     * love : ["随着4月底北交点移位进入狮子座，你们的感情运也将在此后的一年半中面临巨大的机会与转折。占星学上，月亮南北交点是与宿命和内心息息相关的重要指标，当其引动宫内食相也会带来相应宫位对应人生领域的巨大变化。而2017年8月7日和22日发生的狮子座月偏食和水瓶座日全食将引发狮子座对于如何做自己和如何与人合作相处议题的思考。相应这半个月前后也会成为最容易做出情感抉择的一段时间，部分狮子座可能决定尊重自己的喜好，离开一段已经不能让自己心动的感情做回自己；另一部分狮子座则可能突然认识到单打独斗未必是最佳选择，也许可以与另一个志同道合的人共同谱写走向未来的人生篇章。但应该当前土星一直停留在狮子座的恋情宫内，大多数狮子并没有太多心情投入谈情说爱，而选择以一种更加客观和务实的态度看待情感选择，也意味着唯有那些真正符合狮子座内心期待的对象才可能攻破你们的心房，你也会倾向于选择年长可靠对象或是不论是物质还是精神上都能给予你支持的强大对象。"]
     * health : ["身体上会有一些的疾病或是外伤，同时情绪方面要控制好，不要随意爆发。"]
     * finance : ["未来一年，因为人生格局正在剧烈变化，狮子座的财运也会随之产生明显的波动。一方面虽然原有的收入还能保持，但因为个人的人生格局拓展开始登上更大舞台，也对自己有了更多的期望，为了打造更加高大上的星相，当下的收入就会显得有些不够用。因此在做一些与收入有关事情时就会有更多考量。更会随着本年度三次水星逆行的进程将自己的工作、财务、生活脉络挨个整理一遍，之后找到更加适合自己的捞钱方式。总体而言，因为木星还在3宫，狮子座的现金流会整体强于上一年，想花钱时总有钱来也算是一种幸福。此外，因为土木拱一直共同作用在投资宫和未来宫内，投资运整体以长线趋势投资为主，可以少量配合一些短线小钱，但如想有更好的收益则应该耐心研究市场，看准趋势做好以至少半年为周期的准备，尤其在8月底至9月间最容易获得相对满意的正收益。"]
     * luckeyStone : 美国碧玉
     * future :
     * resultcode : 200
     * error_code : 0
     */

    private String name;
    private String date;
    private int year;
    private MimaBean mima;
    private String luckeyStone;
    private String future;
    private String resultcode;
    private int error_code;
    private List<String> career;
    private List<String> love;
    private List<String> health;
    private List<String> finance;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public MimaBean getMima() {
        return mima;
    }

    public void setMima(MimaBean mima) {
        this.mima = mima;
    }

    public String getLuckeyStone() {
        return luckeyStone;
    }

    public void setLuckeyStone(String luckeyStone) {
        this.luckeyStone = luckeyStone;
    }

    public String getFuture() {
        return future;
    }

    public void setFuture(String future) {
        this.future = future;
    }

    public String getResultcode() {
        return resultcode;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public List<String> getCareer() {
        return career;
    }

    public void setCareer(List<String> career) {
        this.career = career;
    }

    public List<String> getLove() {
        return love;
    }

    public void setLove(List<String> love) {
        this.love = love;
    }

    public List<String> getHealth() {
        return health;
    }

    public void setHealth(List<String> health) {
        this.health = health;
    }

    public List<String> getFinance() {
        return finance;
    }

    public void setFinance(List<String> finance) {
        this.finance = finance;
    }

    public static class MimaBean {
        /**
         * info : 梦想成真 自由放飞
         * text : ["对大多数狮子座而言，未来一年将是梦想成真，终于扬眉吐气自由放飞的一年。过去两年，很多狮子座都因为定下了某个所有人都不看好的梦想而备受嘲笑和煎熬。但在近期，这个看似不可实现的梦想将因为你们坚忍不懈的努力终于成为现实，让所有人跌破眼镜。土天拱会持续作用在你们的梦想实现宫位，也让狮子们可以持续发力大展拳脚，一鼓作气更上一层楼。但毕竟木星还在狮子座星盘中相对弱势的位置徘徊，你也必须着眼实际，从眼前的小事做起，注意一些你平时懒于搭理的人际关系，注意团队建设，多写多练才将有机会让自己的意见影响他人，从而实现个人权威地位的建立。4月底开始，月亮北交点也将移位狮子座的命宫与合作婚姻宫轴线，一些狮子座将面临情感关系的挑战，或是因为自己提升太快而不想再妥协于一段过气关系中，或是进入一段相对独特的关系之中。等到10月10日木星进入天蝎之后，你将更加关注自己的内心需求与渴望，或是买房让自己安定下来，或是为另一半做出某种牺牲与取舍，以寻求最理想的情感关系。 "]
         */

        private String info;
        private List<String> text;

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public List<String> getText() {
            return text;
        }

        public void setText(List<String> text) {
            this.text = text;
        }
    }
}
