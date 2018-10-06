package com.github.johnnyjayjay.discord.commandapi;

import net.dv8tion.jda.bot.JDABot;
import net.dv8tion.jda.client.JDAClient;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.hooks.IEventManager;
import net.dv8tion.jda.core.managers.AudioManager;
import net.dv8tion.jda.core.managers.Presence;
import net.dv8tion.jda.core.requests.RestAction;
import net.dv8tion.jda.core.requests.restaction.GuildAction;
import net.dv8tion.jda.core.utils.cache.CacheView;
import net.dv8tion.jda.core.utils.cache.SnowflakeCacheView;

import java.util.Collection;
import java.util.List;

/**
 * @author Johnny_JayJay
 * @version 0.1-SNAPSHOT
 */
public class JDAMock implements JDA {

    @Override
    public JDA awaitStatus(Status status) throws InterruptedException {
        return null;
    }

    @Override
    public IEventManager getEventManager() {
        return null;
    }

    @Override
    public RestAction<Webhook> getWebhookById(String s) {
        return null;
    }

    @Override
    public Status getStatus() {
        return null;
    }

    @Override
    public long getPing() {
        return 0;
    }

    @Override
    public List<String> getCloudflareRays() {
        return null;
    }

    @Override
    public List<String> getWebSocketTrace() {
        return null;
    }

    @Override
    public void setEventManager(IEventManager iEventManager) {

    }

    @Override
    public void addEventListener(Object... objects) {

    }

    @Override
    public void removeEventListener(Object... objects) {

    }

    @Override
    public List<Object> getRegisteredListeners() {
        return null;
    }

    @Override
    public GuildAction createGuild(String s) {
        return null;
    }

    @Override
    public CacheView<AudioManager> getAudioManagerCache() {
        return null;
    }

    @Override
    public SnowflakeCacheView<User> getUserCache() {
        return null;
    }

    @Override
    public List<Guild> getMutualGuilds(User... users) {
        return null;
    }

    @Override
    public List<Guild> getMutualGuilds(Collection<User> collection) {
        return null;
    }

    @Override
    public RestAction<User> retrieveUserById(String s) {
        return null;
    }

    @Override
    public RestAction<User> retrieveUserById(long l) {
        return null;
    }

    @Override
    public SnowflakeCacheView<Guild> getGuildCache() {
        return null;
    }

    @Override
    public SnowflakeCacheView<Role> getRoleCache() {
        return null;
    }

    @Override
    public SnowflakeCacheView<Category> getCategoryCache() {
        return null;
    }

    @Override
    public SnowflakeCacheView<TextChannel> getTextChannelCache() {
        return null;
    }

    @Override
    public SnowflakeCacheView<VoiceChannel> getVoiceChannelCache() {
        return null;
    }

    @Override
    public SnowflakeCacheView<PrivateChannel> getPrivateChannelCache() {
        return null;
    }

    @Override
    public SnowflakeCacheView<Emote> getEmoteCache() {
        return null;
    }

    @Override
    public SelfUser getSelfUser() {
        return null;
    }

    @Override
    public Presence getPresence() {
        return null;
    }

    @Override
    public ShardInfo getShardInfo() {
        return null;
    }

    @Override
    public String getToken() {
        return null;
    }

    @Override
    public long getResponseTotal() {
        return 0;
    }

    @Override
    public int getMaxReconnectDelay() {
        return 0;
    }

    @Override
    public void setAutoReconnect(boolean b) {

    }

    @Override
    public void setRequestTimeoutRetry(boolean b) {

    }

    @Override
    public boolean isAutoReconnect() {
        return false;
    }

    @Override
    public boolean isAudioEnabled() {
        return false;
    }

    @Override
    public boolean isBulkDeleteSplittingEnabled() {
        return false;
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void shutdownNow() {

    }

    @Override
    public AccountType getAccountType() {
        return null;
    }

    @Override
    public JDAClient asClient() {
        return null;
    }

    @Override
    public JDABot asBot() {
        return null;
    }
}
