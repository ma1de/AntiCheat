package me.ma1de.anticheat.check;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckData {
    String name();
    CheckType type();
    String description() default "Default check description";
    int maxVl() default 25;
    boolean enabled() default true;
}
