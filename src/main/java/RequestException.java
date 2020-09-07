/**
 * @Author: tongq
 * @Date: 2020/9/3 17:59
 * @since：0.0.1
 */
public class RequestException extends RuntimeException{

    private String code;
    private String messages;
    public RequestException(String code, String messages) {
        super(messages);
        this.code = code;
        this.messages = messages;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }
}
