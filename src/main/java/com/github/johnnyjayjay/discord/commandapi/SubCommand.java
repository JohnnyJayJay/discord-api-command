package com.github.johnnyjayjay.discord.commandapi;

import net.dv8tion.jda.core.Permission;

import java.lang.annotation.*;
import java.util.function.Consumer;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SubCommand {

    boolean isDefault() default false;

    String regex() default "";

    int argsLength() default -1;

    Permission[] botPerms() default {};

    Permission[] memberPerms() default {};

}
