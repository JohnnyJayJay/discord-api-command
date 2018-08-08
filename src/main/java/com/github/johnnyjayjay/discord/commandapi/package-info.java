/**
 * Created and maintained by JohnnyJayJay.<p>
 * License:
 * The MIT License (MIT)
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.<p>
 * <p>
 * This is the main package for the CommandAPI.<br>
 * This framework is built around the interface {@link com.github.johnnyjayjay.discord.commandapi.ICommand ICommand}.
 * It is the base interface for the user to create own commands. An alternative implementation is {@link com.github.johnnyjayjay.discord.commandapi.AbstractCommand AbstractCommand}.<br>
 * {@link com.github.johnnyjayjay.discord.commandapi.CommandSettings CommandSettings} is also one of the most important classes. It is used to control the commands and their execution as well as some other features.<br>
 * {@link com.github.johnnyjayjay.discord.commandapi.AbstractHelpCommand AbstractHelpCommand} and {@link com.github.johnnyjayjay.discord.commandapi.DefaultHelpCommand DefaultHelpCommand} belong to
 * the help command feature of this framework. AbstractHelpCommand provides a template for own implementations, whereas DefaultHelpCommand is the default implementation of this abstract class.<br>
 * {@link com.github.johnnyjayjay.discord.commandapi.CommandEvent CommandEvent} is a sub class of {@code GuildMessageReceivedEvent} from JDA. It is used as a parameter type in {@code onCommand} from
 * {@link com.github.johnnyjayjay.discord.commandapi.ICommand ICommand}.<br>
 * {@link com.github.johnnyjayjay.discord.commandapi.SubCommand SubCommand} is an annotation that is used in {@link com.github.johnnyjayjay.discord.commandapi.AbstractCommand AbstractCommand} to declare methods as sub command
 * methods. To see how this works, please refer to the examples or the readme file on github.<br>
 * {@link com.github.johnnyjayjay.discord.commandapi.CommandSetException CommandSetException} is a {@code RuntimeException} that is thrown in case of any problems with {@link com.github.johnnyjayjay.discord.commandapi.CommandSettings CommandSettings}.
 * <p>
 * Please read the readme on github and take a look at the examples to see how it works.<br>I will give this documentation a revision in the future to provide more examples and helpful code.
 */
package com.github.johnnyjayjay.discord.commandapi;