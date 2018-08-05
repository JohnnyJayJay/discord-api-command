package com.github.johnnyjayjay.discord.commandapi;

import net.dv8tion.jda.core.Permission;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SubCommand {

    String name() default "";

    Permission[] botPerms() default {};

    Permission[] memberPerms() default {};
}
