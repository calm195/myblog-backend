package com.chrissy.core.dal;

/**
 * @author chrissy
 * @description 数据源上下文保存，存储当前选中的数据源
 * @date 2024/8/5 14:04
 */
public class DataSourceContextHolder {
    private static final ThreadLocal<DataSourceNode> CONTEXT_HOLDER = new InheritableThreadLocal<>();

    /**
     * 增加并切换数据源
     * @param dbType 数据源
     */
    public static void set(String dbType){
        DataSourceNode cur = CONTEXT_HOLDER.get();
        CONTEXT_HOLDER.set(new DataSourceNode(cur, dbType));
    }

    /**
     * 获取当前数据源
     * @return 当前数据源
     */
    public static String get(){
        DataSourceNode cur = CONTEXT_HOLDER.get();
        return cur == null ? null : cur.value;
    }

    /**
     * 增加并切换数据源
     * @param ds 数据源
     */
    public static void set(DataSource ds) {
        set(ds.name().toUpperCase());
    }

    /**
     * 退出当前数据源，恢复上次数据源
     */
    public static void reset() {
        DataSourceNode ds = CONTEXT_HOLDER.get();
        if (ds == null) {
            return;
        }

        if (ds.parent != null) {
            // 退出当前的数据源选择，切回去走上一次的数据源配置
            CONTEXT_HOLDER.set(ds.parent);
        } else {
            CONTEXT_HOLDER.remove();
        }
    }

    public static class DataSourceNode{
        DataSourceNode parent;
        String value;

        public DataSourceNode(DataSourceNode parent, String value){
            this.parent = parent;
            this.value = value;
        }
    }
}
