package javaapplication6;

public class QuestManager {
    public String getObjective(GameState state, Inventory inv) {
        if (state == GameState.COLLECT) {
            return "收集三样物品：陈年檀香、破碎的信札、生锈的铁剪";
        }
        if (state == GameState.FARM) {
            return "村外收集：山魈精粉(2) 与 瘴气核心(1)";
        }
        if (state == GameState.BOSS_INTRO || state == GameState.BOSS_FIGHT) {
            return "回村中心，面对索命阴差";
        }
        return "第一章完成";
    }
}
