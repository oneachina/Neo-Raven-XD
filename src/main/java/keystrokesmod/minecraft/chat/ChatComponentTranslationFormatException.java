package keystrokesmod.minecraft.chat;

/**
 * 自定义异常类，用于处理 ChatComponentTranslation 格式化过程中出现的异常。
 */
public class ChatComponentTranslationFormatException extends RuntimeException {

    /**
     * 构造方法，仅包含错误信息。
     * @param message 错误信息
     */
    public ChatComponentTranslationFormatException(String message) {
        super(message);
    }

    /**
     * 构造方法，包含错误信息和引发异常的对象。
     * @param component 引发异常的 ChatComponentTranslation 对象
     * @param message 错误信息
     */
    public ChatComponentTranslationFormatException(ChatComponentTranslation component, String message) {
        super("Error in ChatComponentTranslation (key: " + component.getKey() + "): " + message);
    }

    /**
     * 构造方法，包含错误信息、引发异常的对象和原始异常。
     * @param component 引发异常的 ChatComponentTranslation 对象
     * @param cause 原始异常
     */
    public ChatComponentTranslationFormatException(ChatComponentTranslation component, Throwable cause) {
        super("Error in ChatComponentTranslation (key: " + component.getKey() + ")", cause);
    }

    /**
     * 构造方法，包含错误信息、引发异常的对象和索引。
     * @param component 引发异常的 ChatComponentTranslation 对象
     * @param index 错误索引
     */
    public ChatComponentTranslationFormatException(ChatComponentTranslation component, int index) {
        super("Index out of bounds in ChatComponentTranslation (key: " + component.getKey() + ", index: " + index + ")");
    }
}