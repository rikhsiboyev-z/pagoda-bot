package org.example.tgpocodabot.tg;

import lombok.RequiredArgsConstructor;
import org.example.tgpocodabot.pagoda.PagodaFeign;
import org.example.tgpocodabot.pagoda.dto.WeatherResponseDTO;
import org.example.tgpocodabot.user.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
public class MyPagodaBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.username}")
    private String USERNAME_BOT;
    @Value("${telegram.bot.token}")
    private String TOKEN_BOT;

    @Value("${pagoda.key}")
    private String PAGODA_KEY;

    private final PagodaFeign pagodaFeign;


    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {
            Message message = update.getMessage();
            Long chatId = message.getChatId();
            sendMessage(chatId, "\uD83D\uDC4B\uD83C\uDFFB Assalomu Alaykum! " + message.getChat().getFirstName());
            sendInlineKeyboard(chatId.toString());

            if (message.hasLocation()) {
                Location location = message.getLocation();
                Double latitude = location.getLatitude();
                Double longitude = location.getLongitude();

                WeatherResponseDTO weatherForecast =
                        pagodaFeign.getWeatherForecast(PAGODA_KEY, latitude + "," + longitude, formatMessageDate(message), "13");

                sendMessage(message.getChatId(), weatherForecast.toString());
            }

        }

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            Message callbackMessage = callbackQuery.getMessage();

            if (callbackMessage != null) {
                Long chatId = callbackMessage.getChatId();
                String data = callbackQuery.getData();

                String[] regions = {
                        "    📍 Toshkent    ",
                        "    📍 Andijon     ",
                        "    📍 Buxoro      ",
                        "    📍 Fargona    ",
                        "    📍 Jizzax      ",
                        "    📍 Xorazm      ",
                        "    📍 Namangan    ",
                        "    📍 Navoiy      ",
                        "    📍 Qashqadaryo ",
                        "    📍 Samarqand   ",
                        "    📍 Sirdaryo    ",
                };

                for (String region : regions) {
                    if (region.equals(data)) {
                        sendMessage(chatId, "Siz tanladingiz " + data);

                        WeatherResponseDTO weatherForecast = pagodaFeign.getWeatherForecast(
                                PAGODA_KEY,
                                data,
                                formatMessageDate(callbackMessage.getDate()),
                                formatMessageTime(callbackMessage.getDate()));
                        sendMessage(chatId, weatherForecast.toString());
                        break;
                    }

                }
                if (data.equals(" Lokatsiya yuborish ")) {
                    SendMessage sendMessage = getSendMessage(chatId);

                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                }
                if (data.equals("  Haftalik malumot  ")) {
                    WeatherResponseDTO tashkent = pagodaFeign.getWeatherDaysForecast(PAGODA_KEY, "Tashkent", 3, "13");
                    sendMessage(chatId, tashkent.toString());


                }
            }
        }

    }

    private SendMessage getSendMessage(Long chatId) {
        SendMessage sendMessage = new SendMessage();
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        KeyboardButton locationButton = new KeyboardButton("Mening joylashuvimni yuborish");
        locationButton.setRequestLocation(true);
        row.add(locationButton);

        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);

        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(keyboardMarkup);
        sendMessage.setText("Iltimos, joylashuvingizni ulashing");
        return sendMessage;
    }

    public void sendInlineKeyboard(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("\uD83C\uDF03 Sizga qaysi hudud haqida malumot kerak ?");

        InlineKeyboardMarkup inlineKeyboardMarkup = getInlineKeyboardMarkup();
        message.setReplyMarkup(inlineKeyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private static InlineKeyboardMarkup getInlineKeyboardMarkup() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        String[] regions = {
                "    📍 Toshkent    ",
                "    📍 Andijon     ",
                "    📍 Buxoro      ",
                "    📍 Fargona    ",
                "    📍 Jizzax      ",
                "    📍 Xorazm      ",
                "    📍 Namangan    ",
                "    📍 Navoiy      ",
                "    📍 Qashqadaryo ",
                "    📍 Samarqand   ",
                "    📍 Sirdaryo    ",
                " Lokatsiya yuborish ",
        };

        for (String region : regions) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton(region);
            button.setCallbackData(region);
            row.add(button);
            keyboard.add(row);
        }

        inlineKeyboardMarkup.setKeyboard(keyboard);

        return inlineKeyboardMarkup;

    }

    private void sendMessage(Long chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public String formatMessageDate(Integer message) {

        long timestampInMilliseconds = message * 1000L;

        Instant instant = Instant.ofEpochMilli(timestampInMilliseconds);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM.dd.yy");
        return localDateTime.format(formatter);
    }

    public String formatMessageDate(Message message) {

        Integer date = message.getDate();
        long timestampInMilliseconds = date * 1000L;

        Instant instant = Instant.ofEpochMilli(timestampInMilliseconds);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM.dd.yy");
        return localDateTime.format(formatter);
    }

    public String formatMessageTime(Integer message) {
        long timestampInMilliseconds = message * 1000L;

        Instant instant = Instant.ofEpochMilli(timestampInMilliseconds);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH");
        return localDateTime.format(formatter);
    }

    @Override
    public String getBotUsername() {
        return USERNAME_BOT;
    }

    @Override
    public String getBotToken() {
        return TOKEN_BOT;
    }
}
