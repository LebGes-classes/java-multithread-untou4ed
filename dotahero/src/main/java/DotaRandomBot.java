import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DotaRandomBot extends TelegramLongPollingBot {
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final Map<Long, List<String>> userHistory = new ConcurrentHashMap<>();

    // Списки героев и позиций Dota 2
    private final List<String> heroes = Arrays.asList(
            "Abaddon", "Alchemist", "Ancient Apparition", "Anti-Mage", "Arc Warden",
            "Axe", "Bane", "Batrider", "Beastmaster", "Bloodseeker",
            "Bounty Hunter", "Brewmaster", "Bristleback", "Broodmother", "Centaur Warrunner",
            "Chaos Knight", "Chen", "Clinkz", "Clockwerk", "Crystal Maiden",
            "Dark Seer", "Dark Willow", "Dawnbreaker", "Dazzle", "Death Prophet",
            "Disruptor", "Doom", "Dragon Knight", "Drow Ranger", "Earth Spirit",
            "Earthshaker", "Elder Titan", "Ember Spirit", "Enchantress", "Enigma",
            "Faceless Void", "Grimstroke", "Gyrocopter", "Hoodwink", "Huskar",
            "Invoker", "Io", "Jakiro", "Juggernaut", "Keeper of the Light",
            "Kunkka", "Legion Commander", "Leshrac", "Lich", "Lifestealer",
            "Lina", "Lion", "Lone Druid", "Luna", "Lycan",
            "Magnus", "Marci", "Mars", "Medusa", "Meepo",
            "Mirana", "Monkey King", "Morphling", "Muerta", "Naga Siren",
            "Nature's Prophet", "Necrophos", "Night Stalker", "Nyx Assassin", "Ogre Magi",
            "Omniknight", "Oracle", "Outworld Destroyer", "Pangolier", "Phantom Assassin",
            "Phantom Lancer", "Phoenix", "Primal Beast", "Puck", "Pudge",
            "Pugna", "Queen of Pain", "Razor", "Riki", "Rubick",
            "Sand King", "Shadow Demon", "Shadow Fiend", "Shadow Shaman", "Silencer",
            "Skywrath Mage", "Slardar", "Slark", "Snapfire", "Sniper",
            "Spectre", "Spirit Breaker", "Storm Spirit", "Sven", "Techies",
            "Templar Assassin", "Terrorblade", "Tidehunter", "Timbersaw", "Tinker",
            "Tiny", "Treant Protector", "Troll Warlord", "Tusk", "Underlord",
            "Undying", "Ursa", "Vengeful Spirit", "Venomancer", "Viper",
            "Visage", "Void Spirit", "Warlock", "Weaver", "Windranger",
            "Winter Wyvern", "Witch Doctor", "Wraith King", "Zeus");

    private final List<String> positions = Arrays.asList(
            "Керри (1 позиция)", "Мидер (2 позиция)", "Оффлейнер (3 позиция)",
            "Полуспорт (4 позиция)", "Фулл сапорт (5 позиция)");

    @Override
    public String getBotUsername() {
        return "dota2randomhero_newbot";
    }

    @Override
    public String getBotToken() {
        return "7633320277:AAFD4ZHiqBetwblgp3amOzJ4FJbgQILeMHM";
    }

    @Override
    public void onUpdateReceived(Update update) {
        executorService.execute(() -> handleUpdate(update));
    }

    private void handleUpdate(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        long chatId = update.getMessage().getChatId();
        String messageText = update.getMessage().getText();
        String response;

        switch (messageText) {
            case "/start":
                response = "Привет! Я бот для выбора рандомного героя или позиции " +
                        "в Dota2.\n" +
                        "Используй команды:\n" +
                        "/hero - случайный герой\n" +
                        "/position - случайная позиция\n" +
                        "/history - история твоих выборов";
                break;
            case "/hero":
                String randomHero = getRandomElement(heroes);
                addToHistory(chatId, "Герой: " + randomHero);
                response = "Твой герой: " + randomHero;
                break;
            case "/position":
                String randomPosition = getRandomElement(positions);
                addToHistory(chatId, "Позиция: " + randomPosition);
                response = "Твоя позиция: " + randomPosition;
                break;
            case "/history":
                response = getUserHistory(chatId);
                break;
            default:
                response = "Неизвестная команда. Используй /start для списка команд";
        }

        sendMessage(chatId, response);
    }

    private String getRandomElement(List<String> list) {
        Random random = new Random();
        return list.get(random.nextInt(list.size()));
    }

    private synchronized void addToHistory(long userId, String entry) {
        userHistory.computeIfAbsent(userId, k -> new ArrayList<>()).add(entry);
    }

    private synchronized String getUserHistory(long userId) {
        List<String> history = userHistory.getOrDefault(userId, Collections.emptyList());
        if (history.isEmpty()) {
            return "У тебя пока нет истории выборов";
        }

        StringBuilder sb = new StringBuilder("Твоя история:\n");
        for (int i = 0; i < history.size(); i++) {
            sb.append(i + 1).append(". ").append(history.get(i)).append("\n");
        }
        return sb.toString();
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClosing() {
        executorService.shutdown();
        super.onClosing();
    }
}