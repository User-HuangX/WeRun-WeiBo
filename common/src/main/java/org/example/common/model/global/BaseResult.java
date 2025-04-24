package org.example.common.model.global;

import lombok.Data;


@Data
public class BaseResult<T> {
    private int code;
    private String msg;
    private T data;

    private static final long serialVersionUID = 1L;

    /** 状态码 */
    public static final String CODE_TAG = "code";

    /** 返回内容 */
    public static final String MSG_TAG = "msg";

    /** 数据对象 */
    public static final String DATA_TAG = "data";

    /**
     * 初始化一个新创建的 AjaxResult 对象，使其表示一个空消息。
     */
    public BaseResult()
    {
    }



    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param code 状态码
     * @param msg 返回内容
     * @param data 数据对象
     */
    public BaseResult(int code, String msg, T data)
    {
        this.code = code;
        this.msg = msg;
        if(data != null) this.data = data;
    }


    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static <T> BaseResult<T> success() {
        return BaseResult.success("操作成功");
    }

    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static <T> BaseResult<T> success(T data)
    {
        return BaseResult.success("操作成功", data);
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @return 成功消息
     */
    public static <T> BaseResult<T> success(String msg)
    {
        return BaseResult.success(msg, null);
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static <T> BaseResult<T> success(String msg, T data)
    {
        return new BaseResult<>(HttpStatus.SUCCESS, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @return 错误信息
     */
    public static <T> BaseResult<T> error()
    {
        return BaseResult.error("操作失败");
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static <T> BaseResult<T> error(String msg)
    {
        return BaseResult.error(msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static <T> BaseResult<T> error(String msg,T data)
    {
        return new BaseResult<>(HttpStatus.ERROR, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @param code 状态码
     * @param msg 返回内容
     * @return 警告消息
     */
    public static <T> BaseResult <T> error(int code, String msg)
    {
        return new BaseResult<>(code, msg, null);
    }


    public static <T> BaseResult<T> toAjax(int num){
        return num > 0 ? BaseResult.success() : BaseResult.error();
    }

    public static void main(String[] args) {
        System.out.println(BaseResult.success().data);
    }

    public Object get(String tag) {
        return switch (tag) {
            case MSG_TAG -> msg;
            case CODE_TAG -> code;
            case DATA_TAG -> data;
            default -> null;
        };

    }
        public void put (String tag, Object o){
            switch (tag) {
                case MSG_TAG:
                    this.msg = (String) o;
                case CODE_TAG:
                    this.code = (Integer) o;
                case DATA_TAG:
                    this.data = (T) o;
            }
        }
    }