package com.github.johnnyjayjay.discord.commandapi;

import net.dv8tion.jda.core.JDA;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.*;

/**
 * @author Johnny_JayJay
 * @version 0.1-SNAPSHOT
 */
public class CommandSettingsTest {

    private CommandSettings settings;
    private static JDA jdaMock;
    private static ThreadLocalRandom random;

    @BeforeClass
    public static void createJDAMock() {
        jdaMock = new JDAMock();
    }

    @Before
    public void before() {
        random = ThreadLocalRandom.current();
        settings = new CommandSettings("!", jdaMock, true);
        assertEquals("Default prefix given in constructor was not applied", "!", settings.getPrefix());
        assertFalse("Settings are activated before activation", settings.isActivated());
        assertTrue("labelIgnoreCase was not applied", settings.isLabelIgnoreCase());
    }

    @Test
    public void setCustomPrefixTest() {
        for (int i = 0; i < 10000; i++) {
            String prefix = randomString(true);
            long id = random.nextLong();
            settings.setCustomPrefix(id, prefix);
            assertEquals("One prefix was not set correctly (manual adding)", prefix, settings.getPrefix(id));
        }
        Map<Long, String> prefixes = new HashMap<>();
        for (int i = 0; i < 10000; i++)
            prefixes.put(random.nextLong(), randomString(true));
        settings.setCustomPrefixes(prefixes);
        prefixes.forEach((id, prefix) -> assertEquals("One prefix was not set correctly (bulk adding)", prefix, settings.getPrefix(id)));
    }

    @Test
    public void generalSettingsTest() {
        for (int i = 0; i < 10000; i++) {
            long cooldown = random.nextLong();
            settings.setCooldown(cooldown);
            assertEquals("Cooldown was not set correctly", cooldown, settings.getCooldown());
        }
        settings.setResetCooldown(true);
        assertTrue("Reset cooldown boolean was not set correctly", settings.isResetCooldown());
        settings.setHelpCommandColor(Color.GREEN);
        assertEquals("Help Command Color was not set correctly", Color.GREEN, settings.getHelpColor());
        settings.setBotExecution(true);
        assertTrue("Bot execution boolean was not set correctly", settings.botsMayExecute());
        settings.setDefaultPrefix("newPrefix");
        assertEquals("Default prefix was not set correctly (after the constructor)", "newPrefix", settings.getPrefix());
    }

    @Test
    public void commandsTest() {
        ICommand command = (event, member, channel, args) -> {};
        Set<String> randomLabels = new HashSet<>();
        for (int i = 0; i < 10000; i++)
            randomLabels.add(randomString(false).toLowerCase());
        settings.put(command, randomLabels);
        Set<String> actualLabels = settings.getLabels(command);
        assertEquals("Labels for command were not set correctly", actualLabels, randomLabels);
        randomLabels.stream().findAny().ifPresent((label) -> assertTrue("Label was not removed correctly", settings.remove(label)));
        settings.put(command, "label");
        assertTrue("Single label addition did not work", settings.getLabels(command).contains("label"));
        settings.clearCommands();
        assertTrue("Commands were not cleared correctly", settings.getCommands().isEmpty());
    }

    @Test
    public void blacklistTest() {
        Set<Long> bulkChannelIds = new HashSet<>();
        for (int i = 0; i < 10000; i++) {
            long id = random.nextLong();
            bulkChannelIds.add(id);
            settings.addChannelToBlacklist(id);
            assertTrue("Channel was not added to blacklist correctly", settings.isBlacklisted(id));
        }
        settings.clearBlacklist();
        assertTrue("Blacklist was not cleared correctly", settings.getBlacklistedChannels().isEmpty());
        settings.addChannelsToBlacklist(bulkChannelIds);
        assertEquals("Channel ids were not added correctly to the blacklist", bulkChannelIds, settings.getBlacklistedChannels());
    }

    @Test
    public void activationTest() {
        settings.activate();
        assertTrue("CommandSettings were not activated correctly", settings.isActivated());
        settings.deactivate();
        assertFalse("CommandSettings were not deactivated correctly", settings.isActivated());
    }


    private String randomString(boolean replaceIllegalPrefixChars) {
        int endIndex = random.nextInt(1, 36);
        int beginIndex = random.nextInt(endIndex);
        String ret = UUID.randomUUID().toString().substring(beginIndex, endIndex);
        return replaceIllegalPrefixChars ? ret.replaceAll(CommandSettings.VALID_PREFIX.replace("^", ""), "") : ret;
    }

}
