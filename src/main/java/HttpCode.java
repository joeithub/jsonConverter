/**
 * @Author: tongq
 * @Date: 2020/9/3 18:02
 * @since：0.0.1
 */
public enum HttpCode {
    /**
     * 请求正常返回
     */
    OK("200", "OK"),
    /**
     * 请求异常返回
     */
    ERROR("500", "系统内部错误"),

    ERR_PARAMS_INVALID("2000", "参数非法");


    private String code;
    private String desc;

    HttpCode(String c, String d) {
        this.code = c;
        this.desc = d;
    }

    public String code() {
        return code;
    }

    public String desc() {
        return desc;
    }

}
