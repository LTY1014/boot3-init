package com.lty.util;

import com.lty.constant.UserConstant;
import org.springframework.util.DigestUtils;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.regex.Pattern;

/**
 * 密码工具类
 *
 * @author lty
 */
public class PasswordUtil {

    private static final Pattern UPPER_CASE = Pattern.compile("[A-Z]");  // 大写字母
    private static final Pattern LOWER_CASE = Pattern.compile("[a-z]");  // 小写字母
    private static final Pattern DIGIT = Pattern.compile("\\d");  // 数字
    private static final Pattern SPECIAL_CHAR = Pattern.compile("[!@#$%^&*(),.?:{}|<>]");  // 特殊字符
    private static final int SALT_LENGTH = 16;  // 盐值长度

    /**
     * 验证密码是否匹配
     */
    public static boolean isValidPassword(String inputPassword, String storedPassword) {
        String hashedInput = encodePassword(inputPassword);
        return hashedInput.equals(storedPassword);
    }

    /**
     * 密码加密
     */
    public static String encodePassword(String password) {
        return DigestUtils.md5DigestAsHex((UserConstant.SALT + password).getBytes());
    }

    /**
     * 检查密码强度（复杂度）
     */
    public static boolean isPasswordStrong(String password) {
        return password.length() >= UserConstant.PASSWORD_LENGTH &&
                UPPER_CASE.matcher(password).find() &&
                LOWER_CASE.matcher(password).find() &&
                DIGIT.matcher(password).find() &&
                SPECIAL_CHAR.matcher(password).find();
    }

    /**
     * 获取密码强度详情
     */
    public static String getPasswordStrengthInfo(String password) {
        StringBuilder sb = new StringBuilder();
        if (password.length() < UserConstant.PASSWORD_LENGTH) {
            sb.append("密码长度至少为" + UserConstant.PASSWORD_LENGTH + "个字符\n");
        }
        if (!UPPER_CASE.matcher(password).find()) {
            sb.append("密码必须包含大写字母\n");
        }
        if (!LOWER_CASE.matcher(password).find()) {
            sb.append("密码必须包含小写字母\n");
        }
        if (!DIGIT.matcher(password).find()) {
            sb.append("密码必须包含数字\n");
        }
        if (!SPECIAL_CHAR.matcher(password).find()) {
            sb.append("密码必须包含特殊字符\n");
        }
        return sb.toString();
    }

    /**
     * 生成随机密码（包含数字、大小写字母、特殊字符）
     */
    public static String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_-+=<>?";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    /**
     * 生成随机盐值（Base64编码）
     */
    public static String generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * 密码工具类测试
     *
     * @author lty
     */
    public static void main(String[] args) {
        String password = "123456";
        String hashedPassword = encodePassword(password);
        System.out.println("哈希后密码: " + hashedPassword);
        System.out.println("验证结果: " + isValidPassword(password, hashedPassword));
        System.out.println("密码强度: " + isPasswordStrong(password));
        System.out.println("密码强度详情: " + getPasswordStrengthInfo(password));
    }

    // 私有构造函数
    private PasswordUtil() {
        throw new IllegalStateException("工具类不可实例化");
    }
}