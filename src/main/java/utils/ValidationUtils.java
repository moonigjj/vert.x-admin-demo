/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package utils;

import org.hibernate.validator.HibernateValidator;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

/**
 * 使用hibernate的注解来进行验证
 * @author tangyue
 * @version $Id: ValidationUtils.java, v 0.1 2019-02-13 15:38 tangyue Exp $$
 */
public final class ValidationUtils {

    private static Validator validator = Validation
            .byProvider(HibernateValidator.class)
            .configure().failFast(true).buildValidatorFactory().getValidator();

    /**
     * 注解验证参数
     * @param obj
     * @param <T>
     */
    public static <T> String validate(T obj) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
        // 抛出检验异常
        if (constraintViolations.size() > 0) {
            return constraintViolations.iterator().next().getMessage();
        } else {
            return null;
        }
    }
}
