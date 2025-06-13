import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaBotMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.*;
import java.util.concurrent.Executors;

public class Bot extends TelegramLongPollingBot {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
    private final ConcurrentMap<Long, ScheduledFuture<?>> userTimers = new ConcurrentHashMap<>();



    public void stop() {
        scheduler.shutdown();
    }

    private void registerCommands() {
        List<BotCommand> commands = new ArrayList<>();
        commands.add(new BotCommand("/start", "начать работу"));
        commands.add(new BotCommand("/help", "получить помощь"));

        try {
            execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public String getBotUsername() {
        return "MultiThread1336_bot";
    }

    @Override
    public String getBotToken() {
        return "7580476428:AAH4d8RnjAYI1PFVRnftykliVGkPqTLqjls";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (messageText.equals("/start")) {
                SendMessage message = new SendMessage();
                message.setChatId(String.valueOf(chatId));
                message.setText("Привет, я бот. Используй /help дял списка команд.");

                try {
                    execute(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (messageText.startsWith("/таймер")) {
                handleTimerCommand(chatId, messageText);
            } else if (messageText.equals("/stop")) {
            stopTimer(chatId);
        }
        }
    }

    private void handleTimerCommand(long chatId, String text) {
        try {
            String[] parts = text.split(" ");
            if (parts.length != 2) {
                sendText(chatId, "Используйте: /timer <секунды>");
                return;
            }

            int seconds = Integer.parseInt(parts[1]);
            if (seconds <= 0) {
                sendText(chatId, "Укажите положительное число секунд");
                return;
            }

            startTimer(chatId, seconds);
            sendText(chatId, "Таймер установлен на " + seconds + " сек.");

        } catch (NumberFormatException e) {
            sendText(chatId, "Некорректный формат. Используйте: /timer <секунды>");
        }
    }

    private void startTimer(long chatId, int seconds) {
        // Отменяем предыдущий таймер, если был
        stopTimer(chatId);

        ScheduledFuture<?> timer = scheduler.schedule(() -> {
            sendText(chatId, "Таймер сработал! Прошло " + seconds + " сек.");
            userTimers.remove(chatId);
        }, seconds, TimeUnit.SECONDS);

        userTimers.put(chatId, timer);
    }

    private void stopTimer(long chatId) {
        ScheduledFuture<?> timer = userTimers.get(chatId);
        if (timer != null) {
            timer.cancel(false);
            userTimers.remove(chatId);
            sendText(chatId, "Таймер остановлен");
        }
    }

    private void sendText(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.err.println("Ошибка отправки сообщения: " + e.getMessage());
        }
    }




}